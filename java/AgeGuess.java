import java.util.Scanner;
import java.util.Random;

public class AgeGuess implements Game {
    private final static Scanner scan = new Scanner(System.in);
    private final static Random rand = new Random();

    private final int age = rand.nextInt(100);
    private int guess = 0;
    private int guesses = 0;

    public AgeGuess() {
        System.out.println("guess my age!");
    }

    public State play() {
        System.out.print("guess: ");
        guess = scan.nextInt();
        if (guess > age) {
            System.out.println("too high!");
        } else if (guess < age) {
            System.out.println("too low!");
        } else {
            return State.PLAYING;
        }
        guesses++;
        return State.WON;
    }

    public String score() {
        return "you took " + guesses + " guesses";
    }
}