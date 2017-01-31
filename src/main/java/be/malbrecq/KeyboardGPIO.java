package be.malbrecq;

import be.malbrecq.utils.ProgramFocus;
import be.malbrecq.utils.Shortcuts;
import be.malbrecq.spotify.SpotifyController;
import be.malbrecq.utils.VirtualKeyboardImpl;

import java.awt.*;
import java.util.logging.Logger;

public class KeyboardGPIO {
    private final static Logger log = Logger.getLogger(KeyboardGPIO.class.getCanonicalName());

    public static void main(String[] args) throws InterruptedException, AWTException {
        // focus on spotify
        Shortcuts spotify = new SpotifyController(new VirtualKeyboardImpl(new Robot()));

        for (int i = 5; i >= 0; i--) {
             log.info("Starts in " + i);
            Thread.sleep(1000);
        }

        log.info("Playing song");
        spotify.playPause();
        Thread.sleep(5000);

        log.info("Next song");
        spotify.next();
        Thread.sleep(5000);

        log.info("Previous song");
        spotify.previous();
        Thread.sleep(5000);

        ((ProgramFocus)spotify).quit();
    }
}
