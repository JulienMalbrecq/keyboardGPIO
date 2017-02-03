package be.malbrecq;

import be.malbrecq.utils.Shortcuts;
import be.malbrecq.spotify.SpotifyController;
import be.malbrecq.utils.VirtualKeyboardImpl;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.awt.*;
import java.util.logging.Logger;

public class KeyboardGPIO {
    private final static Logger log = Logger.getLogger(KeyboardGPIO.class.getCanonicalName());

    private Shortcuts spotify;
    private GpioPinDigitalInput piezo;

    public KeyboardGPIO() throws AWTException {
        final GpioController gpio = GpioFactory.getInstance();

        spotify = new SpotifyController(new VirtualKeyboardImpl(new Robot()));
        piezo = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
    }

    public void init() {
        piezo.setShutdownOptions(true);

        // create and register gpio pin listener
        piezo.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                log.info(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

                if (event.getState().equals(PinState.HIGH)) {
                    spotify.next();
                }
            }
        });
    }

    public static void main(String[] args) throws InterruptedException, AWTException {
        KeyboardGPIO key = new KeyboardGPIO();
        key.init();

        while (true) {
            Thread.sleep(500);
        }
    }
}
