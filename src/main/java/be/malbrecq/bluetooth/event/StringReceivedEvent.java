package be.malbrecq.bluetooth.event;

public class StringReceivedEvent implements DataReceivedEvent<String> {
    private String data;

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return data;
    }
}
