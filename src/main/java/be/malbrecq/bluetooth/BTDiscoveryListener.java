package be.malbrecq.bluetooth;

import javax.bluetooth.*;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class BTDiscoveryListener implements DiscoveryListener, DeviceHandler {
    private static final String SERVER_PREFIX = "SRV";
    private final Logger log = Logger.getLogger(BTDiscoveryListener.class.toString());
    private Object lock;

    public List<RemoteDevice> devices = new ArrayList<>();
    public List<ServiceRecord> services = new ArrayList<>();

    public BTDiscoveryListener() {
        this.lock = new Object();
    }

    public List<RemoteDevice> getDevices() {
        return devices;
    }

    public List<ServiceRecord> getServices() {
        return services;
    }

    public boolean init() {
        // setup
        LocalDevice localDevice = null;
        try {
            localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();

            // discover devices
            agent.startInquiry(DiscoveryAgent.GIAC, this);
            sync();

            if (devices.isEmpty()) {
                log.info("No device found");
                return false;
            } else {
                log.info("Device Inquiry Completed.");
            }

            UUID[] uuidSet = new UUID[1];
            uuidSet[0] = new UUID(0x1101); //Serial port
            int[] attrIDs =  new int[] {
                    0x0100 // Service name
            };

            for (RemoteDevice device : devices) {
                agent.searchServices(attrIDs, uuidSet, device, this);
                sync();

                log.info("Service search finished.");
            }
        } catch (BluetoothStateException e) {
            log.warning("Exception while initiating devices: " + e.getMessage());
        }

        return !services.isEmpty();
    }

    @Override
    public DataInputStream startListening(java.util.UUID serverId) {
        DataInputStream din = null;

        for (ServiceRecord service: services) {
            String url = service.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);

            try {
                StreamConnection streamConnection = (StreamConnection) Connector.open(url);
                din = streamConnection.openDataInputStream();

                DataOutputStream dout = streamConnection.openDataOutputStream();
                dout.writeBytes(SERVER_PREFIX + serverId.toString());
                dout.flush();
            } catch (IOException e) {
                log.warning("Exception while opening input stream: " + e.getMessage());
            }
        }

        return din;
    }

    @Override
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        String name;
        try {
            name = remoteDevice.getFriendlyName(false);
        } catch (IOException e) {
            name = remoteDevice.getBluetoothAddress();
        }

        log.info("Device found: " + name);
        devices.add(remoteDevice);
    }

    @Override
    public void servicesDiscovered(int transId, ServiceRecord[] serviceRecords) {
        for (ServiceRecord serviceRecord : serviceRecords) {
            String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            if (url == null) {
                log.warning("url is null");
                continue;
            }

            DataElement serviceName = serviceRecord.getAttributeValue(0x0100);
            if (serviceName != null) {
                log.info("service " + serviceName.getValue() + " found " + url);

            } else {
                log.info("service found " + url);
            }

            services.add(serviceRecord);
        }
    }

    @Override
    public void serviceSearchCompleted(int i, int i1) {
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void inquiryCompleted(int i) {
        synchronized (lock) {
            lock.notify();
        }
    }

    private void sync() {
        try {
            synchronized(lock){
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
