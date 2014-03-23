package chapter03;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * This example demonstrates an AnalogListener responding to user input (clicks), 
 * and target picking with a visible mouse cursor.
 * We two cubes and we use ray casting to select which cube rotates.
 */
public class TargetPickCursor extends SimpleApplication {

    private Trigger trigger_rotate = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private static Box mesh = new Box(1, 1, 1);

    @Override
    /** initialize the scene here */
    public void simpleInitApp() {
        /** register input mappings to input manager */
        inputManager.addMapping("Rotate", trigger_rotate);
        inputManager.addListener(analogListener, new String[]{"Rotate"});

        flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true); // make mouse cursor visible

        rootNode.attachChild(myBox("Blue Cube", new Vector3f(0, -2, 0), ColorRGBA.Blue));
        rootNode.attachChild(myBox("Red Cube", new Vector3f(0, 2, 0), ColorRGBA.Red));
    }

    /** Creates colored named cubes, translates to their position. */
    public Geometry myBox(String name, Vector3f loc, ColorRGBA color) {
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);

        return geom;
    }
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("Rotate")) {
                // Reset results list.
                CollisionResults results = new CollisionResults();
                // Convert screen click to 3d position
                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(
                        new Vector2f(click2d.getX(), click2d.getY()), 0f);
                Vector3f dir = cam.getWorldCoordinates(
                        new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
                // Aim the ray from the clicked spot forwards.
                Ray ray = new Ray(click3d, dir);
                // Collect intersections between ray and all nodes in results list.
                rootNode.collideWith(ray, results);
                // (Print the results so we see what is going on:)
                for (int i = 0; i < results.size(); i++) {
                    // (For each “hit”, we know distance, impact point, geometry.)
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String target = results.getCollision(i).getGeometry().getName();
                    System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
                }
                // Use the results -- we rotate the selected geometry.
                if (results.size() > 0) {
                    // The closest result is the target that the player picked:
                    Geometry target = results.getClosestCollision().getGeometry();
                    // Here comes the action:
                    if (target.getName().equals("Red Cube")) {
                        target.rotate(0, -intensity, 0);
                    } else if (target.getName().equals("Blue Cube")) {
                        target.rotate(0, intensity, 0);
                    }
                }

            } // else if ...

        }
    };

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        TargetPickCursor app = new TargetPickCursor();
        app.start();

    }
}
