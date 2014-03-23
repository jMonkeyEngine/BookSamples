package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * This example demonstrates directional sound that it only audible within a
 * "sound cone". The sound cone is defined by a direction and and inner angle
 * (loudest) and outer angle (more quite). Directional sound is not audible
 * outside the "sound cone".
 *
 * @author zathras
 */
public class DirectionalSound extends SimpleApplication {

    public static void main(String[] args) {
        DirectionalSound app = new DirectionalSound();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50f);
        // sound source parent node
        Node beachNode = new Node("sound source node");
        beachNode.setLocalTranslation(1, 1, 1);
        rootNode.attachChild(beachNode);

        //just a blue box floating in space
        Box beachMesh = new Box(Vector3f.UNIT_XYZ, 1, 1, 1);
        Geometry beachGeo = new Geometry("Sound source visuals", beachMesh);
        Material beachMat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        beachMat.setColor("Color", ColorRGBA.Blue);
        beachGeo.setMaterial(beachMat);
        beachNode.attachChild(beachGeo);
        
        // create directional sound 
        AudioNode beachAudio = new AudioNode(assetManager,
                "Sounds/Environment/Ocean Waves.ogg");
        beachAudio.setLooping(true);
        beachAudio.setPositional(true);  // directional is a type of positional
        beachAudio.setDirectional(true);
        beachAudio.setInnerAngle(50);    // the inner sound cone
        beachAudio.setOuterAngle(120);   // the outer sound cone
        beachAudio.setDirection(new Vector3f(0, 0, 1)); // direction of sound cone

        // attach the sound to its parent node
        beachNode.attachChild(beachAudio);
        // start playing
        beachAudio.play();
    }

    @Override
    public void simpleUpdate(float tpf) {
        // keep the audio listener moving with the camera
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
    }
}
