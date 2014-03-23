package chapter05;

import com.jme3.app.SimpleApplication;
import com.jme3.light.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

/** Three colored spheres with increasing shininess.
 * A non-shiny surface appears rough, a very shiny surface appears smooth.
 */
public class MaterialColorShiny extends SimpleApplication {

  public static void main(String[] args) {
    MaterialColorShiny app = new MaterialColorShiny();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    
    Sphere sphereMesh = new Sphere(32,32, 1f);
    
    Geometry spherePlainGeo = new Geometry("rough sphere", sphereMesh);
    Material spherePlainMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    spherePlainMat.setFloat("Shininess", 0f); // [1,128]
    spherePlainMat.setBoolean("UseMaterialColors",true);
    spherePlainMat.setColor("Ambient", ColorRGBA.Black );
    spherePlainMat.setColor("Diffuse", ColorRGBA.Cyan );
    spherePlainMat.setColor("Specular", ColorRGBA.White );
    spherePlainGeo.setMaterial(spherePlainMat);
    spherePlainGeo.move(-2.5f, 0, 0);
    rootNode.attachChild(spherePlainGeo); 
    
    Geometry sphereShinyGeo = new Geometry("normal sphere", sphereMesh);
    Material sphereShinyMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    sphereShinyMat.setBoolean("UseMaterialColors",true);
    sphereShinyMat.setColor("Ambient", ColorRGBA.Black );
    sphereShinyMat.setColor("Diffuse", ColorRGBA.Cyan );
    sphereShinyMat.setColor("Specular", ColorRGBA.White );
    sphereShinyMat.setFloat("Shininess", 4f); // [1,128]
    sphereShinyGeo.setMaterial(sphereShinyMat);
    rootNode.attachChild(sphereShinyGeo); 
    
    Geometry sphereVeryShinyGeo = new Geometry("Smooth sphere", sphereMesh);
    Material sphereVeryShinyMat = new Material(assetManager, 
            "Common/MatDefs/Light/Lighting.j3md");
    sphereVeryShinyMat.setBoolean("UseMaterialColors",true);
    sphereVeryShinyMat.setColor("Ambient", ColorRGBA.Black );
    sphereVeryShinyMat.setColor("Diffuse", ColorRGBA.Cyan );
    sphereVeryShinyMat.setColor("Specular", ColorRGBA.White );
    sphereVeryShinyMat.setFloat("Shininess", 100f); // [1,128]
    sphereVeryShinyGeo.setMaterial(sphereVeryShinyMat);
    sphereVeryShinyGeo.move(2.5f, 0, 0);
    rootNode.attachChild(sphereVeryShinyGeo); 
    
    /** Must add a light to make the lit object visible! */
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1, 0, -2));
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);
   
  }
}