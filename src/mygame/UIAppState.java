package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *
 * @author normenhansen
 */
public class UIAppState extends AbstractAppState {

    private AppStateManager stateManager;
    private final String infoString = "Click tower to select,"
            + " press N(uke) / G(atling) / F(reeze) to load charges.";
    private BitmapText hudText;  // HUD displays score
    private BitmapText infoText; // HUD displays instructions
    private Node guiNode;
    private BitmapFont guiFont;

    public UIAppState(Node guiNode, BitmapFont guiFont) {
        super();
        this.guiNode = guiNode;
        this.guiFont = guiFont;
        infoText = new BitmapText(guiFont, false);
        hudText = new BitmapText(guiFont, false);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        Camera cam = app.getCamera();

        // Info: Display static playing instructions
        int screenHeight = cam.getHeight();
        float lineHeight = infoText.getLineHeight();
        infoText.setSize(guiFont.getCharSet().getRenderedSize());
        infoText.setColor(ColorRGBA.Blue);
        infoText.setLocalTranslation(0, screenHeight, 0);
        infoText.setText(infoString);
        guiNode.attachChild(infoText);

        // Score: this will later display health and budget
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        hudText.setColor(ColorRGBA.Blue);
        hudText.setLocalTranslation(0, screenHeight - lineHeight, 0);
        hudText.setText("");
        guiNode.attachChild(hudText);
    }

    @Override
    public void cleanup() {
        guiNode.detachChild(infoText);
        guiNode.detachChild(hudText);
        super.cleanup();
    }

    public void setInfoText(String text) {
        infoText.setText(text);
    }

    public void updateGameStateDisplay(GamePlayAppState game) {
        // Update health/budget display:
        String score = "Level: " + game.getLevel()
                + ", Budget: " + game.getBudget()
                + ", Health: " + game.getHealth() +"\n";

        // Test whether player wins or loses
        if (game.getHealth() <= 0) {
            hudText.setText(score + "YOU LOSE.");
        } else if ((game.getCreepNum() == 0) && game.getHealth() > 0) {
            hudText.setText(score + "YOU WIN!");
        } else {
            // Otherwise display default text, battle is ongoing.
            hudText.setText(score + "GO GO GO!");
        }
    }
    
    @Override
    public void update(float tpf) {
        // automatically detect attached game and display stats
        GamePlayAppState game = stateManager.getState(GamePlayAppState.class);
        if (game != null) {
            infoText.setText(infoString);
            updateGameStateDisplay(game);
        }else{
            infoText.setText("Press RETURN to continue");
        }
    }

}
