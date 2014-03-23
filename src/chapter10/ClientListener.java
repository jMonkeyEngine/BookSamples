package chapter10;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * This example package shows a client and a server (ServerMain and ClientMain)
 * which send messages (GreetingMessage and InetAddressMessage)
 * via message listeners (ServerListener and ClientListener).
 * @author ruth
 */
public class ClientListener implements MessageListener<Client> {

    public void messageReceived(Client source, Message message) {
        if (message instanceof GreetingMessage) {
            GreetingMessage helloMessage = (GreetingMessage) message;
            System.out.println("Client #" + source.getId()
                    + " received the message: '"
                    + helloMessage.getGreeting() + "'");
        } else if (message instanceof InetAddressMessage) {
            InetAddressMessage addrMessage = (InetAddressMessage) message;
            // unused
        }
    }

}
