package chapter04;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 * This example shows you the underlying wireframe of basic 3D shapes.
 * You see that each shape is a polygon mesh made up of triangles. 
 * The corner points of the triangles are called vertices (one vertex). 
 * Uncomment one of the lines that initializes a mesh (boxes and spheres)
 * at a time, and compare the outcome.
 */
public class WireframeShapes extends SimpleApplication {

    @Override
    /** initialize the scene here */
    public void simpleInitApp() {
        /* uncomment one of these meshes */
        //Box mesh = new Box(1, 1, 1);              // create box mesh OR
        //Sphere mesh = new Sphere(16, 16, 1.0f);   // create sphere mesh OR
        //Sphere mesh = new Sphere(32, 16, 1.0f);   // create sphere mesh OR
        Sphere mesh = new Sphere(16, 32, 1.0f);   // create sphere mesh OR
        //Sphere mesh = new Sphere(32, 32, 1.0f);   // create sphere mesh
        
        Geometry geom = new Geometry("Box", mesh);    // create geometry from this mesh

        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);         // color the material blue
        geom.setMaterial(mat);                         // give object the blue material
        rootNode.attachChild(geom);                    // make object appear in scene
        
        mat.getAdditionalRenderState().setWireframe(true); // activate wireframe view
    }


    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        WireframeShapes app = new WireframeShapes();
        app.start();
        
    }
}
