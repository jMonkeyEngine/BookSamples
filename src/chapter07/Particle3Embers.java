package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * This demo shows a swarm of tiny sparkling embers, like from a campfire.
 */
public class Particle3Embers extends SimpleApplication {

  @Override
  public void simpleInitApp() {
    ParticleEmitter embersEmitter = new ParticleEmitter("embers", Type.Triangle, 20);
    Material embersMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    embersMat.setTexture("Texture", assetManager.loadTexture("Effects/embers.png"));
    embersEmitter.setMaterial(embersMat);
    embersEmitter.setImagesX(1);
    embersEmitter.setImagesY(1);
    rootNode.attachChild(embersEmitter);
    
    embersEmitter.setStartColor(new ColorRGBA(1f, 0.29f, 0.34f, 1.0f));
    embersEmitter.setEndColor(new ColorRGBA(0, 0, 0, 0.5f));
    embersEmitter.setStartSize(1.2f);
    embersEmitter.setEndSize(1.8f);
    embersEmitter.setGravity(0, -.5f, 0);
    embersEmitter.setLowLife(1.8f);
    embersEmitter.setHighLife(2f);
    embersEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3, 0));
    embersEmitter.getParticleInfluencer().setVelocityVariation(.5f);
    embersEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
  }

  public static void main(String[] args) {
    Particle3Embers app = new Particle3Embers();
    app.start();
  }
}
