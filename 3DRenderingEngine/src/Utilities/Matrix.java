package Utilities;

public class Matrix {
    public double[][] m = new double[4][4];

    // Default constructor as an identity matrix
    // | 1 0 0 0 |
    // | 0 1 0 0 |
    // | 0 0 1 0 |
    // | 0 0 0 1 |
    public Matrix() {
        for (int c = 0; c < 4; c ++) {
            for (int r = 0; r < 4; r ++) {
                m[c][r] = (c == r)? 1.0 : 0.0;
            }
        }
    }

    public Matrix(double[][] m) {
        if (m.length > 16) return;
        this.m = m;
    }

    // Multiply a vector by a matrix and return the product
    public static Vector multiplyVector(Matrix mat, Vector v) {
        double[] vO = new double[4];

        for (int c = 0; c < 4; c ++) {
            vO[c] = v.x * mat.m[c][0] + v.y * mat.m[c][1] + v.z * mat.m[c][2] + v.w * mat.m[c][3];
        }

        return new Vector(vO[0], vO[1], vO[2], vO[3]);
    }

    // Multiply 2 matrices together and return the product
    public static Matrix multiplyMatrix(Matrix mat1, Matrix mat2) {
        Matrix newMat = new Matrix();

        for (int c = 0; c < 4; c ++) {
            for (int r = 0; r < 4; r ++) {
                newMat.m[c][r] = mat1.m[0][r] * mat2.m[c][0] + mat1.m[1][r] * mat2.m[c][1] + mat1.m[2][r] * mat2.m[c][2] + mat1.m[3][r] * mat2.m[c][3];
            }
        }
        return newMat;
    }
}
