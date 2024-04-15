import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Runner {
    final static Class<?>[] gameList = {AgeGuess.class, Minesweeper.class, Picross.class, Wordle.class};
    static ArrayList<Game> games = new ArrayList<>();
    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        while (true) {
            menu();
        }
    }

    public static boolean menu() {
        System.out.println("\"old\" - play existing game");
        System.out.println("\"new\" - make a new game");
        System.out.println("\"x\" - exit");
        System.out.print("choice: ");
        String in = scan.nextLine().toLowerCase();
        System.out.println();
        if (in.equals("x")) {
            return false;
        } else if (in.equals("old")) {
            for (int i = 0; i < games.size(); i++) {
                System.out.println((i+1) + ". " + games.get(i));
            }
            System.out.print("enter game number: ");
            int n = scan.nextInt() - 1;
            System.out.println();
            play(games.get(n)); //TODO finish this
        } else if (in.equals("new")) {
            for (int i = 0; i < gameList.length; i++) {
                System.out.println((i+1) + ". " + gameList[i].getName());
            }
            System.out.print("enter choice: ");
            int n = scan.nextInt() - 1;
            System.out.println();
            try {
                games.add((Game) gameList[n].getConstructor().newInstance());
                play(games.getLast());
            } catch (Exception e) {}
        }
        System.out.println();
        return true;
    }

    public static void play(Game g) {
        while (g.state == Game.States.PLAYING) {
            g.play();
        }
    }
}
