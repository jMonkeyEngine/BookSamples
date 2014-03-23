package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * This demo shows random pieces of spinning flying debris that seem to be
 * hurled away after an explosion or after a building collapse.
 */
public class Particle4Debris extends SimpleApplication {

  @Override
  public void simpleInitApp() {
    ParticleEmitter debrisEmitter = new ParticleEmitter("Debris", Type.Triangle, 5);
    Material debrisMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    debrisMat.setTexture("Texture", assetManager.loadTexture("Effects/debris.png"));
    debrisEmitter.setMaterial(debrisMat);
    debrisEmitter.setImagesX(3);
    debrisEmitter.setImagesY(3);
    debrisEmitter.setSelectRandomImage(true);
    rootNode.attachChild(debrisEmitter);
    
    debrisEmitter.setRandomAngle(true);
    debrisEmitter.setRotateSpeed(FastMath.TWO_PI);
    debrisEmitter.setStartColor(new ColorRGBA(0.8f, 0.8f, 1f, 1.0f));
    debrisEmitter.setEndColor(new ColorRGBA(.5f, 0.5f, 0.5f, 1f));
    debrisEmitter.setStartSize(.2f);
    debrisEmitter.setEndSize(.7f);
    debrisEmitter.setGravity(0, 30f, 0);
    debrisEmitter.setLowLife(1.4f);
    debrisEmitter.setHighLife(1.5f);
    debrisEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 15, 0));
    debrisEmitter.getParticleInfluencer().setVelocityVariation(.50f);
    debrisEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
  }

  public static void main(String[] args) {
    Particle4Debris app = new Particle4Debris();
    app.start();

  }
}
