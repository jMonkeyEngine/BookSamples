package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 * The GamePlayAppState class initializes the game. <br/> The number of elements
 * generated depends on the level. Init order: 1. playerbase, 2. creeps, 3.
 * towers. (Because towers depend on creeps (towers want to shoot at creeps);
 * and creeps depend on the playerbase (they want to attack the player)).<br/>
 * Here you also configure the game mechanic factors to make the game more
 * balanced: initial numbers of towers and creeps, initial speed of creeps,
 * initial health of creeps and player, initial budget of player, as well as
 * effects of tower charges (effects are "added" to creep health/speed when the
 * charge hits).
 *
 * @author normenhansen
 */
public class GamePlayAppState extends AbstractAppState {

    private AppStateManager stateManager;
    private final Node rootNode;
    // nodes
    private Node playerBase;
    private final Node creepNode;
    private final Node towerNode;
    private final Node beamNode;
    private final Node chargeMarkerNode;
    // Factory creates playerbase, creeps, towers, and charges for a level.
    Factory factory;
    // timers reset laser beam visualizations and dispense player budget
    private float timer_beam = 0f;
    private float timer_budget = 0f;
    // player control manages player health and budget
    private int level = 0;
    private int score = 0;
    private float health = 0;
    private int budget = 0;
    private boolean lastGameWon = false;
    private int CREEP_INIT_NUM;
    private int TOWER_INIT_NUM;
    private float CREEP_INIT_HEALTH;
    private float CREEP_INIT_SPEED;

    public GamePlayAppState(Node rootNode, Factory factory) {
        this.rootNode = rootNode;
        this.factory = factory;
        playerBase = factory.makePlayerBase();
        creepNode = new Node("CreepNode");
        towerNode = new Node("TowerNode");
        beamNode = new Node("BeamNode");
        chargeMarkerNode = new Node("ChargeMarkerNode");
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        // configurable factors depend on level
        this.budget = 5 + level * 2;
        this.health = 2f + level;
        this.CREEP_INIT_NUM = 2 + level * 2;
        this.TOWER_INIT_NUM = 4 + level / 2;
        this.CREEP_INIT_HEALTH = 20f + level * 2;
        this.CREEP_INIT_SPEED = 0.5f + level / 10;
        rootNode.attachChild(playerBase);
        rootNode.attachChild(creepNode);
        rootNode.attachChild(towerNode);
        rootNode.attachChild(beamNode);
        rootNode.attachChild(chargeMarkerNode);
        addCreeps();
        addTowers();
    }

    @Override
    public void cleanup() {
        creepNode.detachAllChildren();
        towerNode.detachAllChildren();
        beamNode.detachAllChildren();
        chargeMarkerNode.detachAllChildren();
        rootNode.detachChild(playerBase);
        rootNode.detachChild(creepNode);
        rootNode.detachChild(towerNode);
        rootNode.detachChild(beamNode);
        rootNode.detachChild(chargeMarkerNode);
        super.cleanup();
    }

    /**
     * ---------------------------------------------------------
     */
    /**
     * Towers stand in two rows to the left and right of the positive z axis
     * along the "tower-protected valley". They shoot beams at creeps.
     */
    private void addTowers() {
        // Generate a series of towers along the sides of the valley
        for (int index = 0; index < TOWER_INIT_NUM; index++) {
            // Distribute towers to left and right of valley along positive z axis
            int leftOrRight = (index % 2 == 0 ? 1 : -1); // -1 or +1
            float offset_x = leftOrRight * 2.5f;
            float offset_y = Factory.TOWER_HEIGHT * .5f;
            float offset_z = index + 2;
            Vector3f loc = new Vector3f(offset_x, offset_y, offset_z);
            // tower geo
            Geometry tower_geo = factory.makeTower(index);
            tower_geo.setLocalTranslation(loc);
            // data is stored per tower geo, no data in the towernode.
            tower_geo.setUserData("index", index);
            tower_geo.setUserData("chargesNum", 0);
            tower_geo.addControl(new TowerControl(this));
            towerNode.attachChild(tower_geo);
        }
    }

