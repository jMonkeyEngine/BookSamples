package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/** This sample demonstrates adding drop shadows to an existing scene. */
public class ShadowDirectional extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50);
        viewPort.setBackgroundColor(new ColorRGBA(.5f, .7f, .9f, 1));
        setDisplayStatView(false);

        initScene();

        /* directional light source for shadows */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(.3f, -0.5f, -0.5f));
        rootNode.addLight(sun);

        /* two ways to cast drop shadows */
        DirectionalLightShadowRenderer dlsr = 
                new DirectionalLightShadowRenderer(assetManager, 1024, 2);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

        DirectionalLightShadowFilter dlsf = 
                new DirectionalLightShadowFilter(assetManager, 1024, 2);
        dlsf.setLight(sun);
        dlsf.setEnabled(false); // try true or false
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
    }

    public static void main(String[] args) {
        ShadowDirectional app = new ShadowDirectional();
        app.start();
    }

    private void initScene() {
        // Add some objects to the scene: A town and a teapot
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneGeo = assetManager.loadModel("main.scene");
        sceneGeo.setLocalScale(2f);
        sceneGeo.setLocalTranslation(0, -1, 0);
        rootNode.attachChild(sceneGeo);

        Geometry teapotGeo = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.j3o");
        Material teapotMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        teapotMat.setBoolean("UseMaterialColors", true);
        teapotMat.setColor("Diffuse", ColorRGBA.Pink);
        teapotGeo.setMaterial(teapotMat);
        teapotGeo.scale(3);
        teapotGeo.setLocalTranslation(32, 3, -24);
        rootNode.attachChild(teapotGeo);

        rootNode.setShadowMode(ShadowMode.Off); // reset first
        sceneGeo.setShadowMode(ShadowMode.CastAndReceive);
        teapotGeo.setShadowMode(ShadowMode.CastAndReceive);

        cam.setLocation(new Vector3f(0, 8f, 0));
        cam.lookAt(teapotGeo.getWorldTranslation(), Vector3f.UNIT_Y);
    }
}
