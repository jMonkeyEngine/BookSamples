package chapter03;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * This demo uses the simpleUpdate() loop to determine which cube 
 * the player (the camera) looks at. When the player is closer than 10 wu, 
 * the cube moves further away.
 * The loop uses a ray casting algorythm to measure the distance.
 */
public class CubeChaser2 extends SimpleApplication {

    private static Box mesh = new Box(1, 1, 1);
    private Ray ray = new Ray();

    @Override
    /** initialize the scene here */
    public void simpleInitApp() {
        flyCam.setMoveSpeed(100f);
        makeCubes(40);
    }

    private void makeCubes(int number) {
        for (int i = 0; i < number; i++) {
            // randomize 3D coordinates
            Vector3f loc = new Vector3f(
                    FastMath.nextRandomInt(-20, 20),
                    FastMath.nextRandomInt(-20, 20),
                    FastMath.nextRandomInt(-20, 20));
            rootNode.attachChild(
                    myBox("Cube" + i, loc, ColorRGBA.randomColor()));
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
    /** Interact with update loop here */
    public void simpleUpdate(float tpf) {
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
            if (cam.getLocation().distance(target.getLocalTranslation()) < 10) {
                // ... move the cube in the direction that camera is facing
                target.move(cam.getDirection());
            }
        }
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        CubeChaser2 app = new CubeChaser2();
        app.start();

    }
}
