package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;

/**
 * The factory class initializes materials and shapes (meshes or models), 
 * and creates geometries from them. Only static properties are set here.
 * No interaction happens here, no variable userdata is assigned. 
 *
 * @author zathras
 */
public final class Factory {

    // Geometry size ratios 
    public static final float CREEP_RADIUS = 0.3f;
    public static final float TOWER_RADIUS = 0.3f;
    public static final float TOWER_HEIGHT = 2.0f;
    // CONFIGURABLE TOWER CHARGES: SpeedDamage, HealthDamage, AmmoNum, BlastRange
    public static final float[] GATLING = {0.0f, -1f, 6, 0f};
    public static final float[] FREEZE  = {-1f,  -1f, 3, 0f};
    public static final float[] NUKE    = {.5f,  -5f, 1, 2f};
    // assetmanager
    private AssetManager assetManager;
    // materials
    private Material creep_mat;
    private Material floor_mat;
    private Material playerbase_mat;
    private Material tower_sel_mat;
    private Material tower_std_mat;

    public Factory(AssetManager as) {
        this.assetManager = as;
        initMaterials();
    }

    /**
     * ---------------------------------------------------------
     */
    public Node makePlayerBase() {
        Node playerbase_node = new Node("PlayerBaseNode");
        // player base geometry
        Box b2 = new Box(1.5f, .8f, 1f);
        Geometry playerbase_geo = new Geometry("Playerbase", b2);
        playerbase_geo.setMaterial(playerbase_mat);
        playerbase_geo.move(0, .8f, -1f);
        playerbase_node.attachChild(playerbase_geo);

        // floor geometry
        Node floor_node = new Node("Floor");
        Box b = new Box(33f, 0.1f, 33f);
        Geometry floor = new Geometry("Floor", b);
        floor.setMaterial(floor_mat);
        floor.setLocalTranslation(0, -8f, 0);
        floor_node.attachChild(floor);

        // Add floor and player base nodes to rootNode
        playerbase_node.attachChild(floor_node);
        return playerbase_node;
    }

    /**
     * Creates one tower geometry at the origin.
     */
    public Geometry makeTower(int index) {
        Box tower_shape = new Box(
                TOWER_RADIUS,
                TOWER_HEIGHT * .5f,
                TOWER_RADIUS);
        Geometry tower_geo = new Geometry("tower-" + index, tower_shape);
        tower_geo.setMaterial(tower_std_mat);
        tower_geo.setUserData("towerHeight", TOWER_HEIGHT);
        tower_geo.setUserData("selectedMaterial", tower_sel_mat);
        tower_geo.setUserData("standardMaterial", tower_std_mat);
        return tower_geo;
    }

    /**
     * ---------------------------------------------------------
     */
    /**
     * Creates one creep geometry at the origin
     */
    public Geometry makeCreep(Vector3f loc, int index) {
        Dome creep_shape = new Dome(Vector3f.ZERO,
                10, 10, CREEP_RADIUS, false);
        Geometry creep_geo = new Geometry("Creep-" + index, creep_shape);
        creep_geo.setMaterial(creep_mat);
        creep_geo.setLocalTranslation(loc);
        return creep_geo;
    }

    /**
     * --------------------------------------------------------------------
     */
    
    private Geometry getChargeGeometry(Material mat) {
        Sphere dot = new Sphere(10, 10, .1f);
        Geometry chargeMarker_geo = new Geometry("ChargeMarker", dot);
        chargeMarker_geo.setMaterial(mat);
        return chargeMarker_geo;
    }
    
    /**
     * Freeze charges slow down the target and do a bit of damage.
     */
    public Charge getFreezeCharge() {
        Material beam_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        beam_mat.setColor("Color", ColorRGBA.Cyan);
        return new Charge(FREEZE, getChargeGeometry(beam_mat));
    }

    /**
     * Gatling charges do minimal damage but they can be shot more often per
     * round and at various targets.
     */
    public Charge getGatlingCharge() {
        Material beam_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        beam_mat.setColor("Color", ColorRGBA.Yellow);
        return new Charge(GATLING, getChargeGeometry(beam_mat));
    }

    /**
     * Nuke charges do a lot of damage but they are expensive (only one shot per
     * charge). As a side effect they not only damage but also thaw/accelerate
     * the neighbouring creeps!
     */
    public Charge getNukeCharge() {
        Material beam_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        beam_mat.setColor("Color", ColorRGBA.Red);
        return new Charge(NUKE, getChargeGeometry(beam_mat));
    }

    /**
     * ---------------------------------------------------------
     */
    private void initMaterials() {
        // creep material
        creep_mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        creep_mat.setColor("Diffuse", ColorRGBA.Black);
        creep_mat.setColor("Ambient", ColorRGBA.Black);
        creep_mat.setBoolean("UseMaterialColors", true);
        // floor material
        floor_mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        floor_mat.setColor("Diffuse", ColorRGBA.White);
        floor_mat.setColor("Ambient", ColorRGBA.White);
        floor_mat.setBoolean("UseMaterialColors", true);
        // player material
        playerbase_mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        playerbase_mat.setColor("Diffuse", ColorRGBA.Yellow);
        playerbase_mat.setColor("Ambient", ColorRGBA.Yellow);
        playerbase_mat.setBoolean("UseMaterialColors", true);
        // tower SelectedMaterial
        tower_sel_mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        tower_sel_mat.setColor("Diffuse", ColorRGBA.Green.mult(.75f));
        tower_sel_mat.setColor("Ambient", ColorRGBA.Green.mult(.75f));
        tower_sel_mat.setBoolean("UseMaterialColors", true);
        //tower StandardMaterial
        tower_std_mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        tower_std_mat.setColor("Diffuse", ColorRGBA.Green);
        tower_std_mat.setColor("Ambient", ColorRGBA.Green);
        tower_std_mat.setBoolean("UseMaterialColors", true);
    }
}
