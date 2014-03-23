package chapter03;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/** 
 * Run this example by running CubeChaser4.
 * This class works together with the CubeChaser4Control and CubeChaser4. 
 * This AppState adds some cubes, 
 * some of which get the CubeChaser4Control attribute (i.e. chasable).
 * The AppState identifies the CubeChaser4Control'ed cubes 
 * and makes them behave "chaseable" (they run away).
 */
public class CubeChaser4State extends AbstractAppState {

    private Ray ray = new Ray();
    private Camera cam;
    private Node rootNode;
    private SimpleApplication app;
    private AssetManager assetManager;
    private static Box mesh = new Box(1, 1, 1);
    private int counter = 0;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.rootNode = this.app.getRootNode();
        this.cam = this.app.getCamera();

        makeCubes(40);
    }

    public void makeCubes(int number) {
        for (int i = 0; i < number; i++) {
            // randomize 3D coordinates
            Vector3f loc = new Vector3f(
                    FastMath.nextRandomInt(-20, 20),
                    FastMath.nextRandomInt(-20, 20),
                    FastMath.nextRandomInt(-20, 20));
            Geometry geom = myBox("Cube" + i, loc, ColorRGBA.randomColor());
            // make random cubes chasable
            if (FastMath.nextRandomInt(1, 4) == 4) {
                geom.addControl(new CubeChaser4Control());
            }
            rootNode.attachChild(geom);
        }
    }

    public Geometry myBox(String name, Vector3f loc, ColorRGBA color) {
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);
        return geom;
    }

    @Override
    public void update(float tpf) {
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from camera location in camera direction.
        ray.setOrigin(cam.getLocation());
        ray.setDirection(cam.getDirection());
        // 3. Collect intersections between ray and all nodes in results list.
        rootNode.collideWith(ray, results);
        // 4. Use the result
        if (results.size() > 0) {
            // The closest result is the target that the player picked:
            Geometry target = results.getClosestCollision().getGeometry();
            // if camera closer than 10...
            if (target.getControl(CubeChaser4Control.class) != null) {
                if (cam.getLocation().distance(target.getLocalTranslation()) < 10) {
                    // ... move the cube in the direction that camera is facing
                    target.move(cam.getDirection());
                    System.out.println(
                            target.getControl(CubeChaser4Control.class).hello()
                            + " and I am running away from "
                            + cam.getLocation());
                    counter++;
                }
            }
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.detachAllChildren();
    }
}