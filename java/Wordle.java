import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Wordle implements Game {
    private final static Scanner scan = new Scanner(System.in);
    private final static Random rand = new Random();
    private static List<String> wordleLa, wordleTa;

    private String word;
    private int guesses = 0;

    public Wordle() throws IOException {
        Path cur = new File("").toPath().toAbsolutePath();
        wordleLa = Files.readAllLines(cur.resolve("wordleDictionary/wordle-La.txt"));
        wordleTa = Files.readAllLines(cur.resolve("wordleDictionary/wordle-Ta.txt"));
        
        word = wordleLa.get(rand.nextInt(wordleLa.size()));
    }

    public State play() {
        String guess = input();
        guesses++;
        
        if (guess.equals(word)) {
            System.out.println(Colors.GREEN + guess + Colors.RESET);
            return State.WON;
        }
        
        System.out.println(checkGuess(guess));
        return State.PLAYING;
    }

    public String score() {
        return "guesses: " + guesses;
    }

    private String checkGuess(String guess) {
        ArrayList<Character> letters = new ArrayList<>(5);
        for (char c : word.toCharArray()) {
            letters.add(c);
        }

        int i = 0;
        for (char c : guess.toCharArray()) {
            if (c == word.charAt(i++)) {
                letters.remove(Character.valueOf(c));
            }
        }
        
        String s = "";
        for (i = 0; i < word.length(); i++) {
            char c = guess.charAt(i);
            if (c == word.charAt(i)) {
                s += Colors.GREEN + String.valueOf(c) + Colors.RESET;
            } else if (letters.remove(Character.valueOf(c))) {
                s += Colors.YELLOW + String.valueOf(c) + Colors.RESET;
            } else {
                s += Colors.RED + String.valueOf(c) + Colors.RESET;
            }
        }
        return s;
    }

    private static String input() {
        while (true) {
            System.out.print("guess: ");
            String in = scan.nextLine();
            in.toLowerCase();
            if (in.length() != 5) {
                System.out.println("input is incorrect length");
            } else if (!isAlpha(in)) {
                System.out.println("guess must be letters");
            } else if (!wordleLa.contains(in) && !wordleTa.contains(in)) {
                System.out.println("guess is not in word list");
            } else {
                return in;
            }
        }
    }

    static boolean isAlpha(String s) {
        for (char c : s.toCharArray())
            if (!Character.isLetter(c))
                return false;
        
        return true;
    }
}
