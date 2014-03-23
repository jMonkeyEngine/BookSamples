package chapter04;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This example demonstrates saving and loading models using .j3o files.
 * Everytime you run this, a new model is loaded into the scene.
 * When you press ESCAPE (stop() method), the scene is saved and reloaded
 * until you delete "SavedGames/savedgame.j3o" in your home directory.
 * Use this method to save/load the game, by saving/loading and the rootNode.
 */
public class SaveModel extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        String userHome = System.getProperty("user.home");
        assetManager.registerLocator(userHome, FileLocator.class);
        try {
            Node loadedNode = (Node) assetManager.loadModel(
                    "/SavedGames/savedgame.j3o");
            rootNode.attachChild(loadedNode);
        } catch (com.jme3.asset.AssetNotFoundException e) {
        }

        Spatial mymodel = assetManager.loadModel(
                "Models/MyModel/MyModel.j3o");
        mymodel.rotate(0, FastMath.nextRandomFloat() * FastMath.PI, 0);
        mymodel.move(
                FastMath.nextRandomFloat() * 10 - 5,
                FastMath.nextRandomFloat() * 10 - 5,
                FastMath.nextRandomFloat() * 10 - 5);
        rootNode.attachChild(mymodel);

        /** A white, directional light source */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, 0.5f, -0.5f)));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    }

    /* This method defines what to do when the user presses ESCAPE. 
     * It saves the scene in the user's home directory. 
     */
    @Override
    public void stop() {
        String userHome = System.getProperty("user.home");
        File file = new File(userHome + "/SavedGames/savedgame.j3o");
        BinaryExporter exporter = BinaryExporter.getInstance();
        try {
            exporter.save(rootNode, file);
        } catch (IOException ex) {
            Logger.getLogger(SaveModel.class.getName()).log(
                    Level.SEVERE, "Error: Failed to save game!", ex);
        }
        super.stop(); // continue quitting the game
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        SaveModel app = new SaveModel();
        app.start();

    }
}
