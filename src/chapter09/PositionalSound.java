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
 * This example shows how to play and listen to positional sounds. 
 * The positional sound is attached to a blue box (which is attached to rootnode). 
 * When the camera (you) moves around, you hear the sound emanating from the box.
 */
public class PositionalSound extends SimpleApplication {

  public static void main(String[] args) {
    PositionalSound app = new PositionalSound();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    flyCam.setMoveSpeed(40);

    // Create a node for the object the sound belongs to
    Node riverbedNode = new Node("Riverbed");
    riverbedNode.setLocalTranslation(Vector3f.ZERO);
    rootNode.attachChild(riverbedNode);

    // Attach a geometry to the scene node (just a blue cube)
    Box riverbedMesh = new Box(1, 1, 1);
    Geometry riverbedGeo = new Geometry("marks audio source", riverbedMesh);
    Material riverbedMat = new Material(assetManager,
            "Common/MatDefs/Misc/Unshaded.j3md");
    riverbedMat.setColor("Color", ColorRGBA.Blue);
    riverbedGeo.setMaterial(riverbedMat);
    riverbedNode.attachChild(riverbedGeo);
    
    // create and configure a positional sound
    AudioNode riverAudio = new AudioNode(assetManager,
            "Sounds/Environment/River.ogg");
    riverAudio.setPositional(true);   // Use 3D audio
    riverAudio.setRefDistance(10f);   // Distance of 50% volume
    riverAudio.setMaxDistance(1000f); // Distance where it stops going quieter
    riverAudio.setVolume(1);          // Default volume
    riverAudio.setLooping(true);      // Activate continuous playing
    // Attach the positional sound to its scene node
    riverbedNode.attachChild(riverAudio);
    // OK, start playing  
    riverAudio.play();               
  }

  @Override
  public void simpleUpdate(float tpf) {
    // keep the audio listener moving with the first-person camera!
    listener.setLocation(cam.getLocation());
    listener.setRotation(cam.getRotation());
  }
}
