package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

/** This example shows a simple scene with a scene wide glow/bloom efect.  */
public class BloomGlow extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        viewPort.setBackgroundColor(new ColorRGBA(.5f, .7f, .9f, 1));
        flyCam.setMoveSpeed(50);

        initScene();

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White);
        rootNode.addLight(ambientLight);
        DirectionalLight sunLight = new DirectionalLight();
        sunLight.setDirection(new Vector3f(.3f, -0.5f, -0.5f));
        sunLight.setColor(ColorRGBA.White);
        rootNode.addLight(sunLight);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);

        BloomFilter bloom = new BloomFilter();
        fpp.addFilter(bloom);
    }

    public static void main(String[] args) {
        BloomGlow app = new BloomGlow();
        app.start();
    }

    private void initScene() {
        // Add some objects to the scene: A town and a teapot
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneGeo = assetManager.loadModel("main.scene");
        sceneGeo.setLocalScale(2f);
        sceneGeo.setLocalTranslation(0, -1, 0);
        rootNode.attachChild(sceneGeo);

        Geometry teapotGeo = (Geometry) assetManager.loadModel(
                "Models/Teapot/Teapot.j3o");
        Material teapotMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        teapotMat.setBoolean("UseMaterialColors", true);
        teapotMat.setColor("Diffuse", ColorRGBA.Pink);
        teapotGeo.setMaterial(teapotMat);
        teapotGeo.scale(3);
        teapotGeo.setLocalTranslation(32, 3, -24);
        rootNode.attachChild(teapotGeo);

        cam.setLocation(new Vector3f(0, 8f, 0));
        cam.lookAt(teapotGeo.getWorldTranslation(), Vector3f.UNIT_Y);
    }
}
