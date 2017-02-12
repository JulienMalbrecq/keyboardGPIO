package be.malbrecq.bluetooth;

import be.malbrecq.bluetooth.event.DataReceivedEvent;
import be.malbrecq.bluetooth.event.StringReceivedEvent;

import java.io.InputStream;
import java.util.logging.Logger;

public class BTInputListenerThread extends Thread {
    private final Logger log = Logger.getLogger(BTInputListenerThread.class.toString());

    private final String TERMINATOR = "\n";

    private Communicator communicator;
    private InputStream in;

    private String receivedData = "";

    public BTInputListenerThread(InputStream in, Communicator communicator) {
        this.in = in;
        this.communicator = communicator;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (in.available() > 0) {
                    byte buffer[] = new byte[in.available()];
                    in.read(buffer);
                    String input = new String(buffer);
                    analizeData(input);
                } else {
                    sleep(100);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void analizeData(String data) {
        for (char c : data.toCharArray()) {
            String charAsString = Character.toString(c);
            if (charAsString.equals(TERMINATOR)) {
                sendData();
            } else {
                receivedData += charAsString;
            }
        }
    }

    private void sendData() {
        DataReceivedEvent<String> event = new StringReceivedEvent();
        event.setData(receivedData);

        communicator.onDataReceived(event);
        receivedData = "";
    }
}
