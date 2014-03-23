package chapter03;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * This example shows how use Controls to change 
 * the behaviour of geometries. Here we make three cubes rotate.
 */
public class MySpinningCubes extends SimpleApplication {

    private Geometry cube1, cube2, cube3;

    @Override
    public void simpleInitApp() {
        cube1 = myBox("Cube 1", new Vector3f(3, 0, 0), ColorRGBA.Red);
        cube2 = myBox("Cube 2", new Vector3f(0, 0, 0), ColorRGBA.Green);
        cube3 = myBox("Cube 3", new Vector3f(-3, 0, 0), ColorRGBA.Blue);
        rootNode.attachChild(cube1);
        rootNode.attachChild(cube2);
        rootNode.attachChild(cube3);
        cube1.addControl(new MyControl());
        cube2.addControl(new MyControl());
        cube3.addControl(new MyControl());
    }

    public Geometry myBox(String name, Vector3f loc, ColorRGBA color) {
        Box mesh = new Box(1, 1, 1);
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);
        return geom;
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        MySpinningCubes app = new MySpinningCubes();
        app.start();
    }
}
