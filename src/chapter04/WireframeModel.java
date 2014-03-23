package chapter04;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

/**
 * This example shows you the underlying wireframe of a 3D model. 
 * You see that a model is a polygon mesh made up of triangles.
 */
public class WireframeModel extends SimpleApplication {

    @Override
    /** initialize the scene here */
    public void simpleInitApp() {
        Geometry teapot = (Geometry) 
                assetManager.loadModel("Models/Teapot/Teapot.j3o");
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Blue);         // color the material blue
        teapot.setMaterial(mat);                       // give object the blue material
        rootNode.attachChild(teapot);                  // make object appear in scene

        mat.getAdditionalRenderState().setWireframe(true); // activate wireframe view
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        WireframeModel app = new WireframeModel();
        app.start();

    }
}
