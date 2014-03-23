package chapter05;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.SpotLightShadowFilter;
import com.jme3.shadow.SpotLightShadowRenderer;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.TangentBinormalGenerator;

/**
 * This demo shows loading a tank model with a multimapped material 
 * using UV textures and Phong illumination.
 * You also see a floor geometry with multimapped material using seamless tiled textures.
 * Also we are testing some light sources. No drop shadows.
 */
public class HoverTank extends SimpleApplication {

    private SpotLight spot;

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10);
        initLights();
        initEffects();
        initScene();
    }

    @Override
    public void simpleUpdate(float tpf) {
        spot.setDirection(cam.getDirection());
        spot.setPosition(cam.getLocation());
    }

    public static void main(String[] args) {
        HoverTank app = new HoverTank();
        app.start();
    }

    private void initEffects() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        
        /* Drop shadow test */
        final int SHADOWMAP_SIZE=1024;
        SpotLightShadowRenderer dlsr = new SpotLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        dlsr.setLight(spot);
        viewPort.addProcessor(dlsr);
        SpotLightShadowFilter dlsf = new SpotLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        dlsf.setLight(spot);
        dlsf.setEnabled(true); // try true or false
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
        
        /* Activate the glow effect in the hover tank's material*/
        BloomFilter bf = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bf);
        viewPort.addProcessor(fpp);
    }

    private void initScene() {
        /** loading a multimapped hover tank model */
        Node tank = (Node) assetManager.loadModel(
                "Models/HoverTank/Tank.j3o");

        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");

        TextureKey tankDiffuse = new TextureKey("Models/HoverTank/tank_diffuse.jpg", false);
        mat.setTexture("DiffuseMap", assetManager.loadTexture(tankDiffuse));

        TangentBinormalGenerator.generate(tank);
        TextureKey tankNormal = new TextureKey("Models/HoverTank/tank_normals.png", false);
        mat.setTexture("NormalMap", assetManager.loadTexture(tankNormal));

        TextureKey tankSpecular = new TextureKey("Models/HoverTank/tank_specular.jpg", false);
        mat.setTexture("SpecularMap", assetManager.loadTexture(tankSpecular));
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Gray);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 120f);

        TextureKey tankGlow = new TextureKey("Models/HoverTank/tank_glow_map.jpg", false);
        mat.setTexture("GlowMap", assetManager.loadTexture(tankGlow));
        mat.setColor("GlowColor", ColorRGBA.White);

        //Material mat = assetManager.loadMaterial("Materials/tank.j3m");

        tank.setMaterial(mat);
        rootNode.attachChild(tank);

        /** a textured floor geometry */
        Box floorMesh = new Box(new Vector3f(-20, -2, -20), new Vector3f(20, -3, 20));
        floorMesh.scaleTextureCoordinates(new Vector2f(8, 8));
        Geometry floorGeo = new Geometry("floor", floorMesh);
        Material floorMat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setTexture("DiffuseMap", assetManager.loadTexture(
                "Textures/BrickWall/BrickWall_diffuse.jpg"));
        floorMat.setTexture("NormalMap", assetManager.loadTexture(
                "Textures/BrickWall/BrickWall_normal.jpg"));
        floorMat.getTextureParam("NormalMap").getTextureValue().setWrap(WrapMode.Repeat);
        floorMat.getTextureParam("DiffuseMap").getTextureValue().setWrap(WrapMode.Repeat);
        floorGeo.setMaterial(floorMat);
        rootNode.attachChild(floorGeo);
    }

    private void initLights() {
        /** A white, directional light source */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1.1f, -1.5f, -1.5f));
        sun.setColor(ColorRGBA.White);
        //rootNode.addLight(sun);

        /* A white ambient light, no shading */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.Gray);
       // rootNode.addLight(ambient);

        /** A cone-shaped spotlight with location, direction, range */
        spot = new SpotLight();
        spot.setSpotRange(100);
        spot.setSpotOuterAngle(20 * FastMath.DEG_TO_RAD);
        spot.setSpotInnerAngle(15 * FastMath.DEG_TO_RAD);
        spot.setDirection(cam.getDirection());
        spot.setPosition(cam.getLocation());
        rootNode.addLight(spot);
    }
}
