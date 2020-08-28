package Engine;

import Utilities.Vector;

import java.awt.*;

public class Light {

    Vector pos;
    Vector pointDir;

    Color c;

    public Light(double xpos, double ypos, double zpos, Vector pointDir, Color c) {
        pos = new Vector(xpos, ypos, zpos, 1);
        this.pointDir = pointDir;
        this.c = c;
    }
}
