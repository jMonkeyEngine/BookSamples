package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * This demo shows a burst of fire that you can use as the center of an explosion effect.
 */
public class Particle5Burst extends SimpleApplication {

  @Override
  public void simpleInitApp() {
    ParticleEmitter burstEmitter = new ParticleEmitter("Burst emitter", Type.Triangle, 5);
    Material burstMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    burstMat.setTexture("Texture", assetManager.loadTexture("Effects/flash.png"));
    burstEmitter.setMaterial(burstMat);
    burstEmitter.setImagesX(2);
    burstEmitter.setImagesY(2);
    burstEmitter.setSelectRandomImage(true);
    rootNode.attachChild(burstEmitter);
    
    burstEmitter.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1f));
    burstEmitter.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
    burstEmitter.setStartSize(.1f);
    burstEmitter.setEndSize(5.0f);
    burstEmitter.setGravity(0, 0, 0);
    burstEmitter.setLowLife(.3f);
    burstEmitter.setHighLife(.3f);
    burstEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5f, 0));
    burstEmitter.getParticleInfluencer().setVelocityVariation(1);
    burstEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO, .5f));
  }

  public static void main(String[] args) {
    Particle5Burst app = new Particle5Burst();
    app.start();
  }
}
