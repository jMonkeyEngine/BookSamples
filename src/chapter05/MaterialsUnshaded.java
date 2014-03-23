package chapter05;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/** 
 * How to give an object's surface a material and texture.
 * This class demonstrates opaque and transparent textures, 
 * No Phong illumination -- just unshaded with one lightmap. */
public class MaterialsUnshaded extends SimpleApplication {

    public static void main(String[] args) {
        MaterialsUnshaded app = new MaterialsUnshaded();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50);
        viewPort.setBackgroundColor(ColorRGBA.White);

        /** A simple textured sphere */
        Sphere sphereMesh = new Sphere(16, 16, 1);
        Geometry sphereGeo = new Geometry("Unshaded textured sphere", sphereMesh);
        Material sphereMat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        sphereMat.setTexture("ColorMap",
                assetManager.loadTexture("Interface/Monkey.png"));
        sphereMat.setTexture("LightMap",
                assetManager.loadTexture("Interface/Monkey_light.png"));
        sphereGeo.setMaterial(sphereMat);
        sphereGeo.move(-2f, 0f, 0f);
        sphereGeo.rotate(FastMath.DEG_TO_RAD * -90, FastMath.DEG_TO_RAD * 120, 0f);
        rootNode.attachChild(sphereGeo);

        /** This material turns the box into a stained glass window. 
         * The texture uses an alpha channel and is partially transparent. */
        Box windowMesh = new Box(1f, 1.4f, 0.01f);
        Geometry windowGeo = new Geometry("a transparent window frame", windowMesh);
        windowGeo.move(2f, 0f, 0f);
        Material windowMat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        windowMat.setTexture("ColorMap",
                assetManager.loadTexture("Textures/mucha-window.png"));
        windowGeo.setMaterial(windowMat);
        windowMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        windowGeo.setQueueBucket(Bucket.Translucent);
        // windowMat.getAdditionalRenderState().setDepthTest(true);
        rootNode.attachChild(windowGeo);

        /** A cylinder with its material color "bleeding" through the texture. 
         * The texture uses an alpha channel and is partially transparent. */
        Cylinder logMesh = new Cylinder(32, 32, 1, 8, true);
        //logMesh.scaleTextureCoordinates(new Vector2f(5,1));
        Geometry logGeo = new Geometry("A textured log with brown color", logMesh);
        logGeo.move(0f, 0f, -2f);
        logGeo.rotate(0f, FastMath.DEG_TO_RAD * 90, 0f);
        Material logMat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        Texture logTex = assetManager.loadTexture("Textures/Bark/bark_diffuse.png");
        logMat.setTexture("ColorMap", logTex);
        logMat.setColor("Color", ColorRGBA.Orange);
        logGeo.setMaterial(logMat);
        rootNode.attachChild(logGeo);
    }
}