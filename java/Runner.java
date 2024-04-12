import java.util.ArrayList;

public class Runner {
    public static void main(String[] args) {
        ArrayList<Game> games = new ArrayList<>();
        games.add(new Picross());
        while (games.getLast().play() == Game.State.PLAYING);
        System.out.println(games.getLast().score());
    }
}
