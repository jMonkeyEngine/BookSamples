package chapter06;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.util.TangentBinormalGenerator;


public class PhysicsTestHelper {

    /**
     * creates a simple physics test world with a floor, an obstacle and some test boxes
     * @param rootNode
     * @param assetManager
     * @param space
     */
    public static void createPhysicsTestWorld(Node rootNode, AssetManager assetManager, PhysicsSpace space) {
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);


        Box floorBox = new Box(240, 2f, 240);
        floorBox.scaleTextureCoordinates(new Vector2f(50,50));
        Geometry floorGeometry = new Geometry("Floor", floorBox);
        
        Material mat=assetManager.loadMaterial("Materials/pebbles.j3m");
        floorGeometry.setMaterial(mat);
        floorGeometry.setLocalTranslation(0, -1f, 0);
        floorGeometry.addControl(new RigidBodyControl(0));

        rootNode.attachChild(floorGeometry);
        space.add(floorGeometry);

        for (int i = 0; i < 5; i++) {
            makeBox(assetManager, rootNode, space, true);
        }
        for (int i = 0; i < 5; i++) {
            makeSphere(assetManager, rootNode, space, true);
        }
    }

  private static void makeSphere(AssetManager assetManager, 
          Node rootNode, PhysicsSpace space,Boolean mobile) {
    Sphere sphere = new Sphere(16, 16, FastMath.nextRandomFloat()*3f);
    TangentBinormalGenerator.generate(sphere);
    sphere.setTextureMode(TextureMode.Projected);
    sphere.scaleTextureCoordinates(new Vector2f(5,5));
    Geometry sphereGeometry = new Geometry("Sphere", sphere);
    sphereGeometry.setMaterial(assetManager.loadMaterial("Materials/rock.j3m"));
    sphereGeometry.setLocalTranslation(
            FastMath.nextRandomFloat()*20-10f, 
            10, 
            FastMath.nextRandomFloat()*20f-10);
    float mass = mobile ? FastMath.nextRandomFloat()*200 : 0f;
    RigidBodyControl c = new RigidBodyControl(mass);
    sphereGeometry.addControl(c);
    c.setFriction(1f);
    rootNode.attachChild(sphereGeometry);
    space.add(sphereGeometry);
  }

  private static void makeSphere(AssetManager assetManager, 
          int i, Node rootNode, PhysicsSpace space,Boolean mobile) {
    Sphere sphere = new Sphere(16, 16, 5f);
    TangentBinormalGenerator.generate(sphere);
    sphere.setTextureMode(TextureMode.Projected);
    sphere.scaleTextureCoordinates(new Vector2f(5,5));
    Geometry sphereGeometry = new Geometry("Sphere", sphere);
    sphereGeometry.setMaterial(assetManager.loadMaterial("Materials/rock.j3m"));
    float phi = i/12f * FastMath.PI;
    float x = FastMath.cos(phi)*80;
    float z = FastMath.sin(phi)*80;
    System.out.println(x+" / "+z);
    sphereGeometry.setLocalTranslation(x, -5, z);
    float mass = mobile ? FastMath.nextRandomFloat()*200 : 0f;
    RigidBodyControl c = new RigidBodyControl(mass);
    sphereGeometry.addControl(c);
    c.setFriction(1f);
    rootNode.attachChild(sphereGeometry);
    space.add(sphereGeometry);
  } 
  
  private static void makeBox(AssetManager assetManager, Node rootNode, PhysicsSpace space, Boolean mobile) {
    Box box = new Box(FastMath.nextRandomFloat()*2f+1, FastMath.nextRandomFloat()*2f+1, FastMath.nextRandomFloat()*2f+1);
    Geometry boxGeometry = new Geometry("Box", box);
    boxGeometry.setMaterial((Material)assetManager.loadMaterial("Materials/bark.j3m"));
    boxGeometry.setLocalTranslation(FastMath.nextRandomFloat()*20-10f, 10, FastMath.nextRandomFloat()*20f-10);
    //RigidBodyControl automatically uses box collision shapes when attached to single geometry with box mesh
    float mass = mobile ? FastMath.nextRandomFloat()*200 : 0f;
    RigidBodyControl c = new RigidBodyControl(mass);
    boxGeometry.addControl(c);
    c.setFriction(0.9f);
    
    rootNode.attachChild(boxGeometry);
    space.add(boxGeometry);
  }

}

