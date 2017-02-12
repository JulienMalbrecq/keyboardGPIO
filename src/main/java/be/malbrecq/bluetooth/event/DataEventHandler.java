package be.malbrecq.bluetooth.event;

public interface DataEventHandler {
    void handleEvent(DataReceivedEvent event);
    boolean supportEvent(DataReceivedEvent event);
}
