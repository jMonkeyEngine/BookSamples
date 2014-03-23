package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

/**
 * A better-looking terrain with Phong lighting and normal maps.
 * @author Brent Owens, zathras
 */
public class TerrainLighting extends SimpleApplication {

  TerrainQuad terrain;
  Material terrainMat;

  public static void main(String[] args) {
    TerrainLighting app = new TerrainLighting();
    app.start();
  }

  @Override
  public void simpleInitApp() {

    setDisplayFps(true);
    setDisplayStatView(false);
    flyCam.setMoveSpeed(100);
    cam.setLocation(new Vector3f(0, 512, 512));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

    createTerrain();

    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White);
    rootNode.addLight(ambient); 
    
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(-2.9f, -1.2f, -5.8f));
    sun.setColor(ColorRGBA.White.clone().multLocal(1.7f));
    rootNode.addLight(sun);
 
    Spatial sky = SkyFactory.createSky(assetManager, 
            "Textures/Sky/Bright/BrightSky.dds", false);
    rootNode.attachChild(sky);
    //viewPort.setBackgroundColor(ColorRGBA.Black);
  }

  private void createTerrain() {
    Texture heightMapImage = assetManager.loadTexture(
            "Textures/Terrain/heightmap.png");
    
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
    Texture normalMap0 = assetManager.loadTexture(
            "Textures/Terrain/grass_normal.jpg");
    normalMap0.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("NormalMap", normalMap0);
    
    Texture rock = assetManager.loadTexture(
            "Textures/Terrain/rock.png");
    rock.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("DiffuseMap_1", rock);
    terrainMat.setFloat("DiffuseMap_1_scale", 64);
    Texture normalMap1 = assetManager.loadTexture(
            "Textures/Terrain/rock_normal.png");
    normalMap1.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("NormalMap_1", normalMap1);
    
    Texture road = assetManager.loadTexture(
            "Textures/Terrain/road.png");
    road.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("DiffuseMap_2", road);
    terrainMat.setFloat("DiffuseMap_2_scale", 64);
    Texture normalMap2 = assetManager.loadTexture(
            "Textures/Terrain/road_normal.png");
    normalMap2.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("NormalMap_2", normalMap2);

    AbstractHeightMap heightmap = null;
    try {

      heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), .5f);
      heightmap.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
    heightmap.smooth(0.9f,3);
    
    terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
    TerrainLodControl lodControl = new TerrainLodControl(terrain, getCamera());
    terrain.addControl(lodControl);
    terrain.setMaterial(terrainMat);
    
    rootNode.attachChild(terrain);
  }

  @Override
  public void simpleUpdate(float tpf) { /** unused */ }
  
}
