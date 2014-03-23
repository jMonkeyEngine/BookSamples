package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer.CompareMode;
import com.jme3.shadow.PssmShadowRenderer.FilterMode;

/** This sample demonstrates advanced drop shadows using 
 * parallel-split shadow maps. DEPRECATED; see */
public class ShadowPSSM extends SimpleApplication {

    private Vector3f lightDir = new Vector3f(.3f, -0.5f, -0.5f);
    private PssmShadowRenderer pssm;

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50);
        viewPort.setBackgroundColor(new ColorRGBA(.5f, .7f, .9f, 1));
        setDisplayStatView(false);

        initScene();

        DirectionalLight sunLight = new DirectionalLight();
        sunLight.setDirection(lightDir);
        rootNode.addLight(sunLight);

        pssm = new PssmShadowRenderer(assetManager, 1024, 3);
        pssm.setDirection(lightDir);
        pssm.setCompareMode(CompareMode.Software);
        pssm.setFilterMode(FilterMode.Bilinear);
        //pssm.setLambda(0.65f);
        //pssm.setShadowIntensity(0.7f);
        //pssm.displayDebug();
        viewPort.addProcessor(pssm);
    }

    public static void main(String[] args) {
        ShadowPSSM app = new ShadowPSSM();
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
