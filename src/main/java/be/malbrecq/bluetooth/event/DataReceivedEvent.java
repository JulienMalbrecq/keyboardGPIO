package be.malbrecq.bluetooth.event;

public interface DataReceivedEvent<Type> {
    void setData(Type data);
    Type getData();
}
