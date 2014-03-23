package chapter03;

import com.jme3.app.SimpleApplication;

/**
 * This demo combines an AppState and a Control to change the locations 
 * of certain CubeChaserControl'ed cubes that the player looks at. 
 * Note that the simpleUpdate() method is empty on purpose, 
 * all updates happen now in the AppState's update() method!
 */
public class CubeChaser4 extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(100f);
        /** Note: All real code was moved into the AppStates! */
        CubeChaser4State state = new CubeChaser4State();
        stateManager.attach(state);
    }

    @Override
    public void simpleUpdate(float tpf) {
        /** Note: All real code was moved into an AppState! */
    }

    /** Start the jMonkeyEngine application */
    public static void main(String[] args) {
        CubeChaser4 app = new CubeChaser4();
        app.start();
    }
}
