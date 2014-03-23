package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.shadow.BasicShadowRenderer;

/**
 * The demo shows a simple way of adding dynamic shadows to a scene.
 * @deprecated 
 */
public class ShadowSimple extends SimpleApplication {

    private BasicShadowRenderer bsr;
    private Vector3f lightDir = new Vector3f(.3f, -0.5f, -0.5f);

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(new ColorRGBA(.5f, .7f, .9f, 1));
        setDisplayStatView(false);
        flyCam.setMoveSpeed(30f);

        bsr = new BasicShadowRenderer(assetManager, 1024);
        bsr.setDirection(lightDir);
        viewPort.addProcessor(bsr);

        DirectionalLight sunLight = new DirectionalLight();
        sunLight.setDirection(lightDir);
        rootNode.addLight(sunLight);

        initScene();
    }

    /** Add some objects to the scene: A town  and a teapot*/
    private void initScene() {
        rootNode.setShadowMode(ShadowMode.Off); // reset first

        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneGeo = assetManager.loadModel("main.scene");
        sceneGeo.setLocalScale(2f);
        sceneGeo.setLocalTranslation(0, -1, 0);
        rootNode.attachChild(sceneGeo);
        sceneGeo.setShadowMode(ShadowMode.CastAndReceive);

        Geometry teapotGeo = (Geometry) assetManager.loadModel(
                "Models/Teapot/Teapot.j3o");
        teapotGeo.scale(3);
        teapotGeo.setLocalTranslation(32, 3, -24);
        rootNode.attachChild(teapotGeo);
        teapotGeo.setShadowMode(ShadowMode.Cast);

        /** configure some game properties depending on the scene*/
        cam.setLocation(new Vector3f(0f, 15f, -30f));
        cam.lookAt(teapotGeo.getLocalTranslation(), Vector3f.UNIT_Y);
    }

    public static void main(String[] args) {
        ShadowSimple app = new ShadowSimple();
        app.start();

    }
}
