package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.util.SkyFactory;

/**
 * This demo shows how to load a terrain from a .j3o scene file.
 *
 * @author normenhansen
 */
public class TerrainFromScene extends SimpleApplication {

  TerrainQuad terrain;

  public static void main(String[] args) {
    TerrainFromScene app = new TerrainFromScene();
    app.start();
  }

  @Override
  public void simpleInitApp() {

    setDisplayFps(true);
    setDisplayStatView(false);

    Spatial terrainGeo = assetManager.loadModel("Scenes/myTerrain.j3o");
    rootNode.attachChild(terrainGeo);

    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(-0.39f, -0.32f, -0.74f).mult(1.5f));
    rootNode.addLight(sun);

    flyCam.setMoveSpeed(100);

    Spatial sky = SkyFactory.createSky(assetManager,
            "Textures/Sky/Bright/BrightSky.dds", false);
    rootNode.attachChild(sky);

    cam.setFrustumFar(4000);
  }

  @Override
  public void simpleUpdate(float tpf) {  }
}
