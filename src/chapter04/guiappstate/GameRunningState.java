package chapter04.guiappstate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
  * This demo package shows a game UI switcher with three AppStates. 
  * This state is active while the game is running and shows game content (rotating cube).
 */
public class GameRunningState extends AbstractAppState {

    private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private SimpleApplication app;
    private InputManager inputManager;
    private Node localRootNode = new Node("Game Screen RootNode");
    private Node localGuiNode = new Node("Game Screen GuiNode");
    private final ColorRGBA backgroundColor = ColorRGBA.Blue;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = (SimpleApplication) app;
        this.rootNode = this.app.getRootNode();
        this.viewPort = this.app.getViewPort();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();

        inputManager.setCursorVisible(true);
        this.app.getFlyByCamera().setDragToRotate(false);
        
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);

        viewPort.setBackgroundColor(backgroundColor);

        /** Load this scene */
        viewPort.setBackgroundColor(backgroundColor);

        Box mesh = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat);
        geom.setLocalTranslation(1, 0, 0);
        localRootNode.attachChild(geom);

        /** Load the HUD*/
        BitmapFont guiFont = assetManager.loadFont(
                "Interface/Fonts/Default.fnt");
        BitmapText displaytext = new BitmapText(guiFont);
        displaytext.setSize(guiFont.getCharSet().getRenderedSize());
        displaytext.move(10, displaytext.getLineHeight() + 20, 0);
        displaytext.setText("Game running. Press BACKSPACE to pause and return to the start screen.");
        localGuiNode.attachChild(displaytext);
    }

    @Override
    public void update(float tpf) {
        /** the action happens here */
        Vector3f v = viewPort.getCamera().getLocation();
        viewPort.setBackgroundColor(new ColorRGBA(v.getX() / 10, v.getY() / 10, v.getZ() / 10, 1));
        rootNode.getChild("Box").rotate(tpf, tpf, tpf);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        rootNode.detachChild(localRootNode);
        guiNode.detachChild(localGuiNode);
    }
}