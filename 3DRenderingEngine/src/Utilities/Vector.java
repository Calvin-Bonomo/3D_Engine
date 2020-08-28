package Utilities;

public class Vector {
    public double x, y, z, w;

    public Vector() {
        x = 0;
        y = 0;
        z = 0;
        w = 1;
    }

    public Vector(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    // Add 2 vectors
    // Optionally add the w component
    public static Vector add(Vector v1, Vector v2, boolean addW) {
        return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z, addW? v1.w + v2.w : v1.w);
    }

    // Subtract 2 vectors
    // Optionally subtract the w component
    public static Vector subtract(Vector v1, Vector v2, boolean subW) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z, subW? v1.w - v2.w : v1.w);
    }

    // Multiply a vector by some scalar number
    // Optionally multiply the w component
    public static Vector multiply(Vector v, double n, boolean multW) {
        return new Vector(v.x * n, v.y * n, v.z * n, multW? v.w * n : v.w);
    }

    // Divide a vector by some scalar number
    // Optionally divide the w component
    public static Vector divide(Vector v, double n, boolean divW) {
        return new Vector(v.x / n, v.y / n, v.z / n, divW? v.w / n : v.w);
    }

    // Find the dot product of 2 vectors
    public static double dotProduct(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    // Return the magnitude of a vector
    public static double magnitude(Vector v) {
        return Math.sqrt(Vector.dotProduct(v, v));
    }

    // Return a normalized vector
    public static Vector normalize(Vector v) {
        double mag = Vector.magnitude(v);
        return new Vector(v.x / mag, v.y / mag, v.z / mag, v.w);
    }

    // Return the cross product of 2 vectors
    public static Vector crossProduct(Vector v1, Vector v2) {
        Vector v = new Vector();
        v.x = v1.y * v2.z - v1.z * v2.y;
        v.y = v1.z * v2.x - v1.x * v2.z;
        v.z = v1.x * v2.y - v1.y * v2.x;
        return v;
    }
}
