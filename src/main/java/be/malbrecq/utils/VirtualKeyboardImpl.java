package be.malbrecq.utils;

import java.awt.*;

public class VirtualKeyboardImpl implements VirtualKeyboard {
    private final Robot robot;

    public VirtualKeyboardImpl(Robot robot) {
        this.robot = robot;
    }

    public void press(Integer... keys) {
        for (int key: keys) {
            robot.keyPress(key);
        }

        robot.delay(50);

        for (int key: keys) {
            robot.keyRelease(key);
        }
    }

    public void thenPress(Integer... keys) {
        robot.delay(50);
        press(keys);
    }
}
