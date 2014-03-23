package chapter05;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/** 
 * How to give an object's surface a material and texture.
 * This class demonstrates opaque and transparent textures. 
 * Uses Phong illumination. */
public class TexturesOpaqueTransparent extends SimpleApplication {

    public static void main(String[] args) {
        TexturesOpaqueTransparent app = new TexturesOpaqueTransparent();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.White);
        flyCam.setMoveSpeed(50);

        /** A simple textured sphere */
        Sphere sphereMesh = new Sphere(16, 16, 1);
        Geometry sphereGeo = new Geometry("lit textured sphere", sphereMesh);
        Material sphereMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        sphereMat.setTexture("DiffuseMap",
                assetManager.loadTexture("Interface/Monkey.png"));
        sphereMat.setBoolean("UseMaterialColors", true);
        sphereMat.setColor("Diffuse", ColorRGBA.Gray);
        sphereMat.setColor("Ambient", ColorRGBA.Gray);
        // alpha test start
        sphereMat.getAdditionalRenderState().setAlphaTest(true);
        sphereMat.getAdditionalRenderState().setAlphaFallOff(.5f);
        sphereGeo.setQueueBucket(Bucket.Transparent);
        // alpha test end
        sphereGeo.setMaterial(sphereMat);
        sphereGeo.move(-2f, 0f, 0f);
        sphereGeo.rotate(FastMath.DEG_TO_RAD * -90, FastMath.DEG_TO_RAD * 120, 0f);
        rootNode.attachChild(sphereGeo);

        /** This material turns the box into a stained glass window. 
         * The texture has an alpha channel and is partially transparent. */
        Box windowMesh = new Box(new Vector3f(0f, 0f, 0f), 1f, 1.4f, 0.01f);
        Geometry windowGeo = new Geometry("stained glass window", windowMesh);
        Material windowMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        windowMat.setTexture("DiffuseMap",
                assetManager.loadTexture("Textures/mucha-window.png"));
        windowMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        windowGeo.setMaterial(windowMat);
        windowGeo.setQueueBucket(Bucket.Transparent);
        windowGeo.setMaterial(windowMat);
        windowGeo.move(1, 0, 0);
        rootNode.attachChild(windowGeo);

        /** A box with its material color "bleeding" through. The texture has 
         * an alpha channel and is partially transparent. */
        Cylinder logMesh = new Cylinder(32, 32, 1, 8, true);
        TangentBinormalGenerator.generate(logMesh);
        Geometry logGeo = new Geometry("Bleed-through color", logMesh);
        Material logMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        logMat.setTexture("DiffuseMap",
                assetManager.loadTexture("Textures/Bark/bark_diffuse.png"));
        logMat.setTexture("NormalMap",
                assetManager.loadTexture("Textures/Bark/bark_normal.png"));
        logMat.setBoolean("UseMaterialColors", true);
        logMat.setColor("Diffuse", ColorRGBA.Orange);
        logMat.setColor("Ambient", ColorRGBA.Gray);
        logMat.setBoolean("UseAlpha", true);
        logGeo.setMaterial(logMat);
        logGeo.move(0f, 0f, -2f);
        logGeo.rotate(0f, FastMath.DEG_TO_RAD * 90, 0f);
        rootNode.attachChild(logGeo);

        /** Must add a light to make the lit object visible! */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
    }
}