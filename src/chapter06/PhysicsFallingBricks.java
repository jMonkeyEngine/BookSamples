package chapter06;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;

/**
 * Click to shoot cannon balls at a brick wall. 
 * @author double1984, modified by zathras.
 */
public class PhysicsFallingBricks extends SimpleApplication {
    private static final String SHOOT = "shoot";

  public static void main(String args[]) {
    PhysicsFallingBricks app = new PhysicsFallingBricks();
    app.start();
  }
  
  /** Physics application state (jBullet) */
  private BulletAppState bulletAppState;
  
  /** Materials for bricks, cannon balls, floor. */
  Material brickMat, stoneMat, woodMat;
  /** Mesh shapes for bricks, cannon balls, floor. */
  private static final Box brickMesh;
  private static final Sphere ballMesh;
  private static final Box floorMesh;
  private static Node wallNode;
  /** PhysicsControls for bricks, cannon balls, floor. */
  private RigidBodyControl brickPhy;
  private RigidBodyControl ballPhy;
  private RigidBodyControl floorPhy;
  /** The dimensions used for bricks and wall */
  private static final float BRICK_LENGTH = 0.4f;
  private static final float BRICK_WIDTH  = 0.3f;
  private static final float BRICK_HEIGHT = 0.25f;
  private static final float WALL_WIDTH=12; 
  private static final float WALL_HEIGHT=6;

  static {
    /** Initialize reusable mesh shapes. */
    floorMesh  = new Box(10f, 0.5f, 5f);
    brickMesh    = new Box(BRICK_LENGTH, BRICK_HEIGHT, BRICK_WIDTH);
    ballMesh = new Sphere(32,32, 0.25f, true, false);
    ballMesh.setTextureMode(TextureMode.Projected);
    floorMesh.scaleTextureCoordinates(new Vector2f(4f,4f));
  }

  @Override
  public void simpleInitApp() {
    /** Make this a jBullet Physics Game */
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    
    /** Initialize the scene using helper methods (keeps code readable). */
    initMaterials();
    initLight(); 
    initUserInterface();
    initFloor();
    initBrickwall();

    /** Add InputManager action: Left click triggers shooting. */
    inputManager.addMapping(SHOOT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addListener(actionListener, SHOOT);
    
    /** Move camera to look at scene */
    cam.setLocation(new Vector3f(0f,2f,6f));
    cam.lookAt(new Vector3f(0f,2f,0f),Vector3f.UNIT_Y);
    
  }

  /** Keep default navigation inputs, add shoot action.
   *  Each shot introduces a new cannon ball. */
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals(SHOOT) && !keyPressed) {
        shootCannonBall();
      }
    }
  };

  /** Make a solid floor and add it to the scene. */
  public void initFloor() {
    /* Create and attach floor geometry */
    Geometry floorGeo = new Geometry("Floor", floorMesh);
    floorGeo.setMaterial(woodMat);
    floorGeo.move(0, -BRICK_HEIGHT*2, 0); // don't collide with bricks
    rootNode.attachChild(floorGeo);
    /* Make the floor physical and static (mass zero!) */
    floorPhy = new RigidBodyControl(0.0f);
    floorGeo.addControl(floorPhy);
    bulletAppState.getPhysicsSpace().add(floorPhy);
}
  

  /** This loop builds a wall out of individual bricks. */
  public void initBrickwall() {
    wallNode=new Node("wall");
    float offsetH = BRICK_LENGTH / 3;
    float offsetV = 0;
    for (int j = 0; j < WALL_HEIGHT; j++) { 
      for (int i = 0; i < WALL_WIDTH; i++) {
        Vector3f brickPos = new Vector3f(
                offsetH + BRICK_LENGTH*2.1f*i -(BRICK_LENGTH*WALL_WIDTH), 
                offsetV + BRICK_HEIGHT, 
                0f );
        wallNode.attachChild(makeBrick(brickPos));
      }
      offsetH = -offsetH;
      offsetV += 2 * BRICK_HEIGHT;
    }
    rootNode.attachChild(wallNode);
  }

  /** This method creates one individual physical brick. */
  public Geometry makeBrick(Vector3f loc) {
    /** Create a brick geometry and attach to scene graph. */
    Geometry brickGeo = new Geometry("brick", brickMesh);
    brickGeo.setMaterial(brickMat);
    brickGeo.move(loc);
    /** Create physical brick and add to physics space. */
    brickPhy = new RigidBodyControl(5f);
    brickGeo.addControl(brickPhy);
    bulletAppState.getPhysicsSpace().add(brickPhy);
    //brickPhy.setFriction(.2f); // try for example .2 versus 20f
    return brickGeo;
  }

  /** This method creates one individual physical cannon ball.
   * By defaul, the ball is accelerated and flies
   * from the camera position in the camera direction.*/
  public void shootCannonBall() {
    /** Create a cannon ball geometry and attach to scene graph. */
    Geometry ballGeo = new Geometry("cannon ball", ballMesh);
    ballGeo.setMaterial(stoneMat);
    ballGeo.setLocalTranslation(cam.getLocation());
    rootNode.attachChild(ballGeo);
    /** Create physical cannon ball and add to physics space. */
    ballPhy = new RigidBodyControl(.5f);
    ballGeo.addControl(ballPhy);
    bulletAppState.getPhysicsSpace().add(ballPhy);    
    ballPhy.setCcdSweptSphereRadius(.1f);
    ballPhy.setCcdMotionThreshold(0.001f);
    /** Accelerate the physical ball in camera direction to shoot it! */
    ballPhy.setLinearVelocity(cam.getDirection().mult(50));
  }
  
  /** Create reusable materials. */
  private void initMaterials() {
    brickMat  = assetManager.loadMaterial("Materials/brick.j3m");
    stoneMat  = assetManager.loadMaterial("Materials/pebbles.j3m");
    woodMat   = assetManager.loadMaterial("Materials/bark.j3m");
  }
  
  /** Create light sources. */
  private void initLight() {
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1.1f, -1.3f, -2.1f));
    rootNode.addLight(sun);
    rootNode.addLight(new AmbientLight());
  }
  
  /** User interface: Crosshairs to help with aiming. */
  protected void initUserInterface() {
    // remove the default UI that displays statistics
    setDisplayStatView(false);
    // add custom UI that displays crosshairs
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+");        // Fake crosshairs made of a plus sign :-)
    ch.setLocalTranslation( // Move crosshairs to center of screen
            (settings.getWidth() / 2) , (settings.getHeight() / 2)  , 0);
    guiNode.attachChild(ch);
  }
}
