package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

/**
 * A town with fog.
 */
public class Fog extends SimpleApplication {

  private FilterPostProcessor fpp;
  private FogFilter fogFilter;

  public static void main(String[] args) {
    Fog app = new Fog();
    app.start();
  }

  public void simpleInitApp() {
    // activate fog
    fpp = new FilterPostProcessor(assetManager);
    fogFilter = new FogFilter();
    fogFilter.setFogDistance(155);
    fogFilter.setFogDensity(2.0f);
    //fogFilter.setFogColor(ColorRGBA.Gray);
    fpp.addFilter(fogFilter);
    viewPort.addProcessor(fpp);

    DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(1f, -1f, -1f));
    rootNode.addLight(dl);
    initScene();
  }

  private void initScene() {
    flyCam.setMoveSpeed(30f);
    viewPort.setBackgroundColor(ColorRGBA.Cyan);

    // Add some objects to the scene: A town
    assetManager.registerLocator("town.zip", ZipLocator.class);
    Spatial sceneGeo = assetManager.loadModel("main.scene");
    sceneGeo.setLocalScale(2f);
    sceneGeo.setLocalTranslation(0, -1, 0);
    rootNode.attachChild(sceneGeo);

    // Add some objects to the scene: a tea pot
    Geometry teaGeo = (Geometry) assetManager.loadModel(
            "Models/Teapot/Teapot.j3o");
    Material mat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Diffuse", ColorRGBA.Pink);
    teaGeo.setMaterial(mat);

    teaGeo.scale(3);
    teaGeo.setLocalTranslation(32, 3, -24);
    rootNode.attachChild(teaGeo);
    cam.lookAt(teaGeo.getLocalTranslation(), Vector3f.UNIT_Y);
  }
}
