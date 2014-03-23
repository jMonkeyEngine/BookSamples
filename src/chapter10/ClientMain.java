package chapter10;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 * This example package shows a client and a server (ServerMain and ClientMain)
 * which send messages (GreetingMessage and InetAddressMessage)
 * via message listeners (ServerListener and ClientListener).
 * @author ruth
 */
public class ClientMain extends SimpleApplication implements ClientStateListener {

    private Client myClient;

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("").setLevel(Level.SEVERE);
        ClientMain app = new ClientMain();
        app.start(JmeContext.Type.Display);
    }

    @Override
    public void simpleInitApp() {
        try {
            myClient = Network.connectToServer("My Cool Game", 1, "localhost", 6143);
            myClient.start();
        } catch (IOException ex) {
        }
        Serializer.registerClass(GreetingMessage.class);
        Serializer.registerClass(InetAddressMessage.class);
        Serializer.registerClass(InetAddress.class, new InetAddressSerializer());

        myClient.addMessageListener(new ClientListener(),GreetingMessage.class);
        myClient.addMessageListener(new ClientListener(),InetAddressMessage.class);
        myClient.addClientStateListener(this);

        // example 1 -- client-server communication
        Message m = new GreetingMessage("Hi server, do you hear me?");
        myClient.send(m);
        
        // example 2 -- transmitting data with a custom serializer
        try {
            Message message = new InetAddressMessage(
                    InetAddress.getByName("jmonkeyengine.org"));
            myClient.send(message);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void destroy() {
        try {
            myClient.close();
        } catch (Exception ex) {
        }
        super.destroy();
    }

    /** Specify what happens when this client connects to server */
    public void clientConnected(Client client) {
        System.out.println("Client #" + client.getId() + " is ready.");
    }
    
    /** Specify what happens when this client disconnects from server */
    public void clientDisconnected(Client client, DisconnectInfo info) {
        System.out.println("Client #" + client.getId() + " has left.");
    }
}
