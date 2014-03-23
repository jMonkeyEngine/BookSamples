package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.logging.Level;

/**
 * In this tower defense game, the creeps plan a kamikaze attack 
 * against the player base. The user has to recharge defense towers 
 * with different types of ammo to stop them. <br />
 * The player base is at the origin of the scene. In front of the player base, 
 * along the positive z axis, there's a valley. Player-controlled towers 
 * stand to the left and right of the valley. 
 * A pack of creeps appears at the far end of the positive z axis and 
 * runs down through the valley towards the player base.<br />
 * <ul>
 * <li>Main: Inits UI, light, input handling (selecting Towers, loading Charges), 
 * starts and ends the game</li>
 * <li>Factory: Generates static spatials and Charges.</li>
 * <li>GamePlayAppState: Initializes the scene, runs the main event loop.</li>
 * <li>PlayerBaseControl: Manages score, level, player health, player budget. </li>
 * <li>CreepControl: Manages creep health and speed; 
 *     automatically moves Creeps towards playerbase; 
 *     if Creep reaches playerbase, it decreases the player health.</li>
 * <li>TowerControl: Manages loaded Charges; 
 *     if loaded, shoots Charges at closest Creeps automatically.</li>
 * <li>Charge: Contains sets of ammo that modify speed and health 
 *     of Creeps on impact.</li>
 * </ul>
 * @author zathras
 */
public class Main extends SimpleApplication {

    // Factory creates playerbase, creeps, towers, and charges for a level.
    Factory factory;
    // GUI and input handling 
    private int selected = -1;   // tracks which tower the player has selected 
    private GamePlayAppState game;
    private UIAppState ui;

    /**
     * --------------------------------------------------------------
     */
    // create a new instance of this application
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    // initialize the application
    @Override
    public void simpleInitApp() {
        // configure the jme app including camera and background color
        java.util.logging.Logger.getLogger("").setLevel(Level.WARNING);
        setDisplayStatView(false); // don't show debugger
        viewPort.setBackgroundColor(ColorRGBA.White);
        // 3rd person camera view from above
        cam.setLocation(new Vector3f(0, 8, 18f));
        cam.lookAt(new Vector3f(0, 0, 6f), Vector3f.UNIT_Y);
        flyCam.setEnabled(false);

        // add light to scene
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0.8f, -0.7f, -1));
        sun.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(sun);

        selected = -1;

        factory = new Factory(assetManager);
        // create AppStates and attach ui state already
        ui = new UIAppState(guiNode, guiFont);
        game = new GamePlayAppState(rootNode, factory);
        stateManager.attach(ui);

        // initialize the scene graph
        initInputs(); // activate input handling
        startGame(1); // start game with level 1
    }

    /** This loop is empty! We moved everything cleanly 
      * into AppStates and Controls. */
    @Override
    public void simpleUpdate(float tpf) { }
    

    /**
     * --------------------------------------------------------------
     */
    private void initInputs() {
        //flyCam.setMoveSpeed(25);
        // user needs to see mouse pointer to be able to click
        inputManager.setCursorVisible(true);
        // configure input mappings
        inputManager.addMapping("Restart", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("Select", new MouseButtonTrigger(0)); // click
        inputManager.addMapping("LoadFreezeCharge", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("LoadNukeCharge", new KeyTrigger(KeyInput.KEY_N));
        inputManager.addMapping("LoadGatlingCharge", new KeyTrigger(KeyInput.KEY_G));
        // register to listener
        inputManager.addListener(actionListener,
                "Restart", "Select", "Quit",
                "LoadGatlingCharge", "LoadNukeCharge", "LoadFreezeCharge");
    }
    /**
     * Input handling<br />
     * You left-click to select one Tower and deselect the previous one.
     * You press keys (F / G / N) to assign Charges to the selected Tower.
     * You can only assign Charges if the player has budget.
     * A tower can have zero to 'level+1' Charges assigned.
     */
    private ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(String mapping, boolean keyDown, float tpf) {
            if (stateManager.hasState(game)) {
                // A player clicks to select a tower.
                if (mapping.equals("Select") && !keyDown) {
                    // Deselect previously selected tower if applicable
                    if (selected != -1) {
                        Spatial prevTower = (rootNode.getChild("tower-" + selected));
                        prevTower.setMaterial((Material) prevTower.getUserData("standardMaterial"));
                    }
                    // Determine the coordinate where user clicked
                    CollisionResults results = new CollisionResults();
                    Vector2f click2d = inputManager.getCursorPosition();
                    Vector3f click3d = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 0f).clone();
                    Vector3f dir = cam.getWorldCoordinates(
                            new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d);
                    // Cast invisible ray from click coord foward and intersect with towers
                    Ray ray = new Ray(click3d, dir);
                    rootNode.getChild("TowerNode").collideWith(ray, results);
                    // Determine what the user selected
                    if (results.size() > 0) {
                        // Ray collides with tower: Player has selected a tower
                        CollisionResult closest = results.getClosestCollision();
                        selected = closest.getGeometry().getControl(TowerControl.class).getIndex();
                        Spatial selectedTower = rootNode.getChild("tower-" + selected);
                        selectedTower.setMaterial((Material) selectedTower.getUserData("selectedMaterial"));
                    } else {
                        // Ray misses towers: Player has selected nothing
                        selected = -1;
                    }
                }
                // If a tower is selected and user presses keys, then load a Charge:
                // Add new Charge only if player has budget and if tower is not full yet 
                // (max number of Charges per tower is 'level+1')!
                if (selected != -1 && game.getBudget() > 0 && !keyDown) {
                    TowerControl selectedTower =
                            rootNode.getChild("tower-" + selected).getControl(TowerControl.class);
                    if (selectedTower.getChargeNum() <= game.getLevel()) {
                        // Selected tower is not full yet: Load one more Charge
                        if (mapping.equals("LoadFreezeCharge")) {
                            selectedTower.addCharge(factory.getFreezeCharge());
                            game.addBudgetMod(-1);
                        } else if (mapping.equals("LoadNukeCharge")) {
                            selectedTower.addCharge(factory.getNukeCharge());
                            game.addBudgetMod(-1);
                        } else if (mapping.equals("LoadGatlingCharge")) {
                            selectedTower.addCharge(factory.getGatlingCharge());
                            game.addBudgetMod(-1);
                        }
                    }
                }
            } else {
                // Game is paused: ignore game input, only test for restarting.
                if (mapping.equals("Restart") && !keyDown) {
                    if (game.isLastGameWon()) {
                        // if last game won, then next level
                        startGame(game.getLevel() + 1);
                    } else {
                        // if last game lost, then restart from level 1
                        startGame(1);
                    }
                }
            }
            if (mapping.equals("Quit") && !keyDown) {
                endGame();
                stop();
            }
        }
    };

    /**
     * --------------------------------------------------------------
     */
    /**
     * Reset everything and create the next scene
     */
    private void startGame(int level) {
        game.setLevel(level);
        stateManager.attach(game);
        selected = -1;
    }

    /**
     * Clean up: pause the game, stop all creeps, remove all beams when game
     * ends
     */
    private void endGame() {
        stateManager.detach(game);
    }

}
