import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Wordle implements Game {
    private final static Scanner scan = new Scanner(System.in);
    private final static Random rand = new Random();
    private static List<String> wordleLa, wordleTa;

    private String word;
    private int guesses = 0;
    private HashMap<Character, Integer> _charCounts;

    public Wordle() throws IOException {
        Path cur = new File("").toPath().toAbsolutePath();
        wordleLa = Files.readAllLines(cur.resolve("wordleDictionary/wordle-Ta.txt"));
        wordleTa = Files.readAllLines(cur.resolve("wordleDictionary/wordle-Ta.txt"));
        
        word = wordleLa.get(rand.nextInt(wordleLa.size()));
        _charCounts = new HashMap<>();
        for (char c : word.toCharArray()) {
            if (_charCounts.containsKey(c)) {
                _charCounts.put(c, _charCounts.get(c) + 1);
            } else {
                _charCounts.put(c, 1);
            }
        }
    }

    public State play() {
        String guess = input();

        if (guess == null) {
            return State.PLAYING;
        }
        
        if (guess.equals(word)) {
            System.out.println(Colors.GREEN + guess + Colors.RESET);
            return State.WON;
        }
        
        HashMap<Character, Integer> charCounts = (HashMap) _charCounts.clone();
        for (int i = 0; i < guess.length(); i++) {
            char c = guess.toCharArray()[i];
            if (c == word.toCharArray()[i]) {
                System.out.print(Colors.GREEN + String.valueOf(c) + Colors.RESET);
                charCounts.put(c, charCounts.get(c) - 1);
            } else if (charCounts.get(c) > 0) {
                System.out.print(Colors.YELLOW + String.valueOf(c) + Colors.RESET);
                charCounts.put(c, charCounts.get(c) - 1);
            } else {
                System.out.print(Colors.RED + String.valueOf(c) + Colors.RESET);
            }
        }
        
        guesses++;
        return State.PLAYING;
    }

    public String score() {
        return "guesses: " + guesses;
    }


    private static String input() {
        System.out.print("guess: ");
        String in = scan.nextLine();
        in.toLowerCase();
        if (in.length() != 5) {
            System.out.println("input is incorrect length");
        } else if (!isAlpha(in)) {
            System.out.println("guess must be letters");
        } else if (!wordleLa.contains(in) && !wordleTa.contains(in)) {
            System.out.println("guess is not in word list"); //TODO crane not in word list
        } else {
            return in;
        }
        return null;
    }

    static boolean isAlpha(String s) {
        char[] chars = s.toCharArray();

        for (char c : chars)
            if (!Character.isLetter(c))
                return false;
        
        return true;
    }
}
