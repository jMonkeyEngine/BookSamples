package chapter07;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;

/** 
 * Load a j3o scene containing an effect created in the SDK.
 */
public class LoadEffectFromJ3o extends SimpleApplication { // TODO

    public static void main(String[] args) {
        LoadEffectFromJ3o app = new LoadEffectFromJ3o();
        app.start();
    }

    @Override
    public void simpleInitApp() {
//        DirectionalLight sun = new DirectionalLight();
//        rootNode.addLight(sun);

        Node myScene = (Node) assetManager.loadModel("Scenes/myScene.j3o");
        rootNode.attachChild(myScene);

        SceneGraphVisitorAdapter myControlVisitor = new SceneGraphVisitorAdapter() {

            @Override
            public void visit(Geometry geom) {
                super.visit(geom);
                searchForEmitter(geom);
            }

            @Override
            public void visit(Node node) {
                super.visit(node);
                searchForEmitter(node);
            }

            private void searchForEmitter(Spatial spatial) {
                if (spatial instanceof ParticleEmitter && spatial.getName().equals("myEffect")) {
                    System.out.println("Emitter found in Spatial " + spatial.getName());
                    ((ParticleEmitter)spatial).setNumParticles(10);
                }
            }
        };

        // Start the Search: Scan e.g. the rootNode for MyControl using myControlVisitor.
        System.out.println("depth");
        rootNode.depthFirstTraversal(myControlVisitor);
        System.out.println("breadth");
        rootNode.breadthFirstTraversal(myControlVisitor);

    }
}
