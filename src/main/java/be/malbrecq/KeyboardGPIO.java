package be.malbrecq;

import be.malbrecq.bluetooth.BTCommunicator;
import be.malbrecq.bluetooth.Communicator;
import be.malbrecq.bluetooth.event.DataEventHandler;
import be.malbrecq.bluetooth.event.DataReceivedEvent;
import be.malbrecq.spotify.SpotifyController;
import be.malbrecq.utils.Shortcuts;
import be.malbrecq.utils.VirtualKeyboardImpl;
import java.awt.*;
import java.util.logging.Logger;
import java.util.UUID;

// libbluetooth-dev required

public class KeyboardGPIO {
    private final static Logger log = Logger.getLogger(KeyboardGPIO.class.getCanonicalName());

    private Shortcuts spotify;
    private UUID serverId;

    public KeyboardGPIO() throws AWTException {
        serverId = UUID.randomUUID();
        spotify = new SpotifyController(new VirtualKeyboardImpl(new Robot()));
    }

    public void init() {
        DataEventHandler knockEventHandler = new DataEventHandler() {
            @Override
            public void handleEvent(DataReceivedEvent event) {
                if (supportEvent(event)) {
                    log.info((String)event.getData());
                    spotify.next();

                }
            }

            @Override
            public boolean supportEvent(DataReceivedEvent event) {
                Object data = event.getData();
                return data instanceof String && ((String) data).contains(serverId.toString());
            }
        };

        Communicator communicator = new BTCommunicator();
        communicator.setServerId(serverId);
        communicator.addEventHandler(knockEventHandler);
        communicator.start();
    }

    public static void main(String[] args) throws InterruptedException, AWTException {
        KeyboardGPIO key = new KeyboardGPIO();
        key.init();
    }
}
