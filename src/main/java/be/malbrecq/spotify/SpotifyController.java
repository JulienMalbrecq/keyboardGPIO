package be.malbrecq.spotify;

import be.malbrecq.utils.AbstractProgramFocus;
import be.malbrecq.utils.Shortcuts;
import be.malbrecq.utils.VirtualKeyboard;

import java.awt.event.KeyEvent;

public class SpotifyController extends AbstractProgramFocus implements Shortcuts {
    public final String WINDOW_NAME = "Spotify";
    public final String LAUNCH_COMMAND = "/bin/sh -c spotify";

    private final VirtualKeyboard keyboard;

    public SpotifyController(VirtualKeyboard keyboard) {
        this.keyboard = keyboard;
        launch();
    }

    @Override
    protected String getName() {
        return WINDOW_NAME;
    }

    @Override
    protected String getLaunchCommand() {
        return LAUNCH_COMMAND;
    }

    @Override
    public void quit() {
        playPause();
        super.quit();
    }

    public void previous() {
        keyboard.press(KeyEvent.VK_CONTROL, KeyEvent.VK_LEFT);
        keyboard.thenPress(KeyEvent.VK_CONTROL, KeyEvent.VK_LEFT);
    }

    public void next() {
        keyboard.press(KeyEvent.VK_CONTROL, KeyEvent.VK_RIGHT);
    }

    public void playPause() {
        keyboard.press(KeyEvent.VK_SPACE);
    }


}
