package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.FastMath;

/**
 * This demo shows a moving smoke or dust particle cloud.
 */
public class Particle1DustSmoke extends SimpleApplication {

    private ParticleEmitter dustEmitter;
    private float angle = 0;

    public static void main(String[] args) {
        Particle1DustSmoke app = new Particle1DustSmoke();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        dustEmitter = new ParticleEmitter("dust emitter", Type.Triangle, 100);
        Material dustMat = new Material(assetManager,
                "Common/MatDefs/Misc/Particle.j3md");
        dustEmitter.setMaterial(dustMat);

        dustMat.setTexture("Texture",
                assetManager.loadTexture("Effects/smoke.png")); // 2x2 sprite ani
        dustEmitter.setImagesX(2);
        dustEmitter.setImagesY(2);
        dustEmitter.setSelectRandomImage(true);
        dustEmitter.setRandomAngle(true);
        dustEmitter.getParticleInfluencer().setVelocityVariation(1f);
        rootNode.attachChild(dustEmitter);
    }

    @Override
    public void simpleUpdate(float tpf) {
        /** make the emitter fly in circles */
        angle += tpf;
        angle %= FastMath.TWO_PI;
        float x = FastMath.cos(angle) * 2;
        float y = FastMath.sin(angle) * 2;
        dustEmitter.setLocalTranslation(x, 0, y);
    }
}
