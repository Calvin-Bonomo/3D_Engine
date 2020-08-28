package Engine;

import Engine.Rendering.Camera;
import Utilities.Matrix;
import Utilities.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    Camera camera;

    List<Object> objects = new ArrayList<>();
    public static List<Light> lights = new ArrayList<>();

    public static Matrix globalMatrix = new Matrix();

    public Scene() {
        globalMatrix.m[1][1] = 1.0;
        globalMatrix.m[2][3] = 9.0;

        camera = new Camera();

        lights.add(new Light(0, 0, 0, new Vector(0, 0, 1, 0), Color.white));
        lights.add(new Light(0, -4, 9, new Vector(0, 1, 0, 0), Color.white));
        objects.add(new Object("teapot.obj", 0.0, 0.0, 0.0));
    }

    public void update(Graphics2D g) {
        camera.update();

        for (Object obj : objects) {
            obj.render(g);
        }
    }
}
