package Engine;

import Utilities.Matrix;
import Utilities.Vector;

public class Transform {
    // Create a rotation matrix on the x-axis
    public static Matrix getXRotationMatrix(double a) {
        Matrix xMat = new Matrix();
        xMat.m[1][1] = Math.cos(Math.toRadians(a));
        xMat.m[1][2] = Math.sin(Math.toRadians(a));
        xMat.m[2][1] = -Math.sin(Math.toRadians(a));
        xMat.m[2][2] = Math.cos(Math.toRadians(a));
        return xMat;
    }

    // Create a rotation matrix on the y-axis
    public static Matrix getYRotationMatrix(double a) {
        Matrix yMat = new Matrix();
        yMat.m[0][0] = Math.cos(Math.toRadians(a));
        yMat.m[0][2] = -Math.sin(Math.toRadians(a));
        yMat.m[2][0] = Math.sin(Math.toRadians(a));
        yMat.m[2][2] = Math.cos(Math.toRadians(a));
        return yMat;
    }

    // Create a rotation matrix on the z-axis
    public static Matrix getZRotationMatrix(double a) {
        Matrix zMat = new Matrix();
        zMat.m[0][0] = Math.cos(Math.toRadians(a));
        zMat.m[0][1] = Math.sin(Math.toRadians(a));
        zMat.m[1][0] = -Math.sin(Math.toRadians(a));
        zMat.m[1][1] = Math.cos(Math.toRadians(a));
        return zMat;
    }

    public static Matrix getPointAtMatrix(Vector pos, Vector target, Vector up) {
        Vector newForward = Vector.subtract(target, pos, false);
        newForward = Vector.normalize(newForward);

        Vector overlap = Vector.multiply(newForward, Vector.dotProduct(up, newForward), false);
        Vector newUp = Vector.subtract(up, overlap, false);
        newUp = Vector.normalize(newUp);

        Vector newRight = Vector.crossProduct(newUp, newForward);

        Matrix pointMat = new Matrix();
        pointMat.m[0][0] = newRight.x; pointMat.m[1][0] = newRight.y; pointMat.m[2][0] = newRight.z;
        pointMat.m[0][1] = newUp.x; pointMat.m[1][1] = newUp.y; pointMat.m[2][1] = newUp.z;
        pointMat.m[0][2] = newForward.x; pointMat.m[1][2] = newForward.y; pointMat.m[2][2] = newForward.z;
        pointMat.m[0][3] = pos.x; pointMat.m[1][3] = pos.y; pointMat.m[2][3] = pos.z;

        return pointMat;
    }

    // Only works for rotation and translation
    public static Matrix getLookAtMatrix(Matrix pM) {
        Matrix lookMat = new Matrix();
        lookMat.m[0][0] = pM.m[0][0]; lookMat.m[1][0] = pM.m[0][1]; lookMat.m[2][0] = pM.m[0][2];
        lookMat.m[0][1] = pM.m[1][0]; lookMat.m[1][1] = pM.m[1][1]; lookMat.m[2][1] = pM.m[1][2];
        lookMat.m[0][2] = pM.m[2][0]; lookMat.m[1][2] = pM.m[2][1]; lookMat.m[2][2] = pM.m[2][2];

        lookMat.m[0][3] = -(pM.m[0][3] * pM.m[0][0] + pM.m[1][3] * pM.m[1][0] + pM.m[2][3] * pM.m[2][0]);
        lookMat.m[1][3] = -(pM.m[0][3] * pM.m[0][1] + pM.m[1][3] * pM.m[1][1] + pM.m[2][3] * pM.m[2][1]);
        lookMat.m[2][3] = -(pM.m[0][3] * pM.m[0][2] + pM.m[1][3] * pM.m[1][2] + pM.m[2][3] * pM.m[2][2]);

        return lookMat;
    }
}
