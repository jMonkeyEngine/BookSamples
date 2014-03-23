package chapter03;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;

/** 
 * A template how to create an AppState by extending AbstractAppState.
 * An AppState responds to and controls a subset of application-wide events:
 * This example simply changes a game state (the background color) 
 * depending on a changing parameter (the camera position). 
 * Usage: Attach the state to the stateManager 
 * of any SimpleApplication's simpleInitApp() method:
 *    stateManager.attach( new MyAppState() );
 */
public class MyAppState extends AbstractAppState {

    private ViewPort viewPort;

    @Override
    public void update(float tpf) {
        Vector3f v = viewPort.getCamera().getLocation();
        ColorRGBA color = new ColorRGBA(
                v.getX() / 10,
                v.getY() / 10,
                v.getZ() / 10, 1);
        viewPort.setBackgroundColor(color);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.viewPort = ((SimpleApplication)app).getViewPort();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        viewPort.setBackgroundColor(ColorRGBA.Black);
    }

}