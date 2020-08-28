package Engine;

import Engine.Rendering.Camera;
import Engine.Rendering.Renderer;
import Utilities.Matrix;
import Utilities.Vector;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

public class Object {
    Mesh objectMesh;

    double xpos, ypos, zpos;
    Matrix localTransform;

    public Object(String fileName, double xpos, double ypos, double zpos) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.zpos = zpos;
        loadMesh(fileName);
    }

    public void render(Graphics2D g) {
        List<Polygon> polys = new ArrayList<>();
        List<Color> colors  = new ArrayList<>();

        Vector largestDist = new Vector();

        for (Tri tri : objectMesh.tris) {
            // This was just me testing my rotation matrices (it works!)
//            Matrix xMat = Transform.getXRotationMatrix(2.0);
            Matrix yMat = Transform.getYRotationMatrix(2.0);
//            Matrix zMat = Transform.getZRotationMatrix(2.0);
//
//            Matrix rotMat = Matrix.multiplyMatrix(xMat, zMat);
//
//
            tri.p[0] = Matrix.multiplyVector(yMat, tri.p[0]);
            tri.p[1] = Matrix.multiplyVector(yMat, tri.p[1]);
            tri.p[2] = Matrix.multiplyVector(yMat, tri.p[2]);



            // Translate the triangle into global space
            Vector Tp1 = Matrix.multiplyVector(Scene.globalMatrix, tri.p[0]);
            Vector Tp2 = Matrix.multiplyVector(Scene.globalMatrix, tri.p[1]);
            Vector Tp3 = Matrix.multiplyVector(Scene.globalMatrix, tri.p[2]);
            Tri translatedTri = new Tri(Tp1, Tp2, Tp3, tri.c);

            // Find the normal of the current triangle
            Vector line1 = Vector.subtract(translatedTri.p[1], translatedTri.p[0], false);
            Vector line2 = Vector.subtract(translatedTri.p[2], translatedTri.p[0], false);
            Vector normal = Vector.normalize(Vector.crossProduct(line1, line2));

            // Check the visibility of the triangle via the normal
            if (Vector.dotProduct(normal, Vector.subtract(translatedTri.p[0], Camera.cameraPos, false)) < 0.0) {
                //Shade the triangle
                Color shadedColor;

                float totalLum = 0;
                int affectingLights = 0;
                for (Light light : Scene.lights) {
                    float lum = (float)-Vector.dotProduct(normal, Vector.add(light.pointDir, light.pos, false)) / 10.0f;
                    if (lum > 0) {
                        totalLum += lum;
                        affectingLights ++;
                    }
                }
                totalLum /= affectingLights;
                float[] base = Color.RGBtoHSB(tri.c.getRed(), tri.c.getGreen(), tri.c.getBlue(), null);
                shadedColor = Color.getHSBColor(base[0], base[1], (base[2] + totalLum) / 2.0f);

                Vector target = Vector.add(Camera.cameraPos, Camera.lookDir, false);
                Matrix cameraMatrix = Transform.getPointAtMatrix(Camera.cameraPos, target, Camera.up);

                Matrix inverseCameraMatrix = Transform.getLookAtMatrix(cameraMatrix);

                // Convert the triangle into screenSpace
                Vector s1 = Matrix.multiplyVector(inverseCameraMatrix, translatedTri.p[0]);
                Vector s2 = Matrix.multiplyVector(inverseCameraMatrix, translatedTri.p[1]);
                Vector s3 = Matrix.multiplyVector(inverseCameraMatrix, translatedTri.p[2]);
                Tri screenTri = new Tri(s1, s2, s3, tri.c);

                // Project the triangle into 2d space
                Vector p1 = Matrix.multiplyVector(Renderer.projMat, screenTri.p[0]);
                Vector p2 = Matrix.multiplyVector(Renderer.projMat, screenTri.p[1]);
                Vector p3 = Matrix.multiplyVector(Renderer.projMat, screenTri.p[2]);
                Tri projectedTri = new Tri(p1, p2, p3, tri.c);

                // Normalize the projected triangles
                projectedTri.p[0] = Vector.divide(p1, projectedTri.p[0].w, false);
                projectedTri.p[1] = Vector.divide(p2, projectedTri.p[1].w, false);
                projectedTri.p[2] = Vector.divide(p3, projectedTri.p[2].w, false);

                // Offset the points to the center of the normalized screen
                Vector offset = new Vector(1.0, 1.0, 0.0, 0.0);
                projectedTri.p[0] = Vector.add(projectedTri.p[0], offset, false);
                projectedTri.p[1] = Vector.add(projectedTri.p[1], offset, false);
                projectedTri.p[2] = Vector.add(projectedTri.p[2], offset, false);

                // Scale the points to the screen
                projectedTri.p[0].x *= 0.5 * Display.sW; projectedTri.p[0].y *= 0.5 * Display.sH;
                projectedTri.p[1].x *= 0.5 * Display.sW; projectedTri.p[1].y *= 0.5 * Display.sH;
                projectedTri.p[2].x *= 0.5 * Display.sW; projectedTri.p[2].y *= 0.5 * Display.sH;

                // Convert the triangle into a java.awt polygon for rendering
                Polygon triangle = new Polygon();
                triangle.addPoint((int)projectedTri.p[0].x, (int)projectedTri.p[0].y);
                triangle.addPoint((int)projectedTri.p[1].x, (int)projectedTri.p[1].y);
                triangle.addPoint((int)projectedTri.p[2].x, (int)projectedTri.p[2].y);


//                Vector averageVector = Vector.divide(Vector.add(Vector.add(projectedTri.p[0], projectedTri.p[1], false), projectedTri.p[2], false), 3.0, false);

                // An attempt a a very basic painter's algorithm since i can't figure out z buffering
                Vector cameraDist = Vector.subtract(translatedTri.p[0], Camera.cameraPos, false);
                if (largestDist.x != 0 && largestDist.y != 0 && largestDist.z != 0) {
                    if (cameraDist.z > largestDist.z) {
                        polys.add(0, triangle);
                        colors.add(0, shadedColor);
                        largestDist = cameraDist;
                        continue;
                    }
                }
                polys.add(triangle);
                colors.add(shadedColor);
            }
        }

        for (int i = 0; i < polys.size(); i ++) {
            g.setColor(colors.get(i));
            g.fillPolygon(polys.get(i));
        }
    }

    // This method can only load verticies and triangles
    // This may be expanded in future builds
    private void loadMesh(String fileName) {
        List<Tri> tris = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;

            System.out.println("Loading mesh \"" + fileName + "\"");

            List<Vector> verts = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.length() <= 1) continue;
                switch (line.substring(0, 2)) {
                    // Add the vertices to a list of vectors
                    case "v ":
                        String[] vertPos = line.substring(2).split(" ");

                        double pos1 = Double.parseDouble(vertPos[0]);
                        double pos2 = Double.parseDouble(vertPos[1]);
                        double pos3 = Double.parseDouble(vertPos[2]);

                        verts.add(new Vector(pos1, pos2, pos3, 1.0));
                        break;
                    // Using the vertices in the "verts" list, build triangles
                    case "f ":
                        String[] index = line.substring(2).split(" ");

                        int index1 = Integer.parseInt(index[0]) - 1;
                        int index2 = Integer.parseInt(index[1]) - 1;
                        int index3 = Integer.parseInt(index[2]) - 1;

                        tris.add(new Tri(verts.get(index1), verts.get(index2), verts.get(index3), Color.white));
                        break;
                }
            }
            System.out.println("Finished loading mesh \"" + fileName + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create a new mesh if there is no
        if (tris.size() > 0) objectMesh = new Mesh(tris);
    }
}

class Mesh {
    List<Tri> tris;

    public Mesh(List<Tri> tris) {
        this.tris = tris;
    }
}

class Tri {
    Vector[] p = new Vector[3];
    Color c;

    public Tri(Vector v1, Vector v2, Vector v3, Color c) {
        p[0] = v1;
        p[1] = v2;
        p[2] = v3;
        this.c = c;
    }
}