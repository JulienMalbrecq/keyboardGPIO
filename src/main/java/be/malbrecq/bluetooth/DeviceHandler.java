package be.malbrecq.bluetooth;

import java.io.DataInputStream;
import java.util.UUID;

public interface DeviceHandler {
    boolean init();
    DataInputStream startListening(UUID serverId);
}
