package mygame;

import com.jme3.export.Savable;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Line;
import java.util.ArrayList;
import java.util.List;

/**
 * Tower has charges. It regularly fires at all creeps nearby until out of ammo.
 * Its current Charge status is visualized by colored spheres (ChargeMarkers).
 * In each level, a tower can have level+1 Charges.
 */
public class TowerControl extends AbstractControl implements Savable, Cloneable {

    private List<Charge> charges = new ArrayList<Charge>();
    private float timer = 0f;
    private GamePlayAppState game;

    public TowerControl(GamePlayAppState game) {
        this.game = game;
    }

    @Override
    protected void controlUpdate(float tpf) {
        timer += tpf;
        /* Test whether this tower is loaded with charges. */
        if (getChargeNum() > 0) {
            // Tower is loaded: Tower can attack.
            /* Load the cannon with the first charge */
            Charge charge = popNextCharge();
            /* Find creeps */
            List<CreepControl> reachable = new ArrayList<CreepControl>();
            List<Spatial> creeps = game.getCreeps();
            for (Spatial creep_geo : creeps) {
                CreepControl creep = creep_geo.getControl(CreepControl.class);
                // Find reachable ones
                // Depends on creep's distance to tower top versus range of charge:
                if (creep.isAlive()
                        && getTowerTop().distance(creep.getLoc()) < getHeight() * 2f) { // RANGE
                    reachable.add(creep);
                }
            }
            /* If the loaded tower can reach at least one creep, then... */
            if (reachable.size() > 0 && charge.getAmmoNum() > 0) {
                /* ... shoot once at each reachable creep. */
                for (CreepControl creep : reachable) {
                    if (timer > .01f) { // TIMER
                    /* Show a laser beam from tower to the creep that got hit. 
                         * The laser visuals are slightly random. */
                        Vector3f hit = creep.getLoc();
                        Line beam = new Line(
                                getTowerTop(),
                                new Vector3f(
                                hit.x + FastMath.rand.nextFloat() / 10f,
                                hit.y + FastMath.rand.nextFloat() / 10f,
                                hit.z + FastMath.rand.nextFloat() / 10f));
                        Geometry beam_geo = new Geometry("Beam", beam);
                        beam_geo.setMaterial(charge.getBeamMaterial());
                        game.addBeam(beam_geo);
                        // The laser beam has an effect on the creep
                        applyDamage(creep, charge);
                        // Shooting uses up 1 unit of ammo in this charge
                        charge.addAmmo(-1);
                        if (charge.getAmmoNum() <= 0) {
                            // this charge is out of ammo, discard the charge.
                            removeCharge(charge);
                            // pause to reload, that is, stop shooting until next turn.
                            break;
                        }
                        break;
                    }
                }
            timer = 0f;
            }
        }
    }

    /**
     * ---------------------------------------
     */
    /**
     * All towers do some damage to target. Freeze towers additionally slow
     * target down. 
     * A nuke charge deals additional blast damage to neighbours of the target,
     * but it also increases their speed ("thaws and recharges them").
     */
    private void applyDamage(CreepControl creep, Charge charge) {
        List<Spatial> creeps = game.getCreeps();
        creep.addSpeed(charge.getSpeedImpact());
        creep.addHealth(charge.getHealthImpact());
        // blast neighbours
        for (Spatial neighbour : creeps) {
            float dist = neighbour.getLocalTranslation().distance(creep.getLoc());
            if (dist < charge.getBlastRange() && dist > 0f) {
                neighbour.getControl(CreepControl.class).addHealth(charge.getHealthImpact() / 2f);
                neighbour.getControl(CreepControl.class).addSpeed(charge.getSpeedImpact() / 3);
            }
        }
    }

    public void addCharge(Charge charge) {
        charges.add(charge);
        Vector3f loc = spatial.getLocalTranslation();
        Spatial chargeMarker_geo = charge.getGeometry();
        int offset_x = (getIndex() % 2 == 0 ? 1 : -1);
        chargeMarker_geo.setLocalTranslation(
                loc.x - (offset_x * 0.33f),
                loc.y - ((charges.size() - 1) * .25f) + 1,
                loc.z);
        game.addChargeMarker(charge.getGeometry());
    }

    public void removeCharge(Charge charge) {
        charges.remove(charge);
        game.removeChargeMarker(charge.getGeometry());
    }

    public int getChargeNum() {
        return charges.size();
    }

    /**
     * returns the first Charge (FIFO?) and pops it from the queue
     */
    public Charge popNextCharge() {
        return charges.get(0);
    }

    /**
     * ---------------------------------------
     */
    public float getHeight() {
        return (Float) spatial.getUserData("towerHeight");
    }

    public int getIndex() {
        return (Integer) spatial.getUserData("index");
    }

    /**
     * The location and height of the tower is needed to calculate range.
     *
     * @return loc the coordinates of the top of this tower.
     */
    public Vector3f getTowerTop() {
        Vector3f loc = getLoc();
        return new Vector3f(loc.x, loc.y + getHeight() / 2, loc.z);
    }

    public Vector3f getLoc() {
        return spatial.getLocalTranslation();
    }

    /**
     * ---------------------------------------
     */
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
