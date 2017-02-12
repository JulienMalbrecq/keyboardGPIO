package be.malbrecq.bluetooth;

import be.malbrecq.bluetooth.event.DataEventHandler;
import be.malbrecq.bluetooth.event.DataReceivedEvent;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class BTCommunicator implements Communicator {
    private final Logger log = Logger.getLogger(BTCommunicator.class.toString());
    private List<DataEventHandler> eventHandler;

    private UUID serverId = UUID.randomUUID();

    public BTCommunicator() {
        this.eventHandler = new ArrayList<>();
    }

    @Override
    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    @Override
    public void addEventHandler(DataEventHandler handler) {
        eventHandler.add(handler);
    }

    @Override
    public void onDataReceived(DataReceivedEvent event) {
        for (DataEventHandler handler: eventHandler) {
            handler.handleEvent(event);
        }
    }

    @Override
    public void start() {
        DeviceHandler handler = new BTDiscoveryListener();

        if (handler.init()) {
            InputStream din = handler.startListening(serverId);
            if (din != null) {
                Thread inputThread = new BTInputListenerThread(din, this);
                inputThread.start();
            } else {
                log.warning("Could not initiate inputStream");
            }
        }
    }
}
