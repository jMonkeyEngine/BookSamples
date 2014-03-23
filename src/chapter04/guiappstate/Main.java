package chapter04.guiappstate;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;

/**
 * This demo package shows a user interface switcher with three AppStates. 
 * Instead of game content, it just displays a different cube for each state.
 * <ul>
 * <li>StartScreenState: This state is enabled when the user starts 
 *     the application, or when the game is paused. 
 *     Press BACKSPACE to return to the game, press RETURN to go to Settings.</li>
 * <li>GameRunningState: This state shows the game content and 
 *     is enabled while the game is running. 
 *     Press BACKSPACE to pause the game and return to the start screen.</li>
 * <li>SettingsScreenState: The Settings screen can only be opened from the Start screen.
 *     Press RETURN to toggle it on and off.</li>
 * </ul>
 */
public class Main extends SimpleApplication {

  private Trigger pause_trigger = new KeyTrigger(KeyInput.KEY_BACK);
  private Trigger save_trigger = new KeyTrigger(KeyInput.KEY_RETURN);
  private boolean isRunning = false; // it starts at startscreen
  private GameRunningState gameRunningState;
  private StartScreenState startScreenState;
  private SettingsScreenState settingsScreenState;

  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
    Main app = new Main();
    app.start();
  }

  /** initialize the scene here */
  @Override
  public void simpleInitApp() {
    setDisplayFps(false);         // hide FPS
    setDisplayStatView(false);    // hide debug statistics

    gameRunningState    = new GameRunningState();
    startScreenState    = new StartScreenState();
    settingsScreenState = new SettingsScreenState();

    stateManager.attach(startScreenState); // this is the first state

    inputManager.addMapping("Game Pause Unpause", pause_trigger);
    inputManager.addListener(actionListener, new String[]{"Game Pause Unpause"});
    inputManager.addMapping("Toggle Settings", save_trigger);
    inputManager.addListener(actionListener, new String[]{"Toggle Settings"});
  }
  
  /** Global inputs. 
   * You can also activate and deactivate inputs in each app state individually. */
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean isPressed, float tpf) {
      System.out.println("key" + name);
      if (name.equals("Game Pause Unpause") && !isPressed) {
        if (isRunning) {
          stateManager.detach(gameRunningState);
          stateManager.attach(startScreenState);
          System.out.println("switching to startscreen...");

        } else {
          stateManager.detach(startScreenState);
          stateManager.attach(gameRunningState);
          System.out.println("switching to game...");
        }
        isRunning = !isRunning;
      } else if (name.equals("Toggle Settings") && !isPressed && !isRunning) {
        if (!isRunning && stateManager.hasState(startScreenState)) {
          stateManager.detach(startScreenState);
          stateManager.attach(settingsScreenState);
          System.out.println("switching to settings...");
        } else if (!isRunning && stateManager.hasState(settingsScreenState)) {
          stateManager.detach(settingsScreenState);
          stateManager.attach(startScreenState);
          System.out.println("switching to startscreen...");
        }
      }
    }
  };

  @Override
  public void simpleUpdate(float tpf) {}

}
 