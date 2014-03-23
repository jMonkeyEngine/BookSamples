package chapter09; // works

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Environment;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * In this audio demo, use the WASD keys and mouse to move around the origin.
 * You hear what the same foot steps sound like in four different environments.
 * * @author zathras
 */
public class AudioEnvironments extends SimpleApplication {

    private AudioNode stepsAudio;
    private Environment env = Environment.AcousticLab;
    private float[] eax = new float[]{ 26, 19.6f, 0.940f, -1000, -200, -700, 5.04f, 1.12f, 0.56f, -1230, 0.020f, 0f, 0f, 0f, 200, 0.029f, 0f, 0f, 0f, 0.250f, 0.080f, 2.742f, 0.050f, -2f, 5000f, 250f, 0f, 0x3f} ;
    private Environment env2 = new Environment(eax);

    public static void main(String[] args) {
        AudioEnvironments app = new AudioEnvironments();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initScene();
        flyCam.setMoveSpeed(50f);
        // initialize the sound node
        audioRenderer.setEnvironment(env);
        //audioRenderer.setEnvironment(new Environment(eax));
        stepsAudio = new AudioNode(assetManager, "Sounds/Effects/Bang.wav");
        stepsAudio.setVolume(1);
        stepsAudio.setLooping(true);
        stepsAudio.play();
    }

    @Override
    public void simpleUpdate(float tpf) {
        // Move the listener with the a camera - for 3D audio.
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        stepsAudio.setLocalTranslation(cam.getLocation());
        // repeat the audio inside possibly changed environment
        float x = cam.getLocation().getX();
        float z = cam.getLocation().getZ();
        if (x > 0 && z > 0 && env != Environment.Dungeon) {
            System.out.println("Playing in environment Dungeon");
            env = Environment.Dungeon;
        } else if (x > 0 && z < 0 && env != Environment.Cavern) {
            System.out.println("Playing in environment Cavern");
            env = Environment.Cavern;
        } else if (x < 0 && z < 0 && env != Environment.Closet) {
            System.out.println("Playing in environment Closet");
            env = Environment.Closet;
        } else if (x < 0 && z > 0 && env != Environment.Garage) {
            System.out.println("Playing in environment Garage");
            env = Environment.Garage;
        }
        audioRenderer.setEnvironment(env);

    }

    /**
     * This simple scene is just a blue box floating in space
     */
    private void initScene() {
        Box boxMesh = new Box(1, 1, 1);
        Geometry boxGeo = new Geometry("Player", boxMesh);
        Material boxMat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Blue);
        boxGeo.setMaterial(boxMat);
        rootNode.attachChild(boxGeo);
    }
}
