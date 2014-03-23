package chapter02;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * The Basic jMonkeyEngine game template. Copy and refactor for exercises.
 * We initialize a SimpleApplication, start the app, and render a blue cube.
 * Optionally, you can hook code into the update loop and the renderer.
 */
public class Main extends SimpleApplication {

    @Override
    /** Initialize the scene here: 
     *  Create Geometries and attach them to the rootNode. */
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);                     // create box mesh
        Geometry geom = new Geometry("Box", b);       // create geometry from mesh

        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md"); // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);        // color the material blue
        geom.setMaterial(mat);                        // assign the material to geometry
        rootNode.attachChild(geom);                   // make geometry appear in scene
    }

    @Override
    /** Hook code into the main update loop here. 
     *  This is where the action happens in your game. */
    public void simpleUpdate(float tpf) {
      /* Nothing yet. */
    } 

    @Override
    /** (optional) Advanced renderer/frameBuffer modifications.  */
    public void simpleRender(RenderManager rm) {
      /* Not used in this example. */
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        Main app = new Main();
        app.start();   
    }
}
