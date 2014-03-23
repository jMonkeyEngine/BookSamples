package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * This example demonstrates positional sound. Your hear the sound of a gun shot
 * from various directions. Each quadrant represents a different environment.
 * The ear (listener) moves with the camera.
 *
 * @author zathras
 */
public class PositionalEnvironment extends SimpleApplication {

    private AudioNode shotAudio;
    private Environment e = Environment.AcousticLab;
    private float range = 100;

    public static void main(String[] args) {
        PositionalEnvironment test = new PositionalEnvironment();
        test.start();
    }

    @Override
    public void simpleInitApp() {
        cam.setLocation(Vector3f.ZERO);
        // initialize the sound node
        audioRenderer.setEnvironment(e);
        shotAudio = new AudioNode(assetManager, "Sounds/Effects/Bang.wav");
        shotAudio.setPositional(true);
        shotAudio.setRefDistance(range*.40f); 
        shotAudio.setMaxDistance(range*.75f);     
        shotAudio.setLooping(false);

    }

    @Override
    public void simpleUpdate(float tpf) {
        // Move the listener with the a camera - for 3D audio.
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        // every time the sound source is done playing, move it to a random spot.
        Vector3f randomLoc = new Vector3f();
        if (shotAudio.getStatus().equals(AudioNode.Status.Stopped)) {
            randomLoc.setX(FastMath.nextRandomFloat());
            randomLoc.setY(FastMath.nextRandomFloat());
            randomLoc.setZ(FastMath.nextRandomFloat());
            randomLoc.multLocal(range * 2, 2, range * 2); // scale
            randomLoc.subtractLocal(range, 1, range);     // center
            shotAudio.setLocalTranslation(randomLoc);
            float x = randomLoc.getX();
            float z = randomLoc.getZ();
            if (x > 0 && z > 0 && e != Environment.Dungeon) {
                e = Environment.Dungeon;
            } else if (x > 0 && z < 0 && e != Environment.Cavern) {
                e = Environment.Cavern;
            } else if (x < 0 && z < 0 && e != Environment.Closet) {
                e = Environment.Closet;
            } else if (x < 0 && z > 0 && e != Environment.Garage) {
                e = Environment.Garage;
                // You can configure custom environments too
                // e = new Environment(1, 1, 1, 1, 
                //        .9f, .5f, .751f, .0039f, .661f, .0137f);
            }
            audioRenderer.setEnvironment(e);
                System.out.println("Playing in environment "+ e.toString() +" at " + randomLoc);
            shotAudio.play();
        }
    }
}
