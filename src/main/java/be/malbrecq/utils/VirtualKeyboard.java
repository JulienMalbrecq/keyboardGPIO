package be.malbrecq.utils;

public interface VirtualKeyboard {
    void press(Integer... keys);
    void thenPress(Integer... keys);
}
