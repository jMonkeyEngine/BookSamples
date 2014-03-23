package chapter05;

import com.jme3.app.SimpleApplication;
import com.jme3.light.*;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.TangentBinormalGenerator;

/** This demo shows transparent materials - a simple glass sphere. */
public class GlassMaterial extends SimpleApplication {

    public static void main(String[] args) {
        GlassMaterial app = new GlassMaterial();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50f);
        
        /** A floor with a rough bumpy surface */
        Box floorMesh = new Box(10, 1, 10);
        TangentBinormalGenerator.generate(floorMesh);
        Geometry floorGeo = new Geometry("floor", floorMesh);

        Material floorMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setTexture("DiffuseMap", assetManager.loadTexture(
                "Textures/BrickWall/BrickWall_diffuse.jpg"));
        floorMat.setTexture("NormalMap", assetManager.loadTexture(
                "Textures/BrickWall/BrickWall_normal.jpg"));
        floorGeo.setMaterial(floorMat);
        floorMat.getTextureParam("DiffuseMap").
                getTextureValue().setWrap(WrapMode.Repeat);
        floorGeo.setLocalTranslation(0, -4f, 0);   // Move it a bit
        rootNode.attachChild(floorGeo);

        Sphere glassMesh = new Sphere(32, 32, 2.5f);
        // generate normal vector data for normal maps
        TangentBinormalGenerator.generate(glassMesh);

        Geometry glassGeo = new Geometry("normal sphere", glassMesh);
        Material glassMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        glassMat.setBoolean("UseMaterialColors", true);
        glassMat.setColor("Ambient", new ColorRGBA(0, 1, 1, .75f));
        glassMat.setColor("Diffuse", new ColorRGBA(0, 1, 1, .75f));
        glassMat.setColor("Specular", ColorRGBA.White);
        glassMat.setFloat("Shininess", 128f);    // [0,128]
        glassMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        glassMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        glassGeo.setQueueBucket(Bucket.Transparent);
        glassGeo.setMaterial(glassMat);
        glassGeo.rotate(FastMath.DEG_TO_RAD * 90, 0, 0);
        rootNode.attachChild(glassGeo);

        /** Must add a light to make the lit object visible! */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(.3f, -0.5f, -0.5f));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

    }
}