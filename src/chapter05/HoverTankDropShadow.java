package chapter05;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer; 
import com.jme3.texture.Texture.WrapMode;

/**
 * This demo shows loading a tank model with a multimapped material 
 * using UV textures and Phong illumination.
 * You also see a floor geometry with multimapped material using seamless tiled textures.
 * This example includes a directional light source casting drop shadows.
 */
public class HoverTankDropShadow extends SimpleApplication {

    private DirectionalLight sun;

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        
        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(4,10,18));
        cam.setRotation(new Quaternion(-0.03f, 0.95f, -0.27f, -0.10f));
        
        /** A HoverTank model using a .j3m material. */
        Node tank = (Node) assetManager.loadModel(
                "Models/HoverTank/Tank.j3o");
        rootNode.attachChild(tank);
        tank.setShadowMode(ShadowMode.CastAndReceive);

        /** A floor with a seemless tiled, multimapped texture */
        Box floorMesh = new Box(new Vector3f(-20, -1, -20), new Vector3f(20, -2, 20));
        floorMesh.scaleTextureCoordinates(new Vector2f(8, 8));
        Geometry floorGeo = new Geometry("floor", floorMesh);
        Material floorMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setTexture("DiffuseMap", assetManager.loadTexture(
                "Textures/BrickWall/BrickWall_diffuse.jpg"));
        floorMat.getTextureParam("DiffuseMap").
                getTextureValue().setWrap(WrapMode.Repeat);
        floorMat.setTexture("NormalMap", assetManager.loadTexture(
                "Textures/BrickWall/BrickWall_normal.jpg"));
        floorMat.getTextureParam("NormalMap").
                getTextureValue().setWrap(WrapMode.Repeat);
        floorGeo.setMaterial(floorMat);
        floorGeo.setShadowMode(ShadowMode.Receive);
        rootNode.attachChild(floorGeo);

        /** Overall brightness*/
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
        /* directional light source for shadows */
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(cam.getDirection());
        rootNode.addLight(sun);


        /* Drop shadows */
        final int SHADOWMAP_SIZE=1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true); // try true or false
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);

        /* Glow effect */
        BloomFilter bf = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bf);
        viewPort.addProcessor(fpp);

    }

    @Override
    public void simpleUpdate(float tpf) {    }

    public static void main(String[] args) {
        HoverTankDropShadow app = new HoverTankDropShadow();
        app.start();

    }
}
