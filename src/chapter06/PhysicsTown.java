package chapter06;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;

/**
 * We use physics to make the walls and floors of a town model solid.
 * We create a solid first-person player (camera) to walk around in our town.
 * @author Normen, Zathras
 */
public class PhysicsTown extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private RigidBodyControl scenePhy;
    private Node sceneNode;
    private Node playerNode;
    private BetterCharacterControl playerControl; 
    private Vector3f walkDirection = new Vector3f(0,0,0);
    private Vector3f viewDirection = new Vector3f(0,0,1);
    private boolean rotateLeft = false, rotateRight = false,
            strafeLeft = false, strafeRight = false,
            forward = false, backward = false;
    private float speed=8;
    private CameraNode camNode;

    public static void main(String[] args) {
        PhysicsTown app = new PhysicsTown();
        app.start();
    }

    public void simpleInitApp() {
        initPhysics();
        initLight();
        initNavigation();
        initScene();
        initCharacter();
        initCamera();
    }

    /** Initialize the physics simulation */
    private void initPhysics() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true); // collision shapes visible
    }

    /* Set up collision detection for 1st-person player. 
     * The player control itself has no visible geometry or location:
     * for a 3rd-person player, attach a geometry to the playerNode. */
    private void initCharacter() {
        // 1. Create a player node.
        playerNode = new Node("the player"); 
        playerNode.setLocalTranslation(new Vector3f(0, 6, 0));
        rootNode.attachChild(playerNode);
        // 2. Create a Character Physics Control.
        playerControl = new BetterCharacterControl(1.5f, 4, 30f);
        // 3. Set some properties of Character Physics Control
        playerControl.setJumpForce(new Vector3f(0, 300, 0));
        playerControl.setGravity(new Vector3f(0, -10, 0));
        // 4. Add the player control to the PhysicsSpace
        playerNode.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);
    }

    /** CameraNode depends on playerNode. The camera follows the player. */
    private void initCamera() {
        camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.setLocalTranslation(new Vector3f(0, 4, -6));
        Quaternion quat = new Quaternion();
        quat.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
        camNode.setLocalRotation(quat);
        playerNode.attachChild(camNode);
        camNode.setEnabled(true);
        flyCam.setEnabled(false);
    }

    /** Load a model with floors and walls and make them solid. */
    private void initScene() {
        // make the sky blue
        viewPort.setBackgroundColor(ColorRGBA.Blue); 
        // 1. Load the scene node
        assetManager.registerLocator("town.zip", ZipLocator.class);
        sceneNode = (Node) assetManager.loadModel("main.scene");
        sceneNode.scale(1.5f);
        rootNode.attachChild(sceneNode);
        // 2. Create a RigidBody PhysicsControl with mass zero
        // 3. Add the scene's PhysicsControl to the scene's geometry
        // 4. Add the scene's PhysicsControl to the PhysicsSpace
        scenePhy = new RigidBodyControl(0f);
        sceneNode.addControl(scenePhy);
        bulletAppState.getPhysicsSpace().add(scenePhy);
    }

    /** An ambient light and a directional sun light */
    private void initLight() {
        AmbientLight ambient = new AmbientLight();
        rootNode.addLight(ambient);
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1.4f, -1.4f, -1.4f));
        rootNode.addLight(sun);
    }

    /**
     * We override default fly camera key mappings (WASD), because we want to
     * use them for physics-controlled walking and jumping of the player.
     */
    private void initNavigation() {
        flyCam.setMoveSpeed(100);
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Back",    new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Rotate Left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rotate Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Strafe Left",  new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Strafe Right", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addListener(this, "Forward", "Rotate Left", "Rotate Right");
        inputManager.addListener(this, "Back", "Strafe Right", "Strafe Left", "Jump");
    }

    /**
     * Our  custom navigation actions are triggered by user input (WASD). 
     * No walking happens here yet -- we only keep track of 
     * the direction the user wants to go.
     */
    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Rotate Left")) {
            rotateLeft = isPressed;
        } else if (binding.equals("Rotate Right")) {
            rotateRight = isPressed;
        } else 
        if (binding.equals("Strafe Left")) {
            strafeLeft = isPressed;
        } else if (binding.equals("Strafe Right")) {
            strafeRight = isPressed;
        } else  
        if (binding.equals("Forward")) {
            forward = isPressed;
        } else if (binding.equals("Back")) {
            backward = isPressed;
        } else
        if (binding.equals("Jump")) {
            playerControl.jump();
        }
    }

    /**
     * First-person walking happens here in the update loop.
     */
    @Override
    public void simpleUpdate(float tpf) {
        // Get current forward and left vectors of the playerNode: 
        Vector3f modelForwardDir = playerNode.getWorldRotation().mult(Vector3f.UNIT_Z);
        Vector3f modelLeftDir    = playerNode.getWorldRotation().mult(Vector3f.UNIT_X);
        // Depending on which nav keys are pressed, determine the change in direction

        walkDirection.set(0, 0, 0);
        if (strafeLeft) {
            walkDirection.addLocal(modelLeftDir.mult(speed));
        } else if (strafeRight) {
            walkDirection.addLocal(modelLeftDir.mult(speed).negate());
        }
        if (forward) {
            walkDirection.addLocal(modelForwardDir.mult(speed));
        } else if (backward) {
            walkDirection.addLocal(modelForwardDir.mult(speed).negate());
        }
        playerControl.setWalkDirection(walkDirection);
        // Depending on which nav keys are pressed, determine the change in rotation
        if (rotateLeft) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
        } else if (rotateRight) {
            Quaternion rotateR = new Quaternion().fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateR.multLocal(viewDirection);
        }
        playerControl.setViewDirection(viewDirection);
    }

}
