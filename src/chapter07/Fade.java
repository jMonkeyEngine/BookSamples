package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/** Press 1 or 0 to fade the scene in or out */
public class Fade extends SimpleApplication { // TODO

  private FilterPostProcessor fpp;
  private FadeFilter fade;

  public static void main(String[] args) {
    Fade app = new Fade();
    app.start();
  }

  public void simpleInitApp() {
    fpp = new FilterPostProcessor(assetManager);
    fade = new FadeFilter(2);
    fpp.addFilter(fade);
    viewPort.addProcessor(fpp);

    DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(1f, -1f, -1f));
    rootNode.addLight(dl);
    initScene();
    initKeys();
  }

  private void initScene() {
    /**
     * Add some objects to the scene: A town
     */
    assetManager.registerLocator("town.zip", ZipLocator.class);
    Spatial sceneGeo = assetManager.loadModel("main.scene");
    sceneGeo.setLocalScale(2f);
    sceneGeo.setLocalTranslation(0, -1, 0);
    rootNode.attachChild(sceneGeo);

    /**
     * Add some objects to the scene: a tea pot
     */
    Geometry teapotGeo = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.j3o");
    Material teapotMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    teapotMat.setBoolean("UseMaterialColors", true);
    teapotMat.setColor("Diffuse", ColorRGBA.Pink);
    teapotGeo.setMaterial(teapotMat);

    teapotGeo.scale(3);
    teapotGeo.setLocalTranslation(32, 3, -24);
    rootNode.attachChild(teapotGeo);

    /**
     * configure some game properties depending on the scene
     */
    flyCam.setMoveSpeed(30f);
    cam.lookAt(teapotGeo.getLocalTranslation(), Vector3f.UNIT_Y);
  }

  private void initKeys() {
    inputManager.addMapping("fade in", new KeyTrigger(KeyInput.KEY_1));
    inputManager.addMapping("fade out", new KeyTrigger(KeyInput.KEY_0));
    inputManager.addListener(actionListener, new String[]{"fade in", "fade out"});
  }
  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean isPressed, float tpf) {
      if (name.equals("fade in") && !isPressed) {
        fade.fadeIn();
      } else if (name.equals("fade out") && !isPressed) {
        fade.fadeOut();
      }
    }
  };
}
