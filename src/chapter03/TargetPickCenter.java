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
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * This example demonstrates an AnalogListener responding to user input (clicks), 
 * and picking a target using a center mark as "crosshairs".
 * We have two cubes and we use ray casting to select one of them,
 * and we make the selected cube rotate.
 */
public class TargetPickCenter extends SimpleApplication {
    private static final String MAPPING_ROTATE = "Rotate";
    private static final Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private static Box mesh = new Box(1, 1, 1);

    @Override
    /** initialize the scene here */
    public void simpleInitApp() {
        /** register input mappings to input manager */
        inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
        inputManager.addListener(analogListener, new String[]{MAPPING_ROTATE});

        attachCenterMark(); // mark the center of the screen with a white box
        
        rootNode.attachChild(myBox("Blue Cube", new Vector3f(0, -1.5f, 0), ColorRGBA.Blue));
        rootNode.attachChild(myBox("Red Cube",  new Vector3f(0, 1.5f, 0), ColorRGBA.Red));
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
    
    /** This method attaches a small white cube to mark the center of the screen.
     * Note that this mark is attached to the guiNode (not to the rootNode),
     * so it is part of the 2D user interface, and not part of the 3D scene.
     */
    private void attachCenterMark() {
        Geometry c = myBox("center mark", Vector3f.ZERO, ColorRGBA.White);
        c.scale(4);  
        // move to center of x/y, with y (depth) zero.
        c.setLocalTranslation(
                settings.getWidth()/2,
                settings.getHeight()/2,
                0);  
        guiNode.attachChild(c); // attach to 2D user interface
    }

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals(MAPPING_ROTATE)) {
                // Reset results list.
                CollisionResults results = new CollisionResults();
                // Aim the ray from camera location in camera direction 
                // (assuming crosshairs in center of screen).
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                // Collect intersections between ray and all nodes in results list.
                rootNode.collideWith(ray, results);
                // (Print the results so we see what is going on)
                for (int i = 0; i < results.size(); i++) {
                    // (For each “hit”, we know distance, impact point, geometry.)
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String target = results.getCollision(i).getGeometry().getName();
                    System.out.println("Selection: #" + i + ": " + target +
                            " at " + pt + ", " + dist + " WU away.");
                }
                // 5. Use the results -- we rotate the selected geometry.
                if (results.size() > 0) {
                    // The closest result is the target that the player picked:
                    Geometry target = results.getClosestCollision().getGeometry();
                    // Here comes the action:
                    if (target.getName().equals("Red Cube")) {
                        target.rotate(0, -intensity, 0);
                    } else if (target.getName().equals("Blue Cube")) {
                        target.rotate(0, intensity, 0);
                    }
                } else {
                    System.out.println("Selection: Nothing" );
                }
            }   
        }
    };

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        TargetPickCenter app = new TargetPickCenter();
        app.start();

    }
}
