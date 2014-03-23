package chapter05;

import com.jme3.app.SimpleApplication;
import com.jme3.light.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/** This is a demo of Multimapped Materials with bump maps and Phong illumination. 
 * You see a bumpy sphere on a bumpy brick wall. */
public class MaterialLighting extends SimpleApplication {

    public static void main(String[] args) {
        MaterialLighting app = new MaterialLighting();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(25);

        /** A ball with a smooth shiny pebbly surface */
        Sphere rockMesh = new Sphere(16, 16, 1);
        // better texture quality on spheres
        rockMesh.setTextureMode(Sphere.TextureMode.Projected);
        // Generate normal vector data for normal map!
        TangentBinormalGenerator.generate(rockMesh);
        Geometry rockGeo = new Geometry("Shiny rock", rockMesh);
        
        Material rockMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        rockMat.setTexture("DiffuseMap", assetManager.loadTexture(
                "Textures/Pebbles/Pebbles_diffuse.jpg"));
        rockMat.setTexture("NormalMap", assetManager.loadTexture(
                "Textures/Pebbles/Pebbles_normal.png"));

        rockMat.setFloat("Shininess", 56);    // [0,128]
        rockMat.setBoolean("UseMaterialColors", true);
        rockMat.setColor("Ambient", ColorRGBA.Gray);
        rockMat.setColor("Specular", ColorRGBA.White);
        rockMat.setColor("Diffuse", ColorRGBA.Gray);
        
        rockGeo.setMaterial(rockMat);
        rockGeo.move(0, 0, 0);
        rockGeo.rotate(FastMath.DEG_TO_RAD * 90, 0, 0);
        rootNode.attachChild(rockGeo);

        /** A wall with a rough bricky surface */
        
        Box wallMesh = new Box(2, 2, 2);
        // Generate normal vector data for normal map!
        TangentBinormalGenerator.generate(wallMesh);
        Geometry wallGeo = new Geometry("bumpy brick wall", wallMesh);
        Material wallMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        wallMat.setTexture("DiffuseMap", assetManager.loadTexture(
                "Textures/BrickWall/BrickWall_diffuse.jpg"));
        wallMat.setTexture("NormalMap", assetManager.loadTexture(
                "Textures/BrickWall/BrickWall_normal.jpg"));
        wallMat.setFloat("Shininess", 56);    // [0,128]
        wallMat.setBoolean("UseMaterialColors", true);
        wallMat.setColor("Ambient", ColorRGBA.Gray);
        wallMat.setColor("Specular", ColorRGBA.White);
        wallMat.setColor("Diffuse", ColorRGBA.Gray);
        wallGeo.setMaterial(wallMat);
        wallGeo.setLocalTranslation(0, -3, 0);   // Move it a bit
        rootNode.attachChild(wallGeo);

        /** Must add a light to make the lit object visible! */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, -1.1f, -1.2f));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        /** A white, spot light source. */
    PointLight lamp = new PointLight();
    lamp.setPosition(new Vector3f(-3,3,5));
    lamp.setColor(ColorRGBA.White);
    rootNode.addLight(lamp); 
        /** A white ambient light source. */
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White);
    rootNode.addLight(ambient); 
    }
}