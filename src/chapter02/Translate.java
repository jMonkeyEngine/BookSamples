package chapter02;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Sample 2.2
 * This example shows a blue and a yellow cube. 
 * and different ways how to translate, that is move, them.
 * The yellow cube is translated (moved) to a new location v.
 */
public class Translate extends SimpleApplication {

    @Override
    /** Create Geometries and attach them to the rootNode. */
    public void simpleInitApp() {

        /* Cube 1 */
        Box b = new Box(1, 1, 1);                     // create box mesh
        Geometry geo1 = new Geometry("Box 1", b);     // create geometry from mesh

        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md"); // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);        // color the material blue
        geo1.setMaterial(mat);                        // assign the material to geometry
        rootNode.attachChild(geo1);                   // make geometry appear in scene
        
        /* Cube 2 */
        Box b2 = new Box(1, 1, 1);                    // create box mesh
        Geometry geo2 = new Geometry("Box 2", b2);    // create geometry from mesh

        Material mat2 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md"); // create a simple material
        mat2.setColor("Color", ColorRGBA.Yellow);     // color the material YELLOW
        geo2.setMaterial(mat2);                       // assign the material to geometry
        rootNode.attachChild(geo2);                   // make geometry appear in scene
        
        Vector3f v = new Vector3f(2.0f , 1.0f , -3.0f);        

        /* TEST 1  */
//        geo2.setLocalTranslation(v);                 // test absolute translation
        
        /* TEST 2  */
//        geo2.move(v);                                // test relative translation 
//        geo2.move(v);                                // test relative translation

    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        Translate app = new Translate();
        app.start();   
    }
}
