package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * This demo shows how to create a complex effect by combining several simple effects.
 * The explosion loop uses a simple timer that triggers a series of effects 
 * (fire, embers, smoke, debris, sparks, and shockwave) in order.
 */
public class Particle7Explosion extends SimpleApplication {

  private float time = 0;
  private int state = 0;
  private Node explosionEffect = new Node("mega explosion FX");
  private ParticleEmitter sparksEmitter, burstEmitter,
          shockwaveEmitter, debrisEmitter,
          fireEmitter, smokeEmitter, embersEmitter;

  public static void main(String[] args) {
    Particle7Explosion app = new Particle7Explosion();
    app.start();
  }

  private void initFire() {
    fireEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 100);
    Material fireMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    fireMat.setTexture("Texture", assetManager.loadTexture("Effects/flame.png"));
    fireEmitter.setMaterial(fireMat);
    fireEmitter.setImagesX(2);
    fireEmitter.setImagesY(2);
    fireEmitter.setRandomAngle(true);
    fireEmitter.setSelectRandomImage(true);
    fireEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
    rootNode.attachChild(fireEmitter);

    fireEmitter.setStartColor(new ColorRGBA(1f, 1f, .5f, 1f));
    fireEmitter.setEndColor(new ColorRGBA(1f, 0f, 0f, 0f));
    fireEmitter.setGravity(0, -.5f, 0);
    fireEmitter.setStartSize(1f);
    fireEmitter.setEndSize(0.05f);
    fireEmitter.setLowLife(.5f);
    fireEmitter.setHighLife(2f);
    fireEmitter.getParticleInfluencer().setVelocityVariation(0.3f);
    fireEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3f, 0));
    fireEmitter.setParticlesPerSec(0);
  }

  private void initBurst() {
    burstEmitter = new ParticleEmitter("Flash", Type.Triangle, 5);
    Material burstMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    burstMat.setTexture("Texture", assetManager.loadTexture("Effects/flash.png"));
    burstEmitter.setMaterial(burstMat);
    burstEmitter.setImagesX(2);
    burstEmitter.setImagesY(2);
    burstEmitter.setSelectRandomImage(true);
    burstEmitter.setRandomAngle(true);
    rootNode.attachChild(burstEmitter);

    burstEmitter.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1f));
    burstEmitter.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, .25f));
    burstEmitter.setStartSize(.1f);
    burstEmitter.setEndSize(6.0f);
    burstEmitter.setGravity(0, 0, 0);
    burstEmitter.setLowLife(.75f);
    burstEmitter.setHighLife(.75f);
    burstEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2f, 0));
    burstEmitter.getParticleInfluencer().setVelocityVariation(1);
    burstEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO, .5f));
    burstEmitter.setParticlesPerSec(0);

  }

  private void initEmbers() {
    embersEmitter = new ParticleEmitter("embers", Type.Triangle, 50);
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
    embersEmitter.setHighLife(5f);
    embersEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3, 0));
    embersEmitter.getParticleInfluencer().setVelocityVariation(.5f);
    embersEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
    embersEmitter.setParticlesPerSec(0);

  }

  private void initSparks() {
    sparksEmitter = new ParticleEmitter("Spark", Type.Triangle, 20);
    Material sparkMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    sparkMat.setTexture("Texture", assetManager.loadTexture("Effects/spark.png"));
    sparksEmitter.setMaterial(sparkMat);
    sparksEmitter.setImagesX(1);
    sparksEmitter.setImagesY(1);
    rootNode.attachChild(sparksEmitter);

    sparksEmitter.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1.0f)); // orange
    sparksEmitter.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, .5f));
    sparksEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 10, 0));
    sparksEmitter.getParticleInfluencer().setVelocityVariation(1);
    sparksEmitter.setFacingVelocity(true);
    sparksEmitter.setGravity(0, 15, 0);
    sparksEmitter.setStartSize(.5f);
    sparksEmitter.setEndSize(.5f);
    sparksEmitter.setLowLife(.9f);
    sparksEmitter.setHighLife(1.1f);
    sparksEmitter.setParticlesPerSec(0);

  }

  private void initSmoke() {
    smokeEmitter = new ParticleEmitter("Smoke emitter", Type.Triangle, 20);
    Material smokeMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    smokeMat.setTexture("Texture", assetManager.loadTexture("Effects/smoketrail.png"));
    smokeEmitter.setMaterial(smokeMat);
    smokeEmitter.setImagesX(1);
    smokeEmitter.setImagesY(3);
    smokeEmitter.setSelectRandomImage(true);
    rootNode.attachChild(smokeEmitter);

    smokeEmitter.setStartColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 1f));
    smokeEmitter.setEndColor(new ColorRGBA(.1f, 0.1f, 0.1f, .5f));
    smokeEmitter.setLowLife(4f);
    smokeEmitter.setHighLife(4f);
    smokeEmitter.setGravity(0,2,0);
    smokeEmitter.setFacingVelocity(true);
    smokeEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 6f, 0));
    smokeEmitter.getParticleInfluencer().setVelocityVariation(1);
    smokeEmitter.setStartSize(.5f);
    smokeEmitter.setEndSize(3f);
    smokeEmitter.setParticlesPerSec(0);
  }

  private void initDebris() {
    debrisEmitter = new ParticleEmitter("Debris", Type.Triangle, 15);
    Material debrisMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    debrisMat.setTexture("Texture", assetManager.loadTexture("Effects/debris.png"));
    debrisEmitter.setMaterial(debrisMat);
    debrisEmitter.setImagesX(3);
    debrisEmitter.setImagesY(3);
    debrisEmitter.setSelectRandomImage(true);
    debrisEmitter.setRandomAngle(true);
    rootNode.attachChild(debrisEmitter);

    debrisEmitter.setRotateSpeed(FastMath.TWO_PI * 2);
    debrisEmitter.setStartColor(new ColorRGBA(0.4f, 0.4f, 0.4f, 1.0f));
    debrisEmitter.setEndColor(new ColorRGBA(0.4f, 0.4f, 0.4f, 1.0f));
    debrisEmitter.setStartSize(.2f);
    debrisEmitter.setEndSize(1f);
    debrisEmitter.setGravity(0,10f,0);
    debrisEmitter.setLowLife(1f);
    debrisEmitter.setHighLife(1.1f);
    debrisEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 15, 0));
    debrisEmitter.getParticleInfluencer().setVelocityVariation(.60f);
    debrisEmitter.setParticlesPerSec(0);

  }

  private void initShockwave() {
    shockwaveEmitter = new ParticleEmitter("Shockwave", Type.Triangle, 2);
    Material shockwaveMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    shockwaveMat.setTexture("Texture", assetManager.loadTexture("Effects/shockwave.png"));
    shockwaveEmitter.setImagesX(1);
    shockwaveEmitter.setImagesY(1);
    shockwaveEmitter.setMaterial(shockwaveMat);
    explosionEffect.attachChild(shockwaveEmitter);
    
    /* The shockwave faces upward (along the Y axis) to make it appear as
     * a horizontally expanding circle. */
    shockwaveEmitter.setFaceNormal(Vector3f.UNIT_Y);
    shockwaveEmitter.setStartColor(new ColorRGBA(.68f, 0.77f, 0.61f, 1f));
    shockwaveEmitter.setEndColor(new ColorRGBA(.68f, 0.77f, 0.61f, 0f));
    shockwaveEmitter.setStartSize(1f);
    shockwaveEmitter.setEndSize(7f);
    shockwaveEmitter.setGravity(0, 0, 0);
    shockwaveEmitter.setLowLife(1f);
    shockwaveEmitter.setHighLife(1f);
    shockwaveEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
    shockwaveEmitter.getParticleInfluencer().setVelocityVariation(0f);
    shockwaveEmitter.setParticlesPerSec(0);
  }

  @Override
  public void simpleInitApp() {
    cam.setLocation(new Vector3f(0f,10f,10f)); // get a better view
    cam.lookAt(Vector3f.UNIT_Y, Vector3f.ZERO);
    initSparks();
    initBurst();
    initDebris();
    initSmoke();
    initFire();
    initEmbers();
    initShockwave();
    rootNode.attachChild(explosionEffect);
  }

  @Override
  public void simpleUpdate(float tpf) {
    // this is a timer that triggers a series of effects in the right order
    time += tpf / speed;
    if (time > 1.5f && state == 0) {
      sparksEmitter.emitAllParticles();
      state++;
    }
    if (time > 2.0f && state == 1) {
      burstEmitter.emitAllParticles();
      shockwaveEmitter.emitAllParticles();
      debrisEmitter.emitAllParticles();
      state++;
    }
    if (time > 2.2f && state == 2) {
      fireEmitter.emitAllParticles();
      embersEmitter.emitAllParticles();
      smokeEmitter.emitAllParticles();
      state++;
    }
    if (time > 5.0f && state == 3) {
      // rewind the effect
      burstEmitter.killAllParticles();
      sparksEmitter.killAllParticles();
      debrisEmitter.killAllParticles();
      shockwaveEmitter.killAllParticles();
      state++;
    }
    if (time > 8.0f && state == 4) {
      // rewind the effect
      smokeEmitter.killAllParticles();
      embersEmitter.killAllParticles();
      fireEmitter.killAllParticles();
      state++;
    }
    if (time > 8.5f && state == 5) {
      // restart the effect
      state = 0;
      time = 0;
    }
  }
}
