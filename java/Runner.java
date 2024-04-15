import java.io.IOException;
import java.util.ArrayList;

public class Runner {
    public static void main(String[] args) throws IOException {
        ArrayList<Game> games = new ArrayList<>();
        games.add(new Picross());
        while (games.getLast().play() == Game.State.PLAYING);
    }
}
