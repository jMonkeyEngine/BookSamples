package chapter10.threadsample;

import com.jme3.material.Material;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import java.util.concurrent.Callable;

/** This example shows how you enqueue changes to the scene graph 
 * correctly from the network thread. */
public class ClientListener implements MessageListener<Client> {

    private ClientMain app;
    private Client client;

    public void messageReceived(Client source, Message message) {
        if (message instanceof CubeMessage) {
            final CubeMessage cubeMessage = (CubeMessage) message;
            app.enqueue(new Callable() {
                public Void call() {
                    /* change something in the scene graph from here */
                    Material mat = new Material(app.getAssetManager(),
                            "Common/MatDefs/Misc/Unshaded.j3md");
                    mat.setColor("Color", cubeMessage.getColor());
                    app.getRootNode().getChild(0).setMaterial(mat);
                    return null;
                }
            });
        } 
    }
    
    /*A custom contructor to inform our client listener about the app. */
    public ClientListener(ClientMain app, Client client) {
        this.app = app;
        this.client = client;
    }
}
