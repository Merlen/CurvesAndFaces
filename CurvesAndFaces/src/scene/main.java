package scene;

import help.Constants;

/**
 * @author Merlen
 */
public class Main {

    public static void main(String[] args) {

        if (!Constants.surface) {
            Scene.Scene();
        } else {
            SurfaceScene.SurfaceScene();
        }

    }
}
