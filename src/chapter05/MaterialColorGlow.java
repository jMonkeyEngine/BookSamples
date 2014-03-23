package chapter05;

import com.jme3.app.SimpleApplication;
import com.jme3.light.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

/** One normal sphere next to two glowing spheres.
 * One has a black-and-white chequered glow map, 
 * the other has a colored random noise glow map. */
public class MaterialColorGlow extends SimpleApplication {

  public static void main(String[] args) {
    MaterialColorGlow app = new MaterialColorGlow();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    
    FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
    BloomFilter bloom= new BloomFilter(BloomFilter.GlowMode.Objects);
    fpp.addFilter(bloom);
    viewPort.addProcessor(fpp);
    
    Sphere sphereMesh = new Sphere(32,32, 1f);
    
    Geometry spherePlainGeo = new Geometry("normal sphere", sphereMesh);
    Material spherePlainMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    spherePlainMat.setBoolean("UseMaterialColors",true);
    spherePlainMat.setColor("Ambient", ColorRGBA.Cyan );
    spherePlainMat.setColor("Diffuse", ColorRGBA.Cyan );
    spherePlainGeo.setMaterial(spherePlainMat);
    spherePlainGeo.move(-2.5f, 0, 0);
    rootNode.attachChild(spherePlainGeo); 
    
    Geometry sphereCheqGeo = new Geometry("black and white chequered", sphereMesh);
    Material sphereCheqMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    sphereCheqMat.setBoolean("UseMaterialColors",true);
    sphereCheqMat.setColor("Diffuse", ColorRGBA.Cyan );
    sphereCheqMat.setColor("Ambient", ColorRGBA.Cyan );
    sphereCheqMat.setTexture("GlowMap", assetManager.loadTexture("Textures/bloom-glow2.png"));
    sphereCheqMat.setColor("GlowColor", ColorRGBA.Red );
    sphereCheqGeo.setMaterial(sphereCheqMat);
    rootNode.attachChild(sphereCheqGeo); 
    
    Geometry sphereNoiseGeo = new Geometry("random color noise", sphereMesh);
    Material sphereNoiseMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    sphereNoiseMat.setBoolean("UseMaterialColors",true);
    sphereNoiseMat.setColor("Diffuse", ColorRGBA.Cyan );
    sphereNoiseMat.setColor("Ambient", ColorRGBA.Cyan );
    sphereNoiseMat.setTexture("GlowMap", assetManager.
            loadTexture("Textures/bloom-glow.png"));
    sphereNoiseMat.setColor("GlowColor", ColorRGBA.White );
    sphereNoiseGeo.setMaterial(sphereNoiseMat);
    sphereNoiseGeo.move(2.5f, 0, 0);
    rootNode.attachChild(sphereNoiseGeo); 
    
    /** Must add a light to make the lit object visible! */
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);
    
  }
}