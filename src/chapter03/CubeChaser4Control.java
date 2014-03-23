package chapter03;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 * Run this example by running CubeChaser4.
 * This class works together with the CubeChaser4State and CubeChaser4. 
 * This control uses the same code as in CubeChaser2's simpleUpdate() loop 
 * to manipulate the locations of the cube the player looks at.
 * The control adds an attribute to spatials (spatial can spin), 
 * the AppState defines the actual behavior (spatial spins).
 */
public class CubeChaser4Control extends AbstractControl {

    public CubeChaser4Control() {}

    @Override
    protected void controlUpdate(float tpf) {
         spatial.rotate(tpf, tpf, tpf);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String hello(){
        return "Hello, my name is "+spatial.getName();
    }
}
