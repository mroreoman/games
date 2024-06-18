import java.util.Scanner;
import java.util.Random;

public class AgeGuess extends Game {
    private final static Scanner scan = new Scanner(System.in);
    private final static Random rand = new Random();

    private final int age = rand.nextInt(100);
    private int guess = 0;
    private int guesses = 0;

    public AgeGuess() {
        System.out.println("guess my age!");
        state = States.PLAYING;
    }

    public void play() {
        guesses++;
        System.out.print("guess: ");
        guess = scan.nextInt();
        scan.nextLine();
        if (guess > age) {
            System.out.println("too high!");
        } else if (guess < age) {
            System.out.println("too low!");
        } else {
            System.out.println("you won! it took " + guesses + " guesses.");
            state = States.WON;
        }
    }

    public String toString() {
        return "AgeGuess" + super.toString();
    }
}