package Engine.Rendering;

import Utilities.Vector;

public class Camera {
    public static Vector up = new Vector(0, 1, 0, 0);
    public static Vector cameraPos = new Vector();
    public static Vector lookDir = new Vector(0, 0, 1, 0);

    double speed = 0.3;

    public static boolean moveUp = false;
    public static boolean moveDown = false;
    public static boolean moveLeft = false;
    public static boolean moveRight = false;

    public void update() {
        if (moveUp)    cameraPos.y -= speed;
        if (moveDown)  cameraPos.y += speed;
        if (moveLeft)  cameraPos.x -= speed;
        if (moveRight) cameraPos.x += speed;
    }

    private Vector checkPlaneIntersection(Vector planePoint, Vector planeNormal, Vector lineStart, Vector lineEnd) {
        planeNormal = Vector.normalize(planeNormal);
        double planeD = -Vector.dotProduct(planeNormal, planePoint);
        double ad = Vector.dotProduct(lineStart, planeNormal);
        double bd = Vector.dotProduct(lineEnd, planeNormal);
        double t = (-planeD - ad) / (bd - ad);
        Vector lineStartToEnd = Vector.subtract(lineEnd, lineStart, false);
        Vector lineToIntersect = Vector.multiply(lineStartToEnd, t, false);
        return Vector.add(lineStart, lineToIntersect, false);
    }
}