package chapter09;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.audio.AudioNode;

/**
 * This demo shows how to stream long music from a URL or file location.
 */
public class BackgroundStreamingURL extends SimpleApplication {

  public static void main(String[] args) {
    BackgroundStreamingURL test = new BackgroundStreamingURL();
    test.start();
  }

  @Override
  public void simpleUpdate(float tpf) {
  }

  @Override
  public void simpleInitApp() {
    assetManager.registerLocator("http://www.vorbis.com/music/", UrlLocator.class);
    AudioNode src = new AudioNode(assetManager, "Lumme-Badloop.ogg", false);
    src.play();
  }
}