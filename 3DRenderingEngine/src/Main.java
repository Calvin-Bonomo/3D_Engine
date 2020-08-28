import Engine.Display;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Display display = new Display(600, 600, "3D Renderer");

        // An executor service runs the program at 30 fps
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(display, 0, 1000/30, TimeUnit.MILLISECONDS);
    }
}
