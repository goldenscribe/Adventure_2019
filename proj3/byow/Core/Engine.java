package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.*;
//import java.u
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.introcs.StdDraw;



import static byow.Core.RandomUtils.uniform;
import static byow.TileEngine.Tileset.*;


//import StdDraw3D

// interactivity starts at line 364

public class Engine {
    TERenderer ter;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    //2873123
//    private static final long SEED = 2147744;
    private long seed;
    private String move;
    private Random random;
    private int roomLoad;
    private LinkedList<Point> centerPoints = new LinkedList<Point>();
    private WeightedQuickUnionUF roomWQU;
    private TETile[][] world;

    private boolean gameWon;
    private boolean gameLost;
    private boolean dialogue;
    private String name;
    private String nextTo;

    private KiwiBot kiwi;
    private Food food;
    private Customer customer;
    private Thief thief;

    private static final String FALSE1 = "Sets can have duplicates!";
    private static final String FALSE2 = "HashMap put operations are always constant time";
    private static final String FALSE3 = "A* will always find the shortest path even if "
            + "the heuristic overshoots the distance";
    private static final String FALSE4 = "The final for this class is May 17th";
    private static final String FALSE5 = "You can have a WeightedQuickUnion "
            + "of height 4 with 10 elements";


    private static final String TRUE1 = "The minimum edge will always be in an MST";
    private static final String TRUE2 = "The integral of 1/x is ln(x)";
    private static final String TRUE3 = "The Durant Taco Bell happy hour is from 3-5pm";
    private static final String TRUE4 = "The following a valid scheme macro: "
            + "(define-macro (infix e)\n"
            + "(if (number? e) e\n"
            + "`(,(cadr e) (infix ,(car e)) (infix ,(caddr e)))))\n"
            + "(define (cadr x) (car (cdr x)))\n"
            + "(define (caddr x) (car (cdr (cdr x))))";
    private static final String TRUE5 = "Double for-loops don't always imply N^2 runtime";




    // list of rooms on map where index is the rooms
    // corresponding int in the WQU
    private List<Room> roomList;

