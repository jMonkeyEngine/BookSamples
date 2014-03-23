package chapter04;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * This example demonstrates loading a 3D model.
 * Various formats are supported, OBJ, Ogre, and binary .j3o (jME3's format).
 * Note that you save models in non-jME3 formats under "assets/Textures/";
 * save the final .j3o model under "assets/Model/".
 * You need a light source to render the model.
 */
public class LoadModel extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        /* Uncomment one of the following lines. */
        //Spatial mymodel = assetManager.loadModel("Textures/MyModel/MyModel.obj");      // OBJ or
        //Spatial mymodel = assetManager.loadModel("Textures/MyModel/MyModel.mesh.xml"); // Ogre or
        Spatial mymodel = assetManager.loadModel("Models/MyModel/MyModel.j3o");        // j3o
        rootNode.attachChild(mymodel);
        
        /* work-around for missing textures: */
        //Material mat = new Material( assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        //mymodel.setMaterial(mat);

        /** A white, directional light source */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        LoadModel app = new LoadModel();
        app.start();
    }
}