    /**
     * Creeps start at a certain distance from the towers at a spawnloc around
     * the coordinate (+/- offset_x , + 1 , + offset_z). <ul> <li>offset_x is
     * random within the interval of the valley width. Can be positive or
     * negative to distribute creeps to the left and right of the positive z
     * axis. </li> <li>offset_z is the same for all creeps, they start in a row
     * ortogonal to the valley. distance increases each level, depending on the
     * number of towers, so that the creeps always start outside of the
     * tower-protected valley.</li> </ul>
     */
    private void addCreeps() {
        // generate a pack of creesp
        for (int index = 0; index < CREEP_INIT_NUM; index++) {
            // distribute creeps to the left and right of the positive x axis
            int leftOrRight = (index % 2 == 0 ? 1 : -1); // +1 or -1
            float offset_x = 1.75f * leftOrRight * FastMath.rand.nextFloat();
            float offset_y = 0;
            float offset_z = 2.5f * ((TOWER_INIT_NUM / 2f) + 6f);
            Vector3f spawnloc = new Vector3f(offset_x, offset_y, offset_z);
            // creep geometry
            Geometry creep_geo = factory.makeCreep(spawnloc, index);
            // data
            creep_geo.setUserData("index", index);
            creep_geo.setUserData("health", CREEP_INIT_HEALTH);
            creep_geo.setUserData("speed", CREEP_INIT_SPEED);
            creep_geo.addControl(new CreepControl(this));
            creepNode.attachChild(creep_geo);
        }
    }

    /**
     * --------------------------------------------------------------
     */
    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public boolean isLastGameWon() {
        return lastGameWon;
    }
    
    /**
     * Modifies the player score by adding to it.
     *
     * @param mod is (typically) a positive value added to the player score.
     */
    public void addScoreMod(int mod) {
        score += mod;
    }

    public float getHealth() {
        return Math.round(health * 10) / 10; // drop the decimals
    }

    /**
     * Modifies the player health by adding a positive or negative number to it.
     *
     * @param mod is a value added to the player budget.
     */
    public void addHealthMod(float mod) {
        health += mod;
    }

    public int getBudget() {
        return budget;
    }

    /**
     * Modifies the player budget by adding to it.
     *
     * @param mod is often a negative value substracted from the player budget.
     */
    public void addBudgetMod(int mod) {
        budget += mod;
    }

    /**
     * How many creeps are still in the game?
     */
    public int getCreepNum() {
        return creepNode.getChildren().size();
    }

    public List<Spatial> getCreeps() {
        return creepNode.getChildren();
    }

    public void addBeam(Geometry beam) {
        beamNode.attachChild(beam);
    }

    public void addChargeMarker(Spatial spat){
        chargeMarkerNode.attachChild(spat);
    }
    
    public void removeChargeMarker(Spatial spat){
        chargeMarkerNode.detachChild(spat);
    }
    
    /**
     * Do more than zero laser beams extend from any towers? Need to test this
     * to GC beam geometries every few seconds.
     */
    private Boolean thereAreBeams() {
        return beamNode.descendantMatches("Beam").size() > 0;
    }

    /**
     * GC the laser beam visualizations.
     */
    private void clearAllBeams() {
        beamNode.detachAllChildren();
    }

    /**
     * The main loop increases the player budget, displays health and budget,
     * and resets the laser beams. It also tests whether the player or the
     * creeps got killed, and determines winning or losing. <br /> Timers depend
     * on time-per-frame (tpf)
     */
    @Override
    public void update(float tpf) {
        // Player earns x coins every 10+x seconds per level x
        timer_budget += tpf;
        if (timer_budget > getLevel() + 10) {
            addBudgetMod(getLevel());
            timer_budget = 0;
        }

        // Reset all laserbeam visualizations and GC them every second
        timer_beam += tpf;
        if (timer_beam > 1f) {
            if (thereAreBeams()) {
                clearAllBeams();
            }
            timer_beam = 0;
        }

        // Test whether player wins or loses
        if (getHealth() <= 0) {
            lastGameWon = false;
            stateManager.detach(this);
        
        } else if ((getCreepNum() == 0) && getHealth() > 0) {
            lastGameWon = true;
            stateManager.detach(this);
        }
    }
}
