package chapter11;

import com.jme3.app.SimpleApplication;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveAndLoad extends SimpleApplication {

    @Override
    public void simpleUpdate(float tpf) {
    }

    public static void main(String[] args) {
        SaveAndLoad app = new SaveAndLoad();
        app.start();
    }

    @Override
    public void stop() { // TODO
        String userHome = System.getProperty("user.home");
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(userHome + "/mycoolgame/savedgame.j3o");
        try {
            exporter.save(rootNode, file);
            Logger.getLogger(SaveAndLoad.class.getName()).log(Level.INFO,
                    "Success: Saved node");
        } catch (IOException ex) {
            Logger.getLogger(SaveAndLoad.class.getName()).log(Level.SEVERE,
                    "Warning: Failed to save node!", ex);
        }
        super.stop();
    }

    @Override
    public void simpleInitApp() {
        // add a random cube
        Box b = new Box(1, 1, 1);   
        Geometry geom = new Geometry("Box", b);    
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.randomColor());
        geom.setMaterial(mat);
        geom.move((FastMath.nextRandomFloat() * 10)-5,
                (FastMath.nextRandomFloat() * 10)-5,
                (FastMath.nextRandomFloat() * -10));
        rootNode.attachChild(geom);

        // add saved cubes
        String userHome = System.getProperty("user.home");
        BinaryImporter importer = BinaryImporter.getInstance();
        importer.setAssetManager(assetManager);
        try {
            File file = new File(userHome + "/mycoolgame/savedgame.j3o");
            Node sceneNode = (Node) importer.load(file);
            sceneNode.setName("My restored node");
            rootNode.attachChild(sceneNode);
            Logger.getLogger(SaveAndLoad.class.getName()).log(Level.INFO,
                    "Success: Loaded saved node.");
        } catch (IOException ex) {
            Logger.getLogger(SaveAndLoad.class.getName()).log(Level.INFO,
                    "Warning: Could not load saved node.", ex);
        }

    }
}