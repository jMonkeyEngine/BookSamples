package chapter10;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import java.net.InetAddress;

/**
 * This example package shows a client and a server (ServerMain and ClientMain)
 * which send messages (GreetingMessage and InetAddressMessage)
 * via message listeners (ServerListener and ClientListener).
 * Each Serializable message must have a unique id= number, 
 * so they are serialized and deserialized in the right order!
 * @author ruth
 */
@Serializable(id=1)
public class InetAddressMessage extends AbstractMessage {
     private InetAddress address;
     public InetAddressMessage() {}                  // empty default constructor
     public InetAddressMessage(InetAddress a) {      // custom constructor
       address = a;
     }                  
     public void setAddress(InetAddress a){address = a;}
     public InetAddress getAddress(){return address;}
}
