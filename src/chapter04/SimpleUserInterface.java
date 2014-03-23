package chapter04;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;

/**
 * Adding a 2D user interface with text and icons to the screen.
 * The example interface demos an opaque image frame at the bottom, 
 * a partially transparent icon, and text that updates live.
 */
public class SimpleUserInterface extends SimpleApplication {

    private float distance = 0;
    private BitmapText distanceText;

    @Override
    public void simpleInitApp() {
        // hide the statsview and FPS view
        setDisplayStatView(false);
        setDisplayFps(false);

        // just a blue box
        Box mesh = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        // Display a line of text in the default font on depth layer 0
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        distanceText = new BitmapText(guiFont);
        distanceText.setSize(guiFont.getCharSet().getRenderedSize());
        distanceText.move( // x/y coordinates and z = depth layer 0
                settings.getWidth() / 2 + 50,
                distanceText.getLineHeight() + 20,
                0);
        guiNode.attachChild(distanceText);

        // Display a 2D image or icon on depth layer -2
        Picture frame = new Picture("User interface frame");
        frame.setImage(assetManager, "Interface/frame.png", false);
        frame.move(settings.getWidth() / 2 - 265, 0, -2);
        frame.setWidth(530);
        frame.setHeight(10);
        guiNode.attachChild(frame);

        // Display a 2D image or icon on depth layer -1
        Picture logo = new Picture("logo");
        logo.setImage(assetManager, "Interface/Monkey.png", true);
        logo.move(settings.getWidth() / 2 - 47, 2, -1);
        logo.setWidth(95);
        logo.setHeight(75);
        guiNode.attachChild(logo);
    }

    @Override
    /** (optional) Interact with update loop here */
    public void simpleUpdate(float tpf) {
        distance = Vector3f.ZERO.distance(cam.getLocation());
        distanceText.setText("Distance: " + distance); // update the display
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        SimpleUserInterface app = new SimpleUserInterface();
        app.start();
    }
}
