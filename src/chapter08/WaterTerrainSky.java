package chapter08;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 * Demo of advanced water effect with sky, terrain, waves, foam, tides,
 * underwater caustics, god beams, depth of field blur, and bloom.
 *
 * @author normenhansen
 */
public class WaterTerrainSky extends SimpleApplication {

  private Vector3f lightDir = new Vector3f(-2.9f, -1.2f, -5.8f);
  private WaterFilter water;
  private TerrainQuad terrain;
  private float time = 0.0f;
  private float waterHeight = 0.0f;
  private float initialWaterHeight = 0.8f;
  private Node reflectedScene;

  public static void main(String[] args) {
    WaterTerrainSky app = new WaterTerrainSky();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    setDisplayFps(false);
    setDisplayStatView(false);

    flyCam.setMoveSpeed(100);
    cam.setFrustumFar(3000);
    
    reflectedScene=new Node("Reflected Scene");
    rootNode.attachChild(reflectedScene);

    FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
    viewPort.addProcessor(fpp);
    // add water
    water = new WaterFilter(reflectedScene, lightDir);
    water.setLightColor(ColorRGBA.White);
    water.setWindDirection(Vector2f.UNIT_XY);
    water.setLightDirection(lightDir);
    water.setSunScale(3);
    water.setWaveScale(0.005f);
    water.setMaxAmplitude(5);
    water.setWaterTransparency(.1f);
    water.setWaterColor(new ColorRGBA(0.1f, 0.3f, 0.5f, 1.0f));
    water.setDeepWaterColor(new ColorRGBA(0.0f, 0.0f, 0.1f, 1.0f));
    water.setWaterHeight(initialWaterHeight);
    fpp.addFilter(water);

    
    initScene();
  }

  private void initScene() {
    // reflectedScene node groups everything that reflects in water, 
    // including terrain, sky˛ light, but not the water itself.
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(lightDir);
    sun.setColor(ColorRGBA.White.clone().multLocal(1.7f));
    reflectedScene.addLight(sun);

    AmbientLight ambientLight = new AmbientLight();
    reflectedScene.addLight(ambientLight);

    Spatial sky = SkyFactory.createSky(assetManager, 
            "Textures/Sky/Bright/BrightSky.dds", false);
    reflectedScene.attachChild(sky);

    reflectedScene.attachChild(createTerrain()); 

}

  private TerrainQuad createTerrain() {
    Texture heightMapImage = assetManager.loadTexture(
            "Textures/Terrain/heightmap.png");
    
    Material terrainMat = new Material(assetManager, 
            "Common/MatDefs/Terrain/TerrainLighting.j3md");
    terrainMat.setBoolean("useTriPlanarMapping", false);
    terrainMat.setBoolean("WardIso", true);    
    terrainMat.setTexture("AlphaMap", assetManager.loadTexture(
            "Textures/Terrain/alphamap.png"));
    
    Texture grass = assetManager.loadTexture(
            "Textures/Terrain/grass.jpg");
    grass.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("DiffuseMap_1", grass);
    terrainMat.setFloat("DiffuseMap_1_scale", 64);
    Texture normalMap1 = assetManager.loadTexture(
            "Textures/Terrain/grass_normal.jpg");
    normalMap1.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("NormalMap_1", normalMap1);
    
    Texture rock = assetManager.loadTexture(
            "Textures/Terrain/rock.png");
    rock.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("DiffuseMap", rock);
    terrainMat.setFloat("DiffuseMap_0_scale", 64);
    Texture normalMap0 = assetManager.loadTexture(
            "Textures/Terrain/rock_normal.png");
    normalMap0.setWrap(WrapMode.Repeat);
    terrainMat.setTexture("NormalMap", normalMap0);
    
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
    terrain.setMaterial(terrainMat);
    terrain.scale(4, 4, 4);
    terrain.move(0,-110,200);
    
    return terrain;
  }

  @Override
  public void simpleUpdate(float tpf) {
    // simulate tides by varying the height of the water plane
    time += tpf;
    waterHeight = (float) Math.cos(((time * 0.6f) % FastMath.TWO_PI)) * 1.5f;
    water.setWaterHeight(initialWaterHeight + waterHeight);
  }
}
