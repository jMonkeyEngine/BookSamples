package mygame;

import com.jme3.export.Savable;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 * Creeps manage their health and approach player base as long as they're alive.
 * They move forwards in a straight line; they can walk through each other.
 * Creeps substract player‘s health at arrival (kamikaze attack).
 *
 * @author zathras
 */
public class CreepControl extends AbstractControl implements Savable, Cloneable {

    private final float speed_min = 0.5f;
    private GamePlayAppState game;

    public CreepControl(GamePlayAppState game) {
        this.game = game;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (isAlive()) {
            // walk forward along z axis
            Vector3f newloc = new Vector3f(
                    spatial.getLocalTranslation().getX(),
                    spatial.getLocalTranslation().getY(),
                    spatial.getLocalTranslation().getZ()
                    - (getSpeed() * tpf * FastMath.rand.nextFloat()));
            if (newloc.z > 0) {
                // If creep has not yet reached player base at origin,
                // it keeps walking towards playerbase.
                setLoc(newloc);
            } else {
                // Creep has reached player base and attacks player.
                kamikaze();
            }
        } else {
            // Creep got killed by tower
            game.addBudgetMod(1); // increase player budget as reward.
            remove();             // remove me (this creep)
        }
    }

    /**
     * ----------------------------------------------
     */
    public void setHealth(float h) {
        spatial.setUserData("health", h);
    }

    public float getHealth() {
        return (Float) spatial.getUserData("health");
    }

    public Boolean isAlive() {
        return getHealth() > 0f;
    }

    /**
     * @param mod (typically) a negative number by how much to decrease the
     * creep's health.
     */
    public void addHealth(float mod) {
        setHealth(getHealth() + mod);
    }

    /**
     * Creep commits kamikaze when attacking the base. kamikaze damage depends
     * on creeps remaining health.
     */
    public void kamikaze() {
        game.addHealthMod(getHealth() / -10);
        setHealth(0f);
        remove();
    }

    /**
     * ----------------------------------------------
     */
    /**
     * @param mod (typically) a negative number by how much to decrease the
     * creep's speed.
     */
    public void addSpeed(float mod) {
        spatial.setUserData("speed", getSpeed() + mod);
        if (getSpeed() < speed_min) {
            spatial.setUserData("speed", speed_min);
        }
    }

    public float getSpeed() {
        return (Float) spatial.getUserData("speed");
    }

    /**
     * ----------------------------------------------
     */
    public void setLoc(Vector3f loc) {
        spatial.setLocalTranslation(loc);
    }

    public Vector3f getLoc() {
        return spatial.getLocalTranslation();
    }

    /**
     * ----------------------------------------------
     */
    public int getIndex() {
        return (Integer) spatial.getUserData("index");
    }

    public void remove() {
        spatial.removeFromParent();
        spatial.removeControl(this);
    }

    /**
     * ----------------------------------------------
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return this;
    }
}
