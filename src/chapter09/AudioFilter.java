package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.LowPassFilter;

/**
 * The examples demonstrates a Low Pass filter that lets low frequencies pass
 * but cuts off all frequences higher than a cut-off value (here 0.1f). The
 * filter also reduces the volume by half. This creates a muffled sound.
 *
 * @author zathras
 */
public class AudioFilter extends SimpleApplication {

    private AudioNode src;
    private boolean filterIsOn = false;

    public static void main(String[] args) {
        AudioFilter test = new AudioFilter();
        test.start();
    }

    @Override
    public void simpleInitApp() {
        src = new AudioNode(assetManager,
                "Sounds/Effects/Foot steps.ogg", true);
        src.setVolume(10);
        System.out.println("Playing all frequencies unfiltered");
        src.play();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (src.getStatus() != src.getStatus().Playing) {
            filterIsOn = !filterIsOn; // toggle
            src = new AudioNode(assetManager,
                    "Sounds/Effects/Foot steps.ogg", true);
            if (filterIsOn) {
                src.setDryFilter(new LowPassFilter(.5f, .1f));
                System.out.println("Playing with low pass filter");
            } else {
                src.setDryFilter(new LowPassFilter(1f, 1f));
                System.out.println("Playing all frequencies unfiltered");
            }
            src.setVolume(10);
            src.play();
        }
    }
}
