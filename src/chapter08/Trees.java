package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.HillHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

/**
 * A simple unshaded terrain.
 * @author Brent Owens, zathras
 */
public class Trees extends SimpleApplication {

  private TerrainQuad terrain;
  Material terrainMat;

  public static void main(String[] args) {
    Trees app = new Trees();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    setDisplayFps(true);
    setDisplayStatView(false);
    viewPort.setBackgroundColor(ColorRGBA.Blue);
    cam.setFrustumFar(4000);
    cam.setLocation(new Vector3f(0, 20, 30));
    flyCam.setMoveSpeed(50);

    DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
    dl.setColor(ColorRGBA.White);
    rootNode.addLight(dl);

    initTerrain();

    /* Load a tree model */
    Spatial treeGeo = assetManager.loadModel("Models/Tree/Tree.j3o");
    treeGeo.scale(5); // make tree bigger
    treeGeo.setQueueBucket(Bucket.Transparent); // leaves are transparent
    rootNode.attachChild(treeGeo);
    /* Place the tree at (0,?,-30) on the terrain. What is the y value? */
    Vector3f treeLoc = new Vector3f(0,0,-30);
    /* Don't use terrain.getLocalTranslation() to determine y! */
    //treeLoc.setY( terrain.getLocalTranslation().getY() ); // tree stuck in hill!
    /* Use terrain.getHeight() to determine y! */
    treeLoc.setY( terrain.getHeight(new Vector2f(treeLoc.x, treeLoc.z)) );
    treeGeo.setLocalTranslation(treeLoc);  
  }

  private void initTerrain() {
    // load sky
    rootNode.attachChild(SkyFactory.createSky(assetManager,
            "Textures/Sky/Bright/BrightSky.dds", false));
    // load terrain
    terrainMat = new Material(assetManager,
            "Common/MatDefs/Terrain/TerrainLighting.j3md");
    terrainMat.setBoolean("useTriPlanarMapping", false);
    terrainMat.setBoolean("WardIso", true);
    terrainMat.setTexture("AlphaMap", assetManager.loadTexture(
            "Textures/Terrain/alphamap.png"));

    Texture grass = assetManager.loadTexture(
            "Textures/Terrain/grass.jpg");
    grass.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("DiffuseMap", grass);
    terrainMat.setFloat("DiffuseMap_0_scale", 64);
    Texture normalMap = assetManager.loadTexture(
            "Textures/Terrain/grass_normal.jpg");
    normalMap.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("NormalMap", normalMap);
    terrainMat.setTexture("DiffuseMap_1", grass);
    terrainMat.setFloat("DiffuseMap_1_scale", 64);
    terrainMat.setTexture("NormalMap_1", normalMap);
    terrainMat.setTexture("DiffuseMap_2", grass);
    terrainMat.setFloat("DiffuseMap_2_scale", 64);
    terrainMat.setTexture("NormalMap_2", normalMap);
    // Generate a random height map
    AbstractHeightMap heightmap = null;
    try {
      heightmap = new HillHeightMap(1025, 1000, 50, 100, (byte) 3);
      heightmap.load();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Create the terrain, apply the material, attach to rootnode
    terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
    terrain.setMaterial(terrainMat);
    rootNode.attachChild(terrain);
  }
}
