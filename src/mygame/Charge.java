package mygame;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import java.io.IOException;

/** 
 * This is one charge. One charge contains one or more shots of the same type.
 * Each Charge type does a certain combination of speed damage, 
 * health damage, and blast damage. Charges are created by the factory class
 * every time the user selects a tower and presses an action key.
 * A tower can hold several charges.
 * @author zathras
 */
public class Charge implements Savable {

  private float speedDamage;
  private float healthDamage;
  private int   ammoNum;
  private float blast;
  private Material beam_mat;
  private Geometry geometry;

  /** 
   * This creates one charge type.
   * @param s SpeedDamage is a negative number if this is a freezetower 
   * @param h HealthDamage is a negative number how much damage it deals per shot
   * @param a AmmoNum is a positive number how many shots are in one charge.
   * @param r Range is the distance how far the tower can shoot this charge.
   * @param b Blast is the distance how far away neighbouring creeps are also damaged
   *  (recommended: >1 for nuke, 1 for gatling, 0 for freeze).
   * @param m A colored material that is used for the laser beam.
   */
  public Charge(float s, float h, int a, float b, Geometry m) {
    this.speedDamage = s;
    this.healthDamage = h;
    this.ammoNum = a;
    this.blast = b;
    this.geometry = m;
    this.beam_mat = m.getMaterial();
  }

  /** 
   * This creates one charge type.
   * @param v A float array with values {SpeedDamage,HealthDamage,Ammo,Range,Blast}. 
   * @param m A colored material that is used for the laser beam.
   */
  public Charge(float[] v, Geometry m) {
    this.speedDamage = v[0];
    this.healthDamage = v[1];
    this.ammoNum = (int) v[2];
    this.blast = v[3];
    this.geometry = m;
    this.beam_mat = m.getMaterial();
  }

  /** How much Ammo this charge has*/
  public int getAmmoNum() {
    return ammoNum;
  }

  /** deplete ammo by subtracting (adding negative numbers) */
  public void addAmmo(int mod) {
    ammoNum += mod;
  }

  /** how much impact on speed one shot of this ammo has. 
   * typically a negative number, used to slow down creeps. */
  public float getSpeedImpact() {
    return speedDamage;
  }

  /** how much impact on health one shot of this ammo has.
  Typically a negative number to weaken creeps. */
  public float getHealthImpact() {
    return healthDamage;
  }

  public float getBlastRange() {
    return blast;
  }

  public Material getBeamMaterial() {
    return beam_mat;
  }

  public Geometry getGeometry() {
    return geometry;
  }
  /** ----------------------------------------------------------- */
  
  public void write(JmeExporter ex) throws IOException {
    OutputCapsule capsule = ex.getCapsule(this);
    capsule.write(ammoNum,      "ammo", 1);
    capsule.write(healthDamage, "healthDamage", 1f);
    capsule.write(speedDamage,  "speedDamage", 1f);
    capsule.write(blast,        "blast", 1f);
    capsule.write(beam_mat,     "beam_mat", null);
    capsule.write(geometry,     "geometry", null);
  }

  public void read(JmeImporter im) throws IOException {
    InputCapsule capsule = im.getCapsule(this);
    ammoNum       = capsule.readInt("ammo", 1);
    healthDamage  = capsule.readFloat("healthDamage", 1);
    speedDamage   = capsule.readFloat("speedDamage", 1);
    blast         = capsule.readFloat("blast", 1);
    beam_mat      = (Material) capsule.readSavable("beam_mat", null);
    geometry       = (Geometry)  capsule.readSavable("geometry", null);
  }
}