    private class Point {
        private int x;
        private int y;
        protected Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        protected int getX() {
            return this.x;
        }
        protected int getY() {
            return this.y;
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        //Allows player to make a name for themselves.
        try {
            File file = new File("name.txt");
            Scanner scan = new Scanner(file);
            try {
                name = scan.nextLine();
            } catch (java.util.NoSuchElementException e) {
                name = "KIWI";
            }
            if (name == "" || name == "s" || name == "S") {
                name = "KIWI";
            }
        } catch (IOException e) {
            name = "KIWI";
        }

        ter = new TERenderer();
        keyboardDraw();
        /**
         * @source: JHug demo input
         */
        InputSource keys = new KeyboardInputSource();
        String soFar = "";
        while (true) {
            char c = keys.getNextKey();
            if (c == 'l' || c == 'L') {
                try {
                    File file = new File("save.txt");
                    Scanner scan = new Scanner(file);
                    String a = scan.nextLine();
                    interactWithInputString(a);
                    renderer();
                } catch (IOException e) {
                    char n = ' ';
                }
            }
            else if (c == 'n' || c == 'N') {
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.text(WIDTH / 2 - 5, HEIGHT / 2 + 8, "The goal of this game is to deliver food to the customer");
                StdDraw.text(WIDTH / 2 - 1, HEIGHT / 2 + 6, "However, there are two people: one is the customer, the other is a thief disguised as the customer");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "As the KiwiBot, you have to deliver the food to the customer who says a True statement!");

                StdDraw.text(WIDTH / 2, HEIGHT / 2, "enter seed: ");
                StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 3, "press S to start!");
                StdDraw.show();
                String seed = "";
                while (true) {
                    if (c == 's' || c == 'S') {
                        soFar = soFar + c;
                        world = interactWithInputString(soFar);
                        renderer();
                        break;
                    }
                    soFar = soFar + c;
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.text(WIDTH / 2 - 1, HEIGHT / 2 + 10, "The goal of this game is to deliver food to the customer");
                    StdDraw.text(WIDTH / 2 - 1, HEIGHT / 2 + 8, "However, there are two people: one is the customer,");
                    StdDraw.text(WIDTH / 2 + 3, HEIGHT / 2 + 6, "the other is a thief disguised as the customer");
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 4, "As the KiwiBot, you have to deliver the food to the customer who says a True statement!");

                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "enter seed: " + seed);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "press (S) to start!");
                    StdDraw.show();
                    c = keys.getNextKey();
                    seed = seed + c;
                }
            }
            else if (c == 'c' || c == 'C') {
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "enter name: ");
                StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 3, "press (S) to save!");
                StdDraw.show();
                String nameFar = "";
                while (true) {
                    if (c == ':' || c == ':') {
                        char k = keys.getNextKey();
                        if (k == 'S' || k == 's') {
                            try {
                                nameSaver(nameFar.substring(0, nameFar.length() - 1));
                                break;
                            } catch (java.io.IOException e) {
                                continue;
                            }
                        }
                    }
                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2, "enter name: " + nameFar);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "press (:S) to save!");
                    StdDraw.show();
                    c = keys.getNextKey();
                    nameFar = nameFar + c;
                }
                interactWithKeyboard();
            } else {
                interactWithKeyboard();
            }
            while (!(gameLost) && !(gameWon)) {
                char curr = ' ';
                double mouseX = StdDraw.mouseX();
                double mouseY = StdDraw.mouseY();
                if (StdDraw.hasNextKeyTyped()) {
                    curr = keys.getNextKey();
                }
                if (curr == ':') {
                    char bb = keys.getNextKey();
                    if (bb == 'q' || bb == 'Q') {
                        try {
                            writer(soFar);
                            ter.renderFrame(world);
                            System.exit(0);
                            /**
                            StdDraw.clear(StdDraw.BLACK);
                            StdDraw.setPenColor(StdDraw.WHITE);
                            StdDraw.text(6, HEIGHT - 1, "Done");
                            StdDraw.show();
                            break;
                             */
                        } catch (java.io.IOException e) {
                            continue;
                        }
                    }
                } else {
                    String s = String.valueOf(curr);
                    soFar += curr;
                    stringToMove(s);
                }
                ter.renderFrame(world);
                mouseWorker(mouseX, mouseY);
                if (kiwi.hasFood) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.text(20, HEIGHT + 1, "Delivery in Progress");
                }
                while (dialogue) {

                    if (nextTo.equals("customer")) {
                        dialogueCustomer();
                        char t = keys.getNextKey();
                        if (t == 'G' || t == 'g') {
                            dialogue = false;
                            gameWon = true;
                        } else if (t == 'r' || t == 'R') {
                            dialogue = false;
                            soFar = soFar.substring(0, soFar.length() - 1);
                            interactWithInputString(soFar);

//                            break;
                        }
                    } else if (nextTo.equals("thief")) {
                        dialogueThief();
                        char t = keys.getNextKey();
                        if (t == 'G' || t == 'g') {
                            dialogue = false;
                            gameLost = true;
                        } else if (t == 'r' || t == 'R') {
                            dialogue = false;
                            soFar = soFar.substring(0, soFar.length() - 1);
                            interactWithInputString(soFar);
//                            break;
                        }
                    }
                    // call dialogueThief or dialogueCustomer

                }
                StdDraw.setPenColor(StdDraw.WHITE);
                String batt = Integer.toString(kiwi.battery);
                StdDraw.text(4 + name.length(), HEIGHT + 1, "Battery Life: " + batt);
                StdDraw.text(2, HEIGHT + 1, name);
                StdDraw.show();
                StdDraw.pause(50);
            }
            endGame();
            return;
        }
    }
    private void keyboardDraw() {
        StdDraw.setCanvasSize(this.WIDTH * 16, this.HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 4, "KiwiBot: The Game");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 1, "(N)ew Seed");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, "(:Q)uit");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "(L)oad");
        StdDraw.text(WIDTH / 2, HEIGHT/2 - 5, "(C)haracter name");
        StdDraw.show();
    }

    private void endGame() {
        // we're in the endgame now!
        if (gameLost) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "GAME OVER");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, "THE THIEF GOT THE FOOD");

            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Buy More lives!");
            StdDraw.show();
        } else if (gameWon) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "YOU WIN!");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Your Tip: $"
                    + uniform(random, 1.0, 28.74));
            StdDraw.show();
        }
    }

    private void writer(String soFar)
            throws IOException {
        String str = soFar;
        BufferedWriter writer = new BufferedWriter(new FileWriter("save.txt"));
        writer.write(str);
        writer.close();
    }

    private void nameSaver(String nameFar)
            throws IOException {
        String str = nameFar;
        BufferedWriter writer = new BufferedWriter(new FileWriter("name.txt"));
        writer.write(str);
        writer.close();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    //may need to revise to cover more input cases
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        /**
         * @source: JHug demo input
         */
        InputSource str = new StringInputDevice(input);
        String soFar = "";
        move = "";
        char c = ' ';
        if (str.possibleNextInput()) {
            c = str.getNextKey();
        }
        if (c == 'n' || c == 'N') {
            soFar += c;
            c = str.getNextKey();
            String s = "";
            while (str.possibleNextInput()) {
                if (c == 's' || c == 'S') {
                    soFar += c;
                    break;
                }
                s = s + c;
                soFar += c;
                c = str.getNextKey();
            }
            seed = Long.parseLong(s);
        } else if (c == 'l' || c == 'L') {
            try {
                File file = new File("save.txt");
                Scanner scan = new Scanner(file);
                String a = scan.nextLine();
                while (str.possibleNextInput()) {
                    a += str.getNextKey();
                }
                return interactWithInputString(a);
            } catch (IOException e) {
                char n = ' ';
            }
        }
        while (str.possibleNextInput()) {
            c = str.getNextKey();
            if (c == ':') {
                char kk = str.getNextKey();
                if (kk == 'Q' || kk == 'q') {
                    try {
                        writer(soFar);
                    } catch (java.io.IOException e) {
                        char x = ' ';
                    }
                }
            } else {
                soFar += c;
                move += c;
            }
        }
        world = new TETile[WIDTH][HEIGHT];
        random = new Random(seed);
        roomLoad = uniform(random, WIDTH / 10 + HEIGHT / 10, WIDTH / 5 + HEIGHT / 5);
        fillNothing();
        createRandomRooms();
        buildWalls();
        treeConnect();
        buildWalls();
        placeKiwi();
        placeFood();
        placeCustomer();
        placeThief();
        stringToMove(move);
        TETile[][] finalWorldFrame = world;
        return finalWorldFrame;
    }
    public Engine() {
        ter = new TERenderer();
    }
    private Engine(Random random, int roomLoad) {
        this.roomLoad = roomLoad;
        this.random = random;
    }
    public void renderer() {

        ter.initialize(WIDTH, HEIGHT + 2);
        ter.renderFrame(world);
    }
    private class Room {
        Point center;
        Point lowerL;
        Point upperR;
        int number;

        private Room(Point center, Point lowerL, Point upperR, int number) {
            this.center = center;
            this.lowerL = lowerL;
            this.upperR = upperR;
            this.number = number;
        }

    }


    private Boolean notOverlapping(Point L, Point U) {
        for (int x = L.getX() - 1; x <= U.getX() + 1; x++) {
            for (int y = L.getY() - 1; y <= U.getY() + 1; y++) {
                if (world[x][y] == Tileset.FLOOR) {
                    return false;
                }
            }
        }
        return true;
    }

    private Point worldRandomPoint() {
        int randomX = uniform(random, 1, WIDTH - 8);
        int randomY = uniform(random, 1, HEIGHT - 8);
        Point result = new Point(randomX, randomY);
        return result;
    }

    protected boolean isCorner(TETile[][] w, Point p) {
        if (w[p.getX() - 1][p.getY()] == WALL
                && w[p.getX()][p.getY() - 1] == WALL) {
            return true;
        } else if (w[p.getX() + 1][p.getY()] == WALL
                && w[p.getX()][p.getY() - 1] == WALL) {
            return true;
        } else if (w[p.getX() + 1][p.getY()] == WALL
                && w[p.getX()][p.getY() + 1] == WALL) {
            return true;
        } else if (w[p.getX() - 1][p.getY()] == WALL
                && w[p.getX()][p.getY() + 1] == WALL) {
            return true;
        }
        return false;
    }

    private Point worldUpperRight(Point lowerLeft) {
        int randomX = 0;
        int randomY = 0;

        if (lowerLeft.getX() + 7 >= WIDTH - 1) {
            randomX = uniform(random, lowerLeft.getX() + 2, WIDTH - 1);
        } else {
            randomX = uniform(random, lowerLeft.getX() + 2, lowerLeft.getX() + 7);
        }
        if (lowerLeft.getY() + 7 >= HEIGHT - 1) {
            randomY = uniform(random, lowerLeft.getY() + 2, WIDTH - 1);
        } else {
            randomY = uniform(random, lowerLeft.getY() + 2, lowerLeft.getY() + 7);
        }
        Point result = new Point(randomX, randomY);
        return result;
    }

    //FIND WHAT THIS CAN BE USED FOR.
    public void randomPoint(int lowerBound, int upperBound) {
        int randomX = uniform(random, lowerBound, upperBound);
    }

    //Creates random Rooms and includes a set of all Room lower-left and upper-right points.
    protected void createRandomRooms() {

        int index = this.roomLoad;
        roomList = new ArrayList<>();
        while (index > 0) {
            Point lowerLeft = worldRandomPoint();
            Point upperRight = worldUpperRight(lowerLeft);
            if (notOverlapping(lowerLeft, upperRight)) {
                createEmptyRoom(lowerLeft, upperRight);
                int centerX = lowerLeft.getX() + ((upperRight.getX() - lowerLeft.getX()) / 2);
                int centerY = lowerLeft.getY() + ((upperRight.getY() - lowerLeft.getY()) / 2);

                Point tempCenter = new Point(centerX, centerY);
                centerPoints.add(tempCenter);
                Room tempRoom = new Room(tempCenter, lowerLeft, upperRight, index - 1);
                roomList.add(tempRoom);
                index -= 1;
            }
        }
    }

    private void createEmptyRoom(Point lowerLeft, Point upperRight) {
        for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
            for (int y = lowerLeft.getY(); y <= upperRight.getY(); y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    public void fillNothing() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    //Can be called last before initializing world to build walls around all floor tiles.
    //Finish a helper method to do this more efficiently.
    //Still buggy, FIX
    private void buildWalls() {
        for (int x = 1; x < WIDTH - 1; x++) {
            for (int y = 1; y < HEIGHT - 1; y++) {
                if (world[x][y] == Tileset.FLOOR) {
                    Point check = new Point(x, y);
                    for (Point p : adjacent(check)) {
                        if (world[p.getX()][p.getY()] == Tileset.NOTHING) {
                            world[p.getX()][p.getY()] = WALL;
                        }
                    }
                }
            }
        }
    }

    //Returns a list containing all tiles adjacent to a given point.
    //DONE
    private HashSet<Point> adjacent(Point point) {
        HashSet<Point> result = new HashSet();
        int i = point.getX();
        int j = point.getY();
        for (int x = i - 1; x <= i + 1; x++) {
            for (int y = j - 1; y <= j + 1; y++) {
                result.add(new Point(x, y));
            }
        }
        return result;
    }

    private void connect(TETile[][] w, Room start, Room end) {
        // roomWQU.union(start.number, end.number);

        Boolean wallBreak = false;

        if (start.center.getX() == end.center.getX() && start.center.getY() == end.center.getY()) {
            return;
        }
        if (start.center.getX() > end.center.getX()) {
            connect(w, end, start);
            return;
        }
        Point node = new Point(start.center.getX(), start.center.getY());
        while (node.getX() != end.center.getX()) {
            node.x += 1;
            if (w[node.getX()][node.getY()] == WALL && wallBreak && !isCorner(w, node)) {
                w[node.getX()][node.getY()] = Tileset.FLOOR;
                return;
            } else if (w[node.getX()][node.getY()] == WALL) {
                wallBreak = true;
                w[node.getX()][node.getY()] = Tileset.FLOOR;
            }
            w[node.getX()][node.getY()] = Tileset.FLOOR;
        }
//        wallBreak = false;
        if (node.getY() < end.center.getY()) {
            while (node.getY() != end.center.getY()) {
                node.y += 1;
                if (w[node.getX()][node.getY()] == Tileset.FLOOR && wallBreak) {
                    w[node.getX()][node.getY()] = Tileset.FLOOR;
                    return;
                }  else if (w[node.getX()][node.getY()] == WALL) {
                    wallBreak = true;
                    w[node.getX()][node.getY()] = Tileset.FLOOR;
                }
                w[node.getX()][node.getY()] = Tileset.FLOOR;
            }
        } else {
            while (node.getY() != end.center.getY()) {
                node.y -= 1;
                if (w[node.getX()][node.getY()] == Tileset.FLOOR && wallBreak) {
                    w[node.getX()][node.getY()] = Tileset.FLOOR;
                    return;
                } else if (w[node.getX()][node.getY()] == WALL) {
                    wallBreak = true;
                    w[node.getX()][node.getY()] = Tileset.FLOOR;
                }
                w[node.getX()][node.getY()] = Tileset.FLOOR;
            }
        }
        roomWQU.union(start.number, end.number);

    }

    private void treeConnect() {

        roomWQU = new WeightedQuickUnionUF(roomLoad);
        for (Room r1 : roomList) {
            for (Room r2 : roomList) {
                if (!roomWQU.connected(r1.number, r2.number)) {
                    connect(world, r1, r2);
                }
            }
        }
    }


    /** INTERACTIVITY BEGINS
     *
     *
     */

    private class KiwiBot {

        protected Integer battery;
        protected Point position;
        protected boolean hasFood;

        protected KiwiBot(int battery, Point position) {
            this.battery = battery;
            this.position = position;
            this.hasFood = false;
        }
    }

    private class Food {
        Point position;
        protected Food(Point position) {
            this.position = position;
        }
    }

    private class Customer {
        Point position;
        String[] truth;
        String question;

        protected Customer(Point position) {
            this.position = position;
            truth = new String[5];
            truth[0] = TRUE1;
            truth[1] = TRUE2;
            truth[2] = TRUE3;
            truth[3] = TRUE4;
            truth[4] = TRUE5;

            int a = uniform(random, 0, 5);
            question = truth[a];


        }
    }

    private class Thief {
        Point position;
        String[] fake;
        String question;

        protected Thief(Point position) {
            this.position = position;

            fake = new String[5];
            fake[0] = FALSE1;
            fake[1] = FALSE2;
            fake[2] = FALSE3;
            fake[3] = FALSE4;
            fake[4] = FALSE5;

            int b = uniform(random, 0, 5);
            question = fake[b];

        }


    }

    private void placeKiwi() {

        Point place = worldRandomPoint();
//        Point world[place.x][place.y];
//        if (world[place.x][place.y] == WALL || world[place.x][place.y] == NOTHING) {
//            placeKiwi();
//        } else if() {
//            world[place.x][place.y] = AVATAR;
//
//        }

        if (world[place.x][place.y] == FLOOR) {
            world[place.x][place.y] = AVATAR;
            kiwi = new KiwiBot(WIDTH * 3, place);
        } else {
            placeKiwi();
        }
    }

    private void placeFood() {
        Point place = worldRandomPoint();
        if (world[place.x][place.y] == FLOOR) {
            world[place.x][place.y] = FOOD;
            food = new Food(place);
        } else {
            placeFood();

        }
    }
    private void placeCustomer() {
        Point place = worldRandomPoint();
        if (world[place.x][place.y] == FLOOR) {
            world[place.x][place.y] = CUSTOMER;
            customer = new Customer(place);
        } else {
            placeCustomer();
        }

    }
    private void placeThief() {
        Point place = worldRandomPoint();
        if (world[place.x][place.y] == FLOOR) {
            world[place.x][place.y] = THIEF;
            thief = new Thief(place);
        } else {
            placeThief();
        }

    }

    private void stringToMove(String s) {
        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);


            if ((c == 'w' || c == 'W') && (world[kiwi.position.x][kiwi.position.y + 1] == FLOOR
                    || world[kiwi.position.x][kiwi.position.y + 1] == FOOD)) {
                world[kiwi.position.x][kiwi.position.y] = FLOOR;

                if (world[kiwi.position.x][kiwi.position.y + 1] == FOOD) {
                    kiwi.hasFood = true;
                }
                world[kiwi.position.x][kiwi.position.y + 1] = AVATAR;
                kiwi.position = new Point(kiwi.position.x, kiwi.position.y + 1);
                kiwi.battery -= 1;

            } else if ((c == 's' || c == 'S')
                    && (world[kiwi.position.x][kiwi.position.y - 1] == FLOOR
                    || world[kiwi.position.x][kiwi.position.y - 1] == FOOD)) {
                world[kiwi.position.x][kiwi.position.y] = FLOOR;

                if (world[kiwi.position.x][kiwi.position.y - 1] == FOOD) {
                    kiwi.hasFood = true;
                }
                world[kiwi.position.x][kiwi.position.y - 1] = AVATAR;
                kiwi.position = new Point(kiwi.position.x, kiwi.position.y - 1);
                kiwi.battery -= 1;

            } else if ((c == 'a' || c == 'A')
                    && (world[kiwi.position.x - 1][kiwi.position.y] == FLOOR
                    || world[kiwi.position.x - 1][kiwi.position.y] == FOOD)) {
                world[kiwi.position.x][kiwi.position.y] = FLOOR;

                if (world[kiwi.position.x - 1][kiwi.position.y] == FOOD) {
                    kiwi.hasFood = true;
                }
                world[kiwi.position.x - 1][kiwi.position.y] = AVATAR;
                kiwi.position = new Point(kiwi.position.x - 1, kiwi.position.y);
                kiwi.battery -= 1;

            } else if ((c == 'd' || c == 'D')
                    && (world[kiwi.position.x + 1][kiwi.position.y] == FLOOR
                    || world[kiwi.position.x + 1][kiwi.position.y] == FOOD)) {
                world[kiwi.position.x][kiwi.position.y] = FLOOR;

                if (world[kiwi.position.x + 1][kiwi.position.y] == FOOD) {
                    kiwi.hasFood = true;
                }
                world[kiwi.position.x + 1][kiwi.position.y] = AVATAR;
                kiwi.position = new Point(kiwi.position.x + 1, kiwi.position.y);
                kiwi.battery -= 1;
            }




            if (kiwi.battery == 0) {
                gameLost = true;
            }

            /**
            if (new Point(kiwi.position.x + 1, kiwi.position.y) == (thief.position)
                    || new Point(kiwi.position.x - 1, kiwi.position.y) == (thief.position)
                    || new Point(kiwi.position.x, kiwi.position.y + 1) == (thief.position)
                    || new Point(kiwi.position.x, kiwi.position.y - 1) == (thief.position)
                    && kiwi.hasFood) {

//                dialogueThief();

                dialogue = true;
            }
            */


            if ((world[kiwi.position.x + 1][kiwi.position.y] == CUSTOMER
                    || world[kiwi.position.x - 1][kiwi.position.y] == CUSTOMER
                    || world[kiwi.position.x][kiwi.position.y + 1] == CUSTOMER
                    || world[kiwi.position.x][kiwi.position.y - 1] == CUSTOMER)
                    && kiwi.hasFood) {

//                person = ;
                dialogue = true;
                nextTo = "customer";
//                dialogueMode();
//                gameWon = true;

            }
            if ((world[kiwi.position.x + 1][kiwi.position.y] == THIEF
                    || world[kiwi.position.x - 1][kiwi.position.y] == THIEF
                    || world[kiwi.position.x][kiwi.position.y + 1] == THIEF
                    || world[kiwi.position.x][kiwi.position.y - 1] == THIEF)
                    && kiwi.hasFood) {

//                person = ;
                dialogue = true;
                nextTo = "thief";
//                dialogueMode();
//                gameWon = true;

            }


            /**
            if (new Point(kiwi.position.x + 1, kiwi.position.y).equals(customer.position)
                    || new Point(kiwi.position.x - 1, kiwi.position.y).equals(customer.position)
                    || new Point(kiwi.position.x, kiwi.position.y + 1).equals(customer.position)
                    || new Point(kiwi.position.x, kiwi.position.y - 1).equals(customer.position)
                    && kiwi.hasFood) {

//                dialogueCustomer();
                dialogue = true;

            }
             */
//            if (getTileAtPoint(kiwi.position) == FOOD) {
//
//            }

        }

    }

    private void dialogueThief() {
        ter.renderFrame(world);
//        dialogue = true;
        while (dialogue) {

            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(WIDTH / 2, HEIGHT - 2, "Customer says: " + thief.question);
            StdDraw.text(WIDTH / 2 - 3, HEIGHT / 2, "(G)ive Food");
            StdDraw.text(WIDTH / 2 + 3, HEIGHT / 2, "(R)un");

            StdDraw.show();
            return;
        }

    }

    private void dialogueCustomer() {


        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 2, "Customer says: " + customer.question);
        StdDraw.text(WIDTH / 2 - 3, HEIGHT / 2, "(G)ive Food");
        StdDraw.text(WIDTH / 2 + 3, HEIGHT / 2, "(R)un");

        StdDraw.show();
        return;

    }


    private TETile getTileAtPoint(Point p) {
        return world[p.x][p.y];
    }

    /**
     * Mouse Functions
     */
    private void mouseWorker(double x, double y) {

        int xxPos = (int) x;
        int yyPos = (int) y;
        TETile result = world[0][0];

        if (xxPos >= 0 && xxPos < WIDTH && yyPos >= 0 && yyPos < HEIGHT) {
            result = world[xxPos][yyPos];
        }

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.line(0, HEIGHT, WIDTH, HEIGHT);

        int hudxxPos = 30;
        int hudyyPos = HEIGHT + 1;
        String w = "World: ";

        if (result.equals(Tileset.WALL)) {
            StdDraw.text(hudxxPos, hudyyPos, w + "WALL");
        } else if (result.equals(Tileset.FLOOR)) {
            StdDraw.text(hudxxPos, hudyyPos, w +"FLOOR");
        } else if (result.equals(Tileset.NOTHING)) {
            StdDraw.text(hudxxPos, hudyyPos, w +"NOTHING");
        } else if (result.equals(Tileset.AVATAR)) {
            StdDraw.text(hudxxPos, hudyyPos, w + name);
        } else if (result.equals(Tileset.FOOD)) {
            StdDraw.text(hudxxPos, hudyyPos, w +"FOOD");
        } else if (result.equals(Tileset.CUSTOMER)) {
            StdDraw.text(hudxxPos, hudyyPos, w + "CUSTOMER");
        } else if (result.equals(Tileset.THIEF)) {
            StdDraw.text(hudxxPos, hudyyPos, w + "CUSTOMER");
        }
    }






}
