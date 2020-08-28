package Engine.Rendering;

import Engine.Display;
import Engine.Scene;
import Utilities.Matrix;

import java.awt.*;

public class Renderer {
    double fNear = 0.1;
    double fFar = 1000;

    double fovAngle;

    public static Matrix projMat = new Matrix();

    Scene scene;

    public Renderer(double fovAngle) {
        this.fovAngle = fovAngle;
        double fov = 1.0 / Math.tan((fovAngle / 2.0) * (Math.PI / 180.0));

        projMat.m[0][0] = ((double) Display.sW / (double)Display.sH) * fov;
        projMat.m[1][1] = fov;
        projMat.m[2][2] = fFar / (fFar - fNear);
        projMat.m[2][3] = (-fFar * fNear) / (fFar - fNear);
        projMat.m[3][2] = 1.0;
        projMat.m[3][3] = 0.0;

        scene = new Scene();
    }

    public void render(Graphics2D g) {
        g.clearRect(0, 0, Display.sW, Display.sH);

        g.setColor(Color.darkGray);
        g.drawRect(0, 0, Display.sW, Display.sH);
        scene.update(g);
    }
}