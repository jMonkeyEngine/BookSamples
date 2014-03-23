package chapter02;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 * This example shows how to configure app settings before the application starts.
 * Examples for AppSettings are fullscreen/windowed, color depth, resolution.
 */
public class AppSettingsDemo extends SimpleApplication {

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        // read GraphicsDevice attributes
        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
        
        // specify display settings based on DisplayMode
        AppSettings settings = new AppSettings(true);
        settings.setResolution(modes[0].getWidth(), modes[0].getHeight());
        settings.setFrequency(modes[0].getRefreshRate());
        settings.setDepthBits(modes[0].getBitDepth());
        settings.setFullscreen(device.isFullScreenSupported());
        settings.setTitle("My Cool Game"); // only visible if not fullscreen
        settings.setSamples(2);            // anti-aliasing
        
        // activate display settings and start app 
        AppSettingsDemo app = new AppSettingsDemo();
        app.setSettings(settings);         // apply settings to app
        app.setShowSettings(false);        // don't ask user for settings
        app.start();                       // use settings and run
    }

    @Override
    /** Initialize the scene here: 
     *  Create Geometries and attach them to the rootNode. */
    public void simpleInitApp() {
        setDisplayFps(false);                         // hide frames-per-sec display
        setDisplayStatView(false);                    // hide debug statistics display

        Box b = new Box(1, 1, 1);                     
        Geometry geom = new Geometry("Box", b);       
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md"); 
        mat.setColor("Color", ColorRGBA.Blue);        
        geom.setMaterial(mat);                        
        rootNode.attachChild(geom);                   // make geometry appear in scene
    }

}
