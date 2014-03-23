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
 * A user interface that demnos the difference between 2D guiNode and 3D rootNode.
 * What happens if we attach a 3D cube to the 2D user interface?
 * And what if we apply 3D transformations (rotation) to this 3D cube? 
 */
public class SimpleUserInterface3 extends SimpleApplication {
    private float distance=0;
    private BitmapText distanceText;
    Geometry minicube;

    @Override
    /** initialize the scene here */
    public void simpleInitApp() {
        // deactivate default statistics displays
        setDisplayStatView(false);
        setDisplayFps(false);
        
        // A big blue 3D cube attached to the rootNode
        Box mesh = new Box(1, 1, 1);   
        Geometry geom = new Geometry("3D scene box", mesh);
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md"); 
        mat.setColor("Color", ColorRGBA.Blue);        
        geom.setMaterial(mat);                        
        rootNode.attachChild(geom);
        
        // Attach 3D red cube to the 2D guiNode (instead of rootNode)!
        minicube = new Geometry("2D GUI box", mesh);  
        Material mat2 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md"); 
        mat2.setColor("Color", ColorRGBA.Red);        
        minicube.setMaterial(mat2);                   
        minicube.scale(10f); // increae size because GUI is in pixels, not WU. 
        minicube.setLocalTranslation(settings.getWidth()/2-80, 45, 0);
        guiNode.attachChild(minicube);

        
        // Display a line of text with a default font on depth layer 0
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        distanceText = new BitmapText(guiFont);
        distanceText.setSize(guiFont.getCharSet().getRenderedSize());
        distanceText.move(
                settings.getWidth()/2+50, distanceText.getLineHeight()+20, 
                0); // x,y coordinates, and depth layer 0
        guiNode.attachChild(distanceText);
        
        // Display a 2D image or icon on depth layer -2
        Picture frame = new Picture("User interface frame");
        frame.setImage(assetManager, "Interface/frame.png", false);
        frame.move(settings.getWidth()/2-265, 0, -2);
        frame.setWidth(530);
        frame.setHeight(10);
        guiNode.attachChild(frame);
        
        // Display a 2D image or icon on depth layer -1
        Picture logo = new Picture("logo");
        logo.setImage(assetManager, "Interface/Monkey.png", true);
        logo.move(settings.getWidth()/2-47, 2, -1); 
        logo.setWidth(95);
        logo.setHeight(75);
        guiNode.attachChild(logo); 
    }

    @Override
    /** (optional) Interact with update loop here */
    public void simpleUpdate(float tpf) {
        distance = Vector3f.ZERO.distance(cam.getLocation());   
        distanceText.setText("Distance: "+distance); // update the display
        minicube.rotate(tpf, tpf, tpf);
    } 

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        SimpleUserInterface3 app = new SimpleUserInterface3();
        app.start();
    }
}
