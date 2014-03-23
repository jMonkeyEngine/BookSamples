package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 * This demo shows flames and fire.
 */
public class Particle6Fire extends SimpleApplication {

  @Override
  public void simpleInitApp() {
    ParticleEmitter fireEmitter = new ParticleEmitter(
         "Fire Emitter", ParticleMesh.Type.Triangle, 30);    
    Material fireMat = new Material(assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
    fireMat.setTexture("Texture", 
            assetManager.loadTexture("Effects/flame.png"));
    fireEmitter.setMaterial(fireMat);
    fireEmitter.setImagesX(2); 
    fireEmitter.setImagesY(2);
    fireEmitter.setRandomAngle(true);
    fireEmitter.setSelectRandomImage(true);
    rootNode.attachChild(fireEmitter);
    
    fireEmitter.setStartColor(new ColorRGBA(1f, 1f, .5f, 1f)); 
    fireEmitter.setEndColor(  new ColorRGBA(1f, 0f, 0f, 0f));  
    fireEmitter.setGravity(0,0,0);
    fireEmitter.setStartSize(1.5f);
    fireEmitter.setEndSize(0.05f);
    fireEmitter.setLowLife(0.5f);
    fireEmitter.setHighLife(2f);
    fireEmitter.getParticleInfluencer().
            setVelocityVariation(0.2f);
    fireEmitter.getParticleInfluencer().
            setInitialVelocity(new Vector3f(0,3f,0)); 
  }

  public static void main(String[] args) {
    Particle6Fire app = new Particle6Fire();
    app.start();

  }
}
