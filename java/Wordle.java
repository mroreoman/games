import java.io.File;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Wordle {
    public static void main(String[] args) throws Exception {
        Path cur = new File("").toPath().toAbsolutePath();
        Stream<String> wordleLa = Files.lines(cur.resolve("wordleDictionary/wordle-Ta.txt"));
        Stream<String> wordleTa = Files.lines(cur.resolve("wordleDictionary/wordle-Ta.txt"));

        wordleTa.forEach(s -> System.out.println(s));
    }   
}
