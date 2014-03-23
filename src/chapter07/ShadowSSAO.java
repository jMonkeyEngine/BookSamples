package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

/** This example demos the Screen-Space Ambient Occlusion effect 
 * that makes outdoor scenes look like night or overcast, 
 * and indoor scenes dark and gloomy. */
public class ShadowSSAO extends SimpleApplication {

    private FilterPostProcessor fpp;

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(.5f, .7f, .9f, 1));
        setDisplayStatView(false);
        flyCam.setMoveSpeed(50);

        initScene();

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White);
        rootNode.addLight(ambientLight);

        fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);
        SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.93f, 0.33f, 0.60f);
        fpp.addFilter(ssaoFilter);
    }

    public static void main(String[] args) {
        ShadowSSAO app = new ShadowSSAO();
        app.start();
    }

    private void initScene() {
        // Add some objects to the scene: A town
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneGeo = assetManager.loadModel("main.scene");
        sceneGeo.setLocalScale(2f);
        sceneGeo.setLocalTranslation(0, -1, 0);
        rootNode.attachChild(sceneGeo);

        // Add some objects to the scene: a tea pot
        Geometry teapotGeo = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.j3o");
        Material teapotMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        teapotMat.setBoolean("UseMaterialColors", true);
        teapotMat.setColor("Diffuse", ColorRGBA.Blue);
        teapotMat.setColor("Ambient", ColorRGBA.Blue);
        teapotGeo.setMaterial(teapotMat);
        teapotGeo.scale(3);
        teapotGeo.setLocalTranslation(32, 3, -24);
        rootNode.attachChild(teapotGeo);

        cam.setLocation(new Vector3f(0, 8f, 0));
        cam.lookAt(teapotGeo.getWorldTranslation(), Vector3f.UNIT_Y);
    }
}
