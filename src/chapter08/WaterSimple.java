package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;

/**
 * A demo of the basic water simulation, with reflections but no underwater effects.
 * @author normenhansen
 */
public class WaterSimple extends SimpleApplication {
  private Node reflectedScene;
  private Vector3f lightDir = new Vector3f(-0.39f, -0.32f, -0.74f);

  public static void main(String[] args) {
    WaterSimple app = new WaterSimple();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    flyCam.setMoveSpeed(50f);
    cam.setLocation(new Vector3f(10,5,10));
    cam.lookAt(new Vector3f(-0.5f,-0.4f,-0.7f),Vector3f.UNIT_Y);
    
    initLight();
    initScene();
    initWater();
  }

  private void initScene() {
    // mainscene has everything that reflects in water, 
    // including sky and light, but not the water itself.
    reflectedScene = new Node("Scene");
    rootNode.attachChild(reflectedScene);
    // Add sky
    reflectedScene.attachChild(SkyFactory.createSky(assetManager,
            "Textures/Sky/Bright/BrightSky.dds", false));
    // Add some scene content, e.g. an unshaded box
    Material boxMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    boxMat.setTexture("DiffuseMap", 
            assetManager.loadTexture("Interface/Monkey.png"));
    Box boxMesh = new Box(2, 2, 2);
    Geometry boxGeo = new Geometry("Box", boxMesh);
    boxGeo.setMaterial(boxMat);
    reflectedScene.attachChild(boxGeo);
  }

  private void initWater() {
    // create water post-processor
    SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
    waterProcessor.setReflectionScene(reflectedScene); // !
    waterProcessor.setLightPosition(lightDir.mult(-3000));
    viewPort.addProcessor(waterProcessor);
    // create water geometry with material
    Spatial waterPlane = waterProcessor.createWaterGeometry(100, 100);
    waterPlane.setMaterial(waterProcessor.getMaterial());
    waterPlane.setLocalTranslation(-50, 0, 50);
    rootNode.attachChild(waterPlane);
  }

  private void initLight() {
    DirectionalLight dl = new DirectionalLight();
    dl.setDirection(lightDir);
    rootNode.addLight(dl);
  }

  @Override
  public void simpleUpdate(float tpf) {}
  
}
