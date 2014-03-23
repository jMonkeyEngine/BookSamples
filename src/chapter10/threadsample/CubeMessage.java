package chapter10.threadsample;

import com.jme3.math.ColorRGBA;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/** This package contains an example that shows 
 * how you enqueue changes to the scene graph 
 * correctly from the network thread -- see ClientListener. 
 * This message broadcasts a color.
 */
@Serializable(id = 0)
public class CubeMessage extends AbstractMessage {

    ColorRGBA color;

    public CubeMessage() {} // empty default constructor
    public CubeMessage(ColorRGBA color) {
        this.color = color;
    }
    public ColorRGBA getColor(){
        return color; 
    }
}
