package chapter10;

import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * This example package shows a client and a server (ServerMain and ClientMain)
 * which send messages (GreetingMessage and InetAddressMessage)
 * via message listeners (ServerListener and ClientListener).
 * This class is an example of a custom serializer for message data.
 * @author ruth
 */
public class InetAddressSerializer extends Serializer {

  @Override
  public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
    byte[] address = new byte[4];
    data.get(address);
    return (T) InetAddress.getByAddress(address);
  }

  @Override
  public void writeObject(ByteBuffer buffer, Object object) throws IOException {
    InetAddress address = (InetAddress) object;
    buffer.put(address.getAddress());
  }
}