package chapter06;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.util.TangentBinormalGenerator;

/**
 * An automated platform that starts rising when a special dynamic object
 * arrives as passenger (collision listener).
 * @author zathras.
 */
public class PhysicsKinematic2 extends SimpleApplication
        implements PhysicsCollisionListener {

    /* Constants */
    private static final String BALL     = "Ball";
    private static final String ELEVATOR = "Elevator";
    private static final float  TOPFLOOR = 6f;
    /* Materials and geometries. */
    private Material brickMat, stoneMat, woodMat;
    private Geometry platformGeo;
    /* Physics application state (jBullet) */
    private BulletAppState bulletAppState;
    private RigidBodyControl ballPhy;
    private RigidBodyControl scenePhy;
    /*booleans*/
    private boolean isBallOnPlatform = false;

    public static void main(String args[]) {
        PhysicsKinematic2 app = new PhysicsKinematic2();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        /** Make this a jBullet Physics Game */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        setDisplayStatView(false);

        /** Initialize the scene using helper methods (keeps code readable). */
        initLight();
        initMaterials();
        initFloor();
        initPlatform();
        dropBall();

        /** Move camera to look at scene */
        cam.setLocation(new Vector3f(0f, 16f, 10f));
        cam.lookAt(new Vector3f(0,0,-2), Vector3f.UNIT_Y);
    }

    /** The Scene is made up of a solid floor, slope, and wall. */
    public void initFloor() {
        Node sceneNode = new Node("Scene");
        
        /* Create and attach floor geometry */        
        Box floorMesh = new Box(10f, 0.5f, 10f);
        TangentBinormalGenerator.generate(floorMesh);
        Geometry floorGeo = new Geometry("Floor", floorMesh);
        floorGeo.setMaterial(stoneMat);
        floorGeo.move(0, -.1f, 0);
        sceneNode.attachChild(floorGeo);

        /* Create and attach slope geometry */        
        Box slopeMesh = new Box(6f, 0.1f, 5f);
        TangentBinormalGenerator.generate(slopeMesh);
        Geometry slopeGeo = new Geometry("Slope", slopeMesh);
        slopeGeo.setMaterial(brickMat);
        slopeGeo.rotate(0, 0, FastMath.DEG_TO_RAD * 50);
        slopeGeo.move(4f, 4f, 0);
        sceneNode.attachChild(slopeGeo);

        /* Create and attach wall geometry */        
        Box wallMesh = new Box(5f, 0.4f, 5f);
        TangentBinormalGenerator.generate(wallMesh);
        Geometry wallGeo = new Geometry("Wall", wallMesh);
        wallGeo.setMaterial(brickMat);
        wallGeo.rotate(0, 0, FastMath.DEG_TO_RAD * 90);
        wallGeo.move(-3.5f, 2, 0);
        sceneNode.attachChild(wallGeo);

        /* Make Scene solid and static */        
        scenePhy = new RigidBodyControl(0.0f);
        sceneNode.addControl(scenePhy);
        bulletAppState.getPhysicsSpace().add(scenePhy);
        rootNode.attachChild(sceneNode);
    }

    /** Make a kinematic platform and add it to the scene. */
    public void initPlatform() {
        Box platformMesh = new Box(2f, 0.5f, 5f);
        TangentBinormalGenerator.generate(platformMesh);
        platformGeo = new Geometry(ELEVATOR, platformMesh);
        platformGeo.setMaterial(woodMat);
        platformGeo.move(-1, 2, 0);
        rootNode.attachChild(platformGeo);
        RigidBodyControl platformPhy = new RigidBodyControl(100.0f);
        platformGeo.addControl(platformPhy);
        platformPhy.setKinematic(true);
        bulletAppState.getPhysicsSpace().add(platformPhy);
    }

    /** This method creates one individual physical ball.*/
    public void dropBall() {
        /** Create a cannon ball geometry and attach to scene graph. */
        Sphere ballMesh = new Sphere(32, 32, .75f, true, false);
        ballMesh.setTextureMode(TextureMode.Projected);
        TangentBinormalGenerator.generate(ballMesh);
        Geometry ballGeo = new Geometry(BALL, ballMesh);
        ballGeo.setMaterial(stoneMat);
        rootNode.attachChild(ballGeo);
        /** Create physical cannon ball and add to physics space. */
        ballPhy = new RigidBodyControl(5f);
        ballGeo.addControl(ballPhy);
        bulletAppState.getPhysicsSpace().add(ballPhy);
        ballPhy.setPhysicsLocation(new Vector3f(0, 10, 0));
    }

    /** Create reusable materials. */
    private void initMaterials() {
        brickMat = assetManager.loadMaterial("Materials/brick.j3m");
        stoneMat = assetManager.loadMaterial("Materials/pebbles.j3m");
        woodMat  = assetManager.loadMaterial("Materials/bark.j3m");
    }

    /** Create light sources. */
    private void initLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1.1f, -1.3f, 1.1f));
        rootNode.addLight(sun);
        rootNode.addLight(new AmbientLight());
    }

    /* collisionlistener with test, action happens in update loop */
    public void collision(PhysicsCollisionEvent event) {
        if ((event.getNodeA().getName().equals(BALL)
                && event.getNodeB().getName().equals(ELEVATOR))
         || (event.getNodeA().getName().equals(ELEVATOR)
                && event.getNodeB().getName().equals(BALL))) 
        {
            isBallOnPlatform = true;
        } else {
            isBallOnPlatform = false;
        }
    }

    /* update loop relies on test from collisionlistener and moves platform */
    @Override
    public void simpleUpdate(float tpf) {
        if (isBallOnPlatform && platformGeo.getLocalTranslation().getY() < TOPFLOOR) {
            platformGeo.move(0f, tpf, 0f);
        }
        if (isBallOnPlatform && platformGeo.getLocalTranslation().getY() >= TOPFLOOR) {
        }
        if (!isBallOnPlatform && platformGeo.getLocalTranslation().getY() > .5f) {
            platformGeo.move(0f, -tpf * 4, 0f);
        }
    }

 
}
