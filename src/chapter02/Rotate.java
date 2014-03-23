package chapter02;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * This example shows a blue and a yellow cube, 
 * and different ways how to rotate them:
 * Rotate each relatively by radians;
 * Rotate each asbolutely via Quaternions;
 * Rotate both around a common pivot node.
 */
public class Rotate extends SimpleApplication {

    /** Create Geometries and attach them to the rootNode. */
    @Override
    public void simpleInitApp() {
        /* Blue cube */
        Box b = new Box(1, 1, 1);                     // create box mesh
        Geometry geo1 = new Geometry("Box 1", b);     // create geometry from mesh

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md"); // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);        // color the material BLUE
        geo1.setMaterial(mat);                        // assign the material to geometry
        rootNode.attachChild(geo1);                   // make geometry appear in scene
        
        /* Yellow cube */
        Box b2 = new Box(1, 1, 1);                    // create box mesh 
        Geometry geo2 = new Geometry("Box 2", b2);    // create geometry from mesh
        
        Material mat2 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md"); // create a simple material
        mat2.setColor("Color", ColorRGBA.Yellow);     // color the material YELLOW
        geo2.setMaterial(mat2);                       // assign the material to geometry
        Vector3f v = new Vector3f(2.0f, 1.0f, -3.0f);  
        geo2.setLocalTranslation(v);                  // position the cube
        rootNode.attachChild(geo2);                   // make geometry appear in scene

        /* Uncomment each test separately */
        
        /* TEST 1: relative rotation by radians */
//        float r = FastMath.DEG_TO_RAD * 45f;          // convert 45 degrees to radians
//        geo2.rotate(r   , 0.0f, 0.0f);                // relative rotation around x axis
//        geo1.rotate(0.0f,    r, 0.0f);                // relative rotation around y axis

        /* TEST 2: absolute rotation using Quaternion */
//        Quaternion roll045 = new Quaternion();
//        roll045.fromAngleAxis(45 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
//        geo2.setLocalRotation(roll045);

        /* TEST 3: absolute rotation with Quaternion and slerp interpolation */
//        Quaternion q1 = new Quaternion();
//        q1.fromAngleAxis(50 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
//        Quaternion q2 = new Quaternion();
//        q2.fromAngleAxis(40 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
//        Quaternion q3 = new Quaternion();
//        q3.slerp(q1, q2, 0.5f);                     // .5f = halfways between 50 and 40 deg
//        geo2.setLocalRotation(q3);

        /* TEST 4: Rotate both 45 deg around z around pivot node at origin */
//        Node pivot = new Node("pivot node");          // at origin
//        pivot.attachChild(geo1);
//        pivot.attachChild(geo2);
//        pivot.rotate(0, 0, FastMath.DEG_TO_RAD * 45);
//        rootNode.attachChild(pivot);
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        Rotate app = new Rotate();
        app.start();
    }
}
