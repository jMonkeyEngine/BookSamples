package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.HillHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 * A simple unshaded terrain.
 * @author Brent Owens, zathras
 */
public class TerrainSimple extends SimpleApplication {

  private TerrainQuad terrain;
  Material terrainMat;

  public static void main(String[] args) {
    TerrainSimple app = new TerrainSimple();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    setDisplayFps(true);
    setDisplayStatView(false);
    
    flyCam.setMoveSpeed(100);
    cam.setLocation(new Vector3f(0, 512, 512));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

    // Terrain material supports texture splatting
    terrainMat = new Material(assetManager,
            "Common/MatDefs/Terrain/Terrain.j3md");

    // Terrain AlphaMap for the splatting material
    terrainMat.setTexture("Alpha", assetManager.loadTexture(
            "Textures/Terrain/alphamap.png"));
    terrainMat.setBoolean("useTriPlanarMapping", false);

    // Grass texture for the splatting material
    Texture grass = assetManager.loadTexture(
            "Textures/Terrain/grass.jpg");
    grass.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("Tex1", grass);
    terrainMat.setFloat("Tex1Scale", 32);

    // Rock texture for the splatting material
    Texture rock = assetManager.loadTexture(
            "Textures/Terrain/rock.png");
    rock.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("Tex2", rock);
    terrainMat.setFloat("Tex2Scale", 16);

    // Road texture for the splatting material
    Texture road = assetManager.loadTexture(
            "Textures/Terrain/road.png");
    road.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("Tex3", road);
    terrainMat.setFloat("Tex3Scale", 16);

    // Heightmap image on which we base the terrain
    Texture heightMapImage = assetManager.loadTexture(
            "Textures/Terrain/heightmap.png");
    // Create the heightmap object from the heightmap image 
    AbstractHeightMap heightmap = null;
    try {
      // heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), .5f);
      heightmap = new HillHeightMap(1025, 500, 50, 100, (byte) 3); 
      heightmap.load();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Create the terrain, apply the material, attach to rootnode
    terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
    terrain.setMaterial(terrainMat);
    
     TerrainLodControl lodControl = new TerrainLodControl(terrain, getCamera());
    terrain.addControl(lodControl);
    
    rootNode.attachChild(terrain);
  }
}
