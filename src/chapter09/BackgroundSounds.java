package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * This demo shows how you play ambient and background noises in a loop.
 */
public class BackgroundSounds extends SimpleApplication {

  public static void main(String[] args) {
    BackgroundSounds app = new BackgroundSounds();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    flyCam.setMoveSpeed(40);
    initScene();

    // create and configure a sound
    AudioNode audio_nature = new AudioNode(assetManager,
            "Sounds/Environment/River.ogg");
    audio_nature.setVolume(5);
    audio_nature.setLooping(true); // activate continous play mode
    audio_nature.play();           // start playing continuously!  
  }

  /**
   * just a blue box floating in space
   */
  private void initScene() {
    Box box1 = new Box(1, 1, 1);
    Geometry player = new Geometry("Player", box1);
    Material mat1 = new Material(assetManager,
            "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.Blue);
    player.setMaterial(mat1);
    rootNode.attachChild(player);
  }

  @Override
  public void simpleUpdate(float tpf) {
  }
}