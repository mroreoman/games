import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
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
        int guesses = 0;
        String guess = "";

        while (guess != word) {
            guess = input();

        }
    }

    static String input() {
        return scan.nextLine();
    }
}
