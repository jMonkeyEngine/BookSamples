package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 * A demo of a town with sunlight coming down in visible beams ("god beams")
 */
public class SunLight extends SimpleApplication {

  private FilterPostProcessor fpp;
  private LightScatteringFilter sunLightFilter;
  // global vector where the sun is on the skybox
  private Vector3f lightDir = new Vector3f(-0.39f, -0.32f, -0.74f);

  public static void main(String[] args) {
    SunLight app = new SunLight();
    app.start();
  }

  public void simpleInitApp() {
    flyCam.setMoveSpeed(30f);

    // make light shine from where sun is on skybox
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(lightDir);
    sun.setColor(ColorRGBA.White.clone().multLocal(2));
    rootNode.addLight(sun);
    
    // init the filter postprocessor
    fpp = new FilterPostProcessor(assetManager);
    viewPort.addProcessor(fpp);

    // make light beams appear from where sun is on skybox
    sunLightFilter = new LightScatteringFilter(lightDir.mult(-3000));
    fpp.addFilter(sunLightFilter);
    
    initScene();
    
    // look into sun :-)
    cam.lookAtDirection(lightDir.negate(), Vector3f.UNIT_Y);
  }

  private void initScene() {
    // load sky
    rootNode.attachChild(SkyFactory.createSky(assetManager,
            "Textures/Sky/Bright/BrightSky.dds", false));
    // load scene content: a town
    assetManager.registerLocator("town.zip", ZipLocator.class);
    Spatial sceneGeo = assetManager.loadModel("main.scene");
    sceneGeo.setLocalScale(2f);
    sceneGeo.setLocalTranslation(0, -5f, 0);
    rootNode.attachChild(sceneGeo);
  }
}
