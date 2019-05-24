package byow.Core;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Test;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class HelperTest {


//    @Test
    public void testIsCorner() {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Engine eng = new Engine();
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        /**
         for (int x = 20; x < 50; x ++) {
         for (int y = 5; y < 25; y++) {
         world[x][y] = Tileset.FLOOR;
         }
         }
         */

        /**
        eng.createRoom(world);


        ter.renderFrame(world);

        Engine.Point upperCorn = new Engine.Point(68, 15);
        Engine.Point notCorn = new Engine.Point(72, 25);


        assertTrue(eng.isCorner(world, upperCorn));
        assertFalse(eng.isCorner(world, notCorn));




        isCorner();
        */


    }

    @Test
    public void testInput() {

        Engine eng = new Engine();
        eng.interactWithInputString("N12345s");





    }






}



