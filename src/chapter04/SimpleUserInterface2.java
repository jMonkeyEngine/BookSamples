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
 * The sample interface demos an opaque image frame at the bottom, 
 * a partially transparent icon that updates, and text that updates.
 * The context-sensitive Monkey icon is happy if the camera is close to, 
 * and sad if further away from the center of the scene.
 */
public class SimpleUserInterface2 extends SimpleApplication {

    
    private float distance = 0;
    private BitmapText distanceText;
    private Picture logo;

    @Override
    public void simpleInitApp() {
        // deactivate default statistics displays
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

        // Display a line of text with a default font on depth layer 0
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        distanceText = new BitmapText(guiFont);
        distanceText.setSize(guiFont.getCharSet().getRenderedSize());
        distanceText.move(
                settings.getWidth() / 2 + 50, distanceText.getLineHeight() + 20,
                0); // x,y coordinates, and depth layer 0
        guiNode.attachChild(distanceText);

        // Display a 2D image or icon on depth layer -2
        Picture frame = new Picture("User interface frame");
        frame.setImage(assetManager, "Interface/frame.png", false);
        frame.move(settings.getWidth() / 2 - 265, 0, -2);
        frame.setWidth(530);
        frame.setHeight(10);
        guiNode.attachChild(frame);

        // Display a 2D image or icon on depth layer -1
        logo = new Picture("logo");
        logo.setImage(assetManager, "Interface/chimpanzee-sad.gif", true);
        logo.move(settings.getWidth() / 2 - 47, 2, -1);
        logo.setWidth(46);
        logo.setHeight(38);
        guiNode.attachChild(logo);
    }

    @Override
    /** (optional) Interact with update loop here */
    public void simpleUpdate(float tpf) {
        distance = Vector3f.ZERO.distance(cam.getLocation());
        distanceText.setText("Distance: " + distance); // update the display

        // change the GUI icon depending on distance to the center of the scene
        if (distance < 10f) {
            logo.setImage(assetManager, "Interface/chimpanzee-smile.gif", true);
        } else {
            logo.setImage(assetManager, "Interface/chimpanzee-sad.gif", true);
        }
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        SimpleUserInterface2 app = new SimpleUserInterface2();
        app.start();
    }
}
