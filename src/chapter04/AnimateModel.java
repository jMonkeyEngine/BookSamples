package chapter04;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/** This example loads an animated model and plays the animation. 
 * Press the SPACEBAR to start the "Walk" animation.
 * We use a AnimControl with one AnimChannel for the animation, 
 * and an AnimEventListener to monitor which state the animation is in.
 * (idle or walking)
 */
public class AnimateModel extends SimpleApplication
        implements AnimEventListener {

    private static final String MAPPING_WALK = "walk forwards";
    private static final String ANI_IDLE = "Idle";
    private static final String ANI_WALK = "Walk";
    private Node player;
    private AnimControl control;
    private AnimChannel channel;

    public static void main(String[] args) {
        AnimateModel app = new AnimateModel();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        inputManager.addMapping(MAPPING_WALK, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, MAPPING_WALK);
        inputManager.addListener(analogListener, MAPPING_WALK);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        player = (Node) assetManager.loadModel("Models/monkeyExport/Jaime.j3o");
        player.rotate(0, FastMath.DEG_TO_RAD * 180, 0);
        rootNode.attachChild(player);

        control = player.getControl(AnimControl.class);
        control.addListener(this);

        for (String anim : control.getAnimationNames()) {
            System.out.println(anim);
        }

        channel = control.createChannel();
        channel.setAnim(ANI_IDLE);
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals(ANI_WALK)) {
            System.out.println(control.getSpatial().getName() + " completed one walk loop.");
        } else if (animName.equals(ANI_IDLE)) {
            System.out.println(control.getSpatial().getName() + " completed one idle loop.");
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals(ANI_WALK)) {
            System.out.println(control.getSpatial().getName() + " started walking.");
        } else if (animName.equals(ANI_IDLE)) {
            System.out.println(control.getSpatial().getName() + " started being idle.");
        }
    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(MAPPING_WALK) && isPressed) {
                if (!channel.getAnimationName().equals(ANI_WALK)) {
                    channel.setAnim(ANI_WALK);
                }
            }
            if (name.equals(MAPPING_WALK) && !isPressed) {
                channel.setAnim(ANI_IDLE);
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals(MAPPING_WALK)) {
                player.move(0, 0, tpf);
            }
        }
    };
}