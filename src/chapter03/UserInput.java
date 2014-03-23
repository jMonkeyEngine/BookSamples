package chapter03;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * This sample demonstrates how to detect and respond to user input.
 * Left-clicking rotates the cube ("analog"). 
 * Pressing spacebar or C toggles the cube's color ("discrete").
 * It also shows how to free up existing input mappings.
 */
public class UserInput extends SimpleApplication {

    private Geometry geom;
    private final static Trigger TRIGGER_COLOR = new KeyTrigger(KeyInput.KEY_SPACE);
    private final static Trigger TRIGGER_COLOR2 = new KeyTrigger(KeyInput.KEY_C);
    private final static Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static String  MAPPING_COLOR = "Toggle Color";
    private final static String  MAPPING_ROTATE = "Rotate";

    @Override
    /** initialize the scene here */
    public void simpleInitApp() {
        /** unregister one default input mapping, so you can refine Key_C */
        inputManager.deleteMapping(INPUT_MAPPING_CAMERA_POS); // free up Key_C
        /** register input mappings to input manager */
        inputManager.addMapping(MAPPING_COLOR, TRIGGER_COLOR, TRIGGER_COLOR2);
        inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
        inputManager.addListener(actionListener, new String[]{MAPPING_COLOR});
        inputManager.addListener(analogListener, new String[]{MAPPING_ROTATE});

        /** Create a blue cube */
        Box mesh = new Box(1, 1, 1);
        geom = new Geometry("Box", mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            System.out.println("Mapping detected (discrete): "+name );
            if (name.equals(MAPPING_COLOR) && !isPressed) {
                geom.getMaterial().setColor("Color", ColorRGBA.randomColor());
            } // else if ...
        }
    };
    
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float intensity, float tpf) {
            System.out.println("Mapping detected (analog): "+name );
            if (name.equals(MAPPING_ROTATE)) {
                geom.rotate(0, intensity * 10f, 0);
            } // else if ...
        }
    };

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        UserInput app = new UserInput();
        app.start();

    }
}
