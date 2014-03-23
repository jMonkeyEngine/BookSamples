package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.LowPassFilter;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 * An underwater visual effect combined with an underwater sound effect. The
 * sound effect changes depending on whether the camera is above or below the
 * water level.
 */
public class UnderWaterAudio extends SimpleApplication {

    private Vector3f lightDir = new Vector3f(-4.92f, -1.27f, 5.89f);
    private WaterFilter water;
    private float waterHeight = -1.0f;
    private boolean wasUnderwater = false;
    private AudioNode wavesAudio;
    private LowPassFilter underWaterAudioFilter = new LowPassFilter(0.5f, 0.1f);
    private LowPassFilter aboveWaterAudioFilter = new LowPassFilter(1f, 1f);

    public static void main(String[] args) {
        UnderWaterAudio app = new UnderWaterAudio();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // remove debug display from screen
        setDisplayFps(false);
        setDisplayStatView(false);
        // set up camera
        flyCam.setMoveSpeed(50);
        cam.setFrustumFar(4000);

        // Activate post-processor for special effects
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);

        // Create the scene that reflects in the water
        Node mainScene = new Node("Main Scene");
        rootNode.attachChild(mainScene);
        // Add sun light
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(1.7f));
        mainScene.addLight(sun);
        // Add sun beams
        LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
        lsf.setLightDensity(1.0f);
        fpp.addFilter(lsf);
        // Add sky
        Spatial sky = SkyFactory.createSky(assetManager,
                "Textures/Sky/Bright/FullskiesBlueClear03.dds", false);
        sky.setLocalScale(350);
        mainScene.attachChild(sky);

        // Add water (not attached to mainscene node, but to rootNode, 
        // because water doesn't reflect itself)
        water = new WaterFilter(rootNode, lightDir);
        water.setWaveScale(0.003f);
        water.setMaxAmplitude(3f);
        water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
        water.setFoamTexture((Texture2D) assetManager.loadTexture(
                "Common/MatDefs/Water/Textures/foam2.jpg"));
        water.setRefractionStrength(0.2f);
        water.setWaterHeight(waterHeight);
        fpp.addFilter(water);

        // Create water sound
        wavesAudio = new AudioNode(assetManager,
                "Sounds/Environment/Ocean Waves.ogg");
        wavesAudio.setLooping(true);
        wavesAudio.setPositional(false);
        wavesAudio.setReverbEnabled(true);
        wavesAudio.play();
        rootNode.attachChild(wavesAudio);
    }

    @Override
    public void simpleUpdate(float tpf) {
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        // whether under water or not depends on camera position
        if (water.isUnderWater() && !wasUnderwater ) {
            // activate underwater sound effect
            wavesAudio.stop();
            wasUnderwater = true;
            wavesAudio.setDryFilter(underWaterAudioFilter);
            wavesAudio.play();
        } else if (!water.isUnderWater() && wasUnderwater ) {
            // deactivate underwater sound effect
            wavesAudio.stop();
            wasUnderwater = false;
            wavesAudio.play();
            wavesAudio.setDryFilter(aboveWaterAudioFilter);
        }
    }
}
