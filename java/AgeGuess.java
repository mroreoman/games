import java.util.Scanner;
import java.util.Random;

public class AgeGuess {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();
    
        System.out.println("guess my age!");
        int age = rand.nextInt(100);
        int guess = -1;
        int guesses = 0;

        while (guess != age) {
            guesses++;
            System.out.print("guess: ");
            guess = scan.nextInt();
            if (guess > age) {
                System.out.println("too high!");
            } else if (guess < age) {
                System.out.println("too low!");
            } else {
                break;
            }
        }

        System.out.println("you got it! it took " + guesses + " tries!");
        scan.close();
    }
}