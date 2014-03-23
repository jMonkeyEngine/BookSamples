package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 * This example demonstrates the Doppler effect in an orbiting "UFO".
 */
public class UfoDoppler extends SimpleApplication {

  UfoDoppler app;
  private AudioNode ufoAudio;
  private Geometry ufoGeo;
  private Node ufoNode = new Node("UFO");
  float x = 1, z = 1;
  private float rate = -0.25f;
  private float xDist = 2.5f;
  private float zDist = 2.5f;
  private float angle = FastMath.TWO_PI;

  public static void main(String[] args) {
    UfoDoppler app = new UfoDoppler();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    // position the camera
    cam.setLocation(new Vector3f(-5, 0, 5));
    // configure UFO sound
    ufoAudio = new AudioNode(assetManager, "Sounds/Effects/Beep.ogg");
    ufoAudio.setPositional(true);    // moving 3D sound
    ufoAudio.setLooping(true);       // keep playing
    ufoAudio.setReverbEnabled(true); // Doppler effect
    ufoAudio.setRefDistance(6);      // 50% volume fall-off at 6 wu distance
    ufoAudio.setMaxDistance(100);    // The UFO will be the most quiet at 100 wu away
    ufoAudio.play();
    ufoNode.attachChild(ufoAudio);
    // a simple UFO geometry marking the position of the sound
    Sphere sphere = new Sphere(32, 32, .5f);
    ufoGeo = new Geometry("ufo", sphere);
    Material mat = new Material(assetManager,
            "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.LightGray);
    ufoGeo.setMaterial(mat);
    ufoNode.attachChild(ufoGeo);
    // both sound and geometry are attached to one ufoNode
    rootNode.attachChild(ufoNode);
  }

  @Override
  public void simpleUpdate(float tpf) {
    // this formula calculates coordinates of the UFOs orbit
    float dx = (float) (Math.sin(angle) * xDist);
    float dz = (float) (-Math.cos(angle) * zDist);
    x += dx * tpf;
    z += dz * tpf;
    angle += tpf * rate;
    if (angle > FastMath.TWO_PI) {
      angle = FastMath.TWO_PI;
      rate = -rate;
    } else if (angle < -0) {
      angle = -0;
      rate = -rate;
    }
    // The UFO sound flies in circles and pulls the attached geometry with it
    ufoAudio.setVelocity(new Vector3f(dx, 0, dz));
    ufoNode.setLocalTranslation(x, 0, z);
    // print current location and speed
    //System.out.println("LOC: " + (int) x + ", " + (int) z + ", VEL: " + (int) dx + ", " + (int) dz);
    // keep the listener's ear at the camera position!
    listener.setLocation(cam.getLocation());
    listener.setRotation(cam.getRotation());
  }
}
