package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.scene.Spatial;

/**
 * This demo shows depth-of-field blur in a scene. 
 * Move the mouse to look around and note how the view focuses and blurs.
 * Press SPACE, U/J, I/K, O/L to try different focus settings.
 * @author Nehon, edits by zathras
 */
public class DepthOfFieldBlur extends SimpleApplication {

    private FilterPostProcessor fpp;
    private DepthOfFieldFilter dofFilter;
    private Spatial sceneGeo;

    public static void main(String[] args) {
        DepthOfFieldBlur app = new DepthOfFieldBlur();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        viewPort.setBackgroundColor(new ColorRGBA(.5f, .7f, .9f, 1));
        flyCam.setMoveSpeed(50);
        initLights();
        initEffects();
        
        initKeyInputs();

        initScene();
    }

    private void initScene() {
        // load a town model
        assetManager.registerLocator("town.zip", ZipLocator.class);
        sceneGeo = assetManager.loadModel("main.scene");
        sceneGeo.setLocalScale(2f);
        sceneGeo.setLocalTranslation(0, -10f, 0);
        rootNode.attachChild(sceneGeo);
    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                if (name.equals("toggle")) {
                    dofFilter.setEnabled(!dofFilter.isEnabled());
                }
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("blurScaleUp")) {
                dofFilter.setBlurScale(dofFilter.getBlurScale() + 0.01f);
                System.out.println("blurScale : " + dofFilter.getBlurScale());
            }
            if (name.equals("blurScaleDown")) {
                dofFilter.setBlurScale(dofFilter.getBlurScale() - 0.01f);
                System.out.println("blurScale : " + dofFilter.getBlurScale());
            }
            if (name.equals("focusRangeUp")) {
                dofFilter.setFocusRange(dofFilter.getFocusRange() + 1f);
                System.out.println("focusRange : " + dofFilter.getFocusRange());
            }
            if (name.equals("focusRangeDown")) {
                dofFilter.setFocusRange(dofFilter.getFocusRange() - 1f);
                System.out.println("focusRange : " + dofFilter.getFocusRange());
            }
            if (name.equals("focusDistanceUp")) {
                dofFilter.setFocusDistance(dofFilter.getFocusDistance() + 1f);
                System.out.println("focusDistance : " + dofFilter.getFocusDistance());
            }
            if (name.equals("focusDistanceDown")) {
                dofFilter.setFocusDistance(dofFilter.getFocusDistance() - 1f);
                System.out.println("focusDistance : " + dofFilter.getFocusDistance());
            }
        }
    };

    /** To improve the depth-of-field blur effect, we change the focus distance
     *  live depending on what the player is looking at. */
    @Override
    public void simpleUpdate(float tpf) {
        // change the focus depending on where the player looks
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        CollisionResults results = new CollisionResults();
        int numCollisions = sceneGeo.collideWith(ray, results);
        if (numCollisions > 0) {
            CollisionResult hit = results.getClosestCollision();
            dofFilter.setFocusDistance(hit.getDistance() / 10.0f);
            fpsText.setText("Press SPACE, U/J, I/K, O/L. Distance: " + hit.getDistance());
        } else {
            fpsText.setText("Press SPACE, U/J, I/K, O/L. Distance: INF");
        }
    }

    /** These keys change settings so you can easily experiment */
    private void initKeyInputs() {
        // configure keys to experiment with focal blur settings
        inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("blurScaleUp", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("blurScaleDown", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("focusRangeUp", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("focusRangeDown", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("focusDistanceUp", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("focusDistanceDown", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addListener(analogListener, "blurScaleUp", "blurScaleDown",
                "focusRangeUp", "focusRangeDown", "focusDistanceUp", "focusDistanceDown");
        inputManager.addListener(actionListener, "toggle");
    }

    private void initLights() {
        DirectionalLight sunLight = new DirectionalLight();
        sunLight.setDirection(new Vector3f(.3f, -0.5f, -0.5f));
        rootNode.addLight(sunLight);
    }

    /* activate depth-of-field-blur effect */
    private void initEffects() {
        fpp = new FilterPostProcessor(assetManager);
        dofFilter = new DepthOfFieldFilter();
        dofFilter.setFocusDistance(0);
        dofFilter.setFocusRange(20);
        dofFilter.setBlurScale(2f);
        fpp.addFilter(dofFilter);
        viewPort.addProcessor(fpp);
    }
}
