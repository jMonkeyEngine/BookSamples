package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.renderer.Caps;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 * The demo shows how to make a scene appear in cartoon style. Note the black lines
 * along the edges, and the reduced gradients, e.g. on the teapot. 
 * Note that the code applies a custom method on the rootNode 
 * that adjust material settings scene-wide. Try this on any scene.
 */
public class Cartoon extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        viewPort.setBackgroundColor(new ColorRGBA(.5f, .7f, .9f, 1));
        flyCam.setMoveSpeed(50);

        /** are minimum requirements for cel shader met? */
        if (renderer.getCaps().contains(Caps.GLSL100)) {
            FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
            viewPort.addProcessor(fpp);

            CartoonEdgeFilter toon = new CartoonEdgeFilter();
            //toon.setEdgeColor(ColorRGBA.Black);
            fpp.addFilter(toon);
        }

        DirectionalLight sunLight = new DirectionalLight();
        sunLight.setDirection(new Vector3f(.3f, -0.5f, -0.5f));
        rootNode.addLight(sunLight);

        initScene();

        makeToonish(rootNode);

    }

    public void makeToonish(Spatial spatial) {
        if (spatial instanceof Node) {
            Node n = (Node) spatial;
            for (Spatial child : n.getChildren()) {
                makeToonish(child);
            }
        } else if (spatial instanceof Geometry) {
            Geometry g = (Geometry) spatial;
            Material m = g.getMaterial();
            if (m.getMaterialDef().getName().equals("Phong Lighting")) {
                Texture t = assetManager.loadTexture("Textures/ColorRamp/toon.png");
                m.setTexture("ColorRamp", t);
                m.setBoolean("VertexLighting", true);
                m.setBoolean("UseMaterialColors", true);
                m.setColor("Specular", ColorRGBA.Black);
                m.setColor("Diffuse", ColorRGBA.White);
            }
        }
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
        teapotMat.setColor("Diffuse", ColorRGBA.Pink);
        teapotGeo.setMaterial(teapotMat);
        teapotGeo.scale(3);
        teapotGeo.setLocalTranslation(32, 3, -24);
        rootNode.attachChild(teapotGeo);
        cam.lookAt(teapotGeo.getWorldTranslation(), Vector3f.UNIT_Y);
    }

    public static void main(String[] args) {
        Cartoon app = new Cartoon();
        app.start();
    }
}
