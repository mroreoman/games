import java.io.IOException;
import java.util.ArrayList;

public class Runner {
    public static void main(String[] args) throws IOException {
        ArrayList<Game> games = new ArrayList<>();
        games.add(new Wordle());
        while (games.getLast().play() == Game.State.PLAYING);
        System.out.println(games.getLast().score());
    }
}
