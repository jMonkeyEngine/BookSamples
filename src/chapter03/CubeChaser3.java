package chapter03;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * This example shows how a Control (CubeChaser3Control) 
 * moves certain cubes that the player looks at. 
 * The control is only attached to some cubes, only they are affected. 
 * Note that the simpleUpdate() method is unused, because
 * all updates happen in the CubeChaser3Control's update loop!
 */
public class CubeChaser3 extends SimpleApplication {

    private static Box mesh = new Box(1, 1, 1);
    
    @Override
    /** initialize the scene here */
    public void simpleInitApp() {
        flyCam.setMoveSpeed(100f);
        makeCubes(40);
    }

    private void makeCubes(int number) {
        for (int i = 0; i < number; i++) {
            // randomize 3D coordinates
            Vector3f loc = new Vector3f(
                    FastMath.nextRandomInt(-20, 20),
                    FastMath.nextRandomInt(-20, 20),
                    FastMath.nextRandomInt(-20, 20));
            Geometry geom = myBox("Cube" + i, loc, ColorRGBA.randomColor());
            // make 25% random cubes chasable by adding the Control
            if (FastMath.nextRandomInt(1, 4) == 4) {
                geom.addControl(new CubeChaser3Control(cam, rootNode));
            }
            rootNode.attachChild(geom);
        }
    }

    public Geometry myBox(String name, Vector3f loc, ColorRGBA color) {
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
        CubeChaser3 app = new CubeChaser3();
        app.start();
    }
}
