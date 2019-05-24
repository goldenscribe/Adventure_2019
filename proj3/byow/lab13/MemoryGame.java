package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class MemoryGame {
    private int width;
    private int height;
    private Random random;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);

        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        Random r = new Random(seed);
        this.random = r;



    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        int size = CHARACTERS.length;
        String str = "";
        for (int i = 0; i < n; i++) {
            int index = uniform(random, size);
            char c = CHARACTERS[index];
            str += c;

        }
        return str;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen

        StdDraw.clear();
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.setPenColor();
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        StdDraw.clear();
        for (int i = 0; i < letters.length(); i++) {
//            StdDraw.clear();
            StdDraw.pause(500);
//            Font font = new Font("Arial", Font.BOLD, 30);
//            StdDraw.setFont(font);
            char show = letters.charAt(i);
            String s = Character.toString(show);
            drawFrame(s);
//            StdDraw.text(0.5, 0.5, s);
//            StdDraw.show();
            StdDraw.pause(1000);


        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
//        StdDraw.clear();
        String str = "";
//        Font font = new Font("Arial", Font.BOLD, 30);
//        StdDraw.setFont(font);
        while (StdDraw.hasNextKeyTyped() && n > 0) {
            StdDraw.pause(4000);
            char c = StdDraw.nextKeyTyped();
            str = str + c;
            drawFrame(str);
//            StdDraw.text(0.5, 0.5, str);
            n -= 1;
//            StdDraw.show();

        }
        return str;


    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        //TODO: Establish Engine loop
        round = 1;
        gameOver = false;

        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);
            StdDraw.pause(1000);
            String str = generateRandomString(round);
            flashSequence(str);
            playerTurn = true;
            if (playerTurn) {
                String input = solicitNCharsInput(str.length());

                if (!input.equals(str)) {
                    gameOver = true;
                } else {
                    round += 1;

                }
            }

        }

        System.out.println("Game Over! You made it to round: " + round);

    }



}
