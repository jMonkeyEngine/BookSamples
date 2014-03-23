package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * This demo shows flying sparks, like from welding or
 * from a short-circuiting electric panel.
 */
public class Particle2Sparks extends SimpleApplication {

  @Override
  public void simpleInitApp() {
    ParticleEmitter sparksEmitter = new ParticleEmitter(
            "Spark emitter", Type.Triangle, 60);
    Material sparkMat = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
    sparkMat.setTexture("Texture", 
            assetManager.loadTexture("Effects/spark.png"));
    sparksEmitter.setMaterial(sparkMat);
    sparksEmitter.setImagesX(1);
    sparksEmitter.setImagesY(1);
    rootNode.attachChild(sparksEmitter);
    
    sparksEmitter.getParticleInfluencer().
            setInitialVelocity(new Vector3f(0f, 10f, 0f));
    sparksEmitter.getParticleInfluencer().
            setVelocityVariation(1f);
    sparksEmitter.setStartColor( ColorRGBA.Yellow );
    sparksEmitter.setEndColor( ColorRGBA.Red ); 
    sparksEmitter.setGravity(0, 50, 0);
    sparksEmitter.setFacingVelocity(true);
    sparksEmitter.setStartSize(.5f);
    sparksEmitter.setEndSize(.5f);
    sparksEmitter.setLowLife(.9f);
    sparksEmitter.setHighLife(1.1f);
  }

  public static void main(String[] args) {
    Particle2Sparks app = new Particle2Sparks();
    app.start();
  }
}
