package chapter04;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.MaterialList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.OgreMeshKey;
import com.jme3.util.SkyFactory;

/** 
 * This example loads a scene from a zip file 
 * hosted online or the local hard drive.
 */
public class LoadScene extends SimpleApplication {
  
  public static void main(String[] args) {
    LoadScene app = new LoadScene();
    app.start();
  }
  
  @Override
  public void simpleInitApp() {
    setDisplayFps(false);
    setDisplayStatView(false);
      
    viewPort.setBackgroundColor(ColorRGBA.LightGray);
    flyCam.setMoveSpeed(50f);
    
    /* light sources */
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1f, -1f, -1f));
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White.mult(3f));
    rootNode.addLight(ambient); 
    
    /* a sky as background */
    rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));

    /* TEST 1: load a zipped level from the project directory */
    assetManager.registerLocator("town.zip", ZipLocator.class);
    Spatial sceneModel = assetManager.loadModel("main.scene");
    sceneModel.setLocalScale(2f);
    sceneModel.move(0, -5f, 0);
    rootNode.attachChild(sceneModel);

    /* TEST 2: load a zipped level from an online source - wait for download! */
//    assetManager.registerLocator(
//    "http://jmonkeyengine.googlecode.com/files/quake3level.zip", 
//    HttpZipLocator.class);
//    MaterialList matList = (MaterialList) assetManager.loadAsset("Scene.material");
//    OgreMeshKey key = new OgreMeshKey("main.meshxml", matList);
//    Node gameLevel = (Node) assetManager.loadAsset(key);
//    gameLevel.setLocalScale(0.5f);
//    gameLevel.move(200,0,200); // position so camera is inside
//    rootNode.attachChild(gameLevel);

  }
}
