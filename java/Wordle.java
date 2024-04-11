import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Scanner;

public class Wordle {
    static Scanner scan = new Scanner(System.in);
    static Stream<String> wordleLa, wordleTa;

    public static void main(String[] args) throws Exception { //TODO: reformat all the games to work with a runner
        Path cur = new File("").toPath().toAbsolutePath();
        wordleLa = Files.lines(cur.resolve("wordleDictionary/wordle-Ta.txt"));
        wordleTa = Files.lines(cur.resolve("wordleDictionary/wordle-Ta.txt"));
        
        wordleLa.skip((int)(Math.random() * wordleLa.count()));
        String word = wordleLa.findFirst().orElse(null);
        HashMap<Character, Integer> letterCount = new HashMap<>();

        int guesses = 0;
        String guess = "";

        while (guess != word) {
            guess = input();
            if (guess == null)
                continue;
            
        }
    }

    static String input() {
        String in = scan.nextLine();
        in.toLowerCase();
        if (in.length() != 5) {
            System.out.println("input is incorrect length");
        } else if (!isAlpha(in)) {
            System.out.println("guess must be letters");
        } else if (wordleLa.anyMatch(s -> s.equals(in)) || wordleTa.anyMatch(s -> s.equals(in))) {
            System.out.println("guess is not in word list");
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
