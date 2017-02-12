package be.malbrecq.bluetooth;

import be.malbrecq.bluetooth.event.DataEventHandler;
import be.malbrecq.bluetooth.event.DataReceivedEvent;

import java.util.UUID;

public interface Communicator {
    void start();
    void onDataReceived(DataReceivedEvent data);
    void addEventHandler(DataEventHandler handler);
    void setServerId(UUID serverId);
}
