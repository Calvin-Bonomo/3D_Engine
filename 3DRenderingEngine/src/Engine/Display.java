package Engine;

import Engine.Rendering.Camera;
import Engine.Rendering.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Display implements Runnable{
    JFrame f;
    BufferStrategy b;

    public static int sW, sH;

    Engine.Rendering.Renderer renderer;

    public Display(int sW, int sH, String dN) {
        Display.sW = sW;
        Display.sH = sH;

        Listener listener = new Listener();

        // Create the window displaying the rendering engine
        f = new JFrame(dN);

        JPanel p = (JPanel) f.getContentPane();
        p.setSize(sW, sH);
        p.setLayout(null);

        Canvas c = new Canvas();
        c.setBounds(0, 0, sW, sH);
        c.setIgnoreRepaint(true);
        c.setSize(sW, sH);
        c.setBackground(Color.darkGray);
        p.add(c);

        c.addKeyListener(listener);

        f.setUndecorated(false);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setExtendedState(JFrame.NORMAL);
        f.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width  / 2 - sW / 2,
                      Toolkit.getDefaultToolkit().getScreenSize().height / 2 - sH / 2);
        f.pack();
        f.setSize(sW, sH);
        f.setVisible(true);

        c.createBufferStrategy(2);
        b = c.getBufferStrategy();
        c.requestFocus();

        renderer = new Renderer(90.0);
    }


    @Override
    public void run() {
        renderer.render((Graphics2D) b.getDrawGraphics());

        b.show();
    }
}

class Listener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                Camera.moveUp = true;
                break;
            case KeyEvent.VK_DOWN:
                Camera.moveDown = true;
                break;
            case KeyEvent.VK_LEFT:
                Camera.moveLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                Camera.moveRight = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                Camera.moveUp = false;
                break;
            case KeyEvent.VK_DOWN:
                Camera.moveDown = false;
                break;
            case KeyEvent.VK_LEFT:
                Camera.moveLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                Camera.moveRight = false;
                break;
        }
    }
}