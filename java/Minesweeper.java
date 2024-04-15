import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper extends Game {
    private static enum Mark {HIDDEN, FLAGGED, REVEALED};

    private static class Tile {
        private boolean isMine = false;
        private int neighbors;
        private Mark mark = Mark.HIDDEN;

        public String toString() {
            switch (mark) {
                case Mark.HIDDEN:
                    return "_";
                case Mark.FLAGGED:
                    return Colors.RED + "F" + Colors.RESET;
                case Mark.REVEALED:
                    if (isMine) {
                        return Colors.RED + "x" + Colors.RESET;
                    } else {
                        return Colors.values()[neighbors] + String.valueOf(neighbors) + Colors.RESET;
                    }
                default:
                    return "_";
            }
        }

        public boolean isCorrect() {
            if (isMine) {
                return mark != Mark.REVEALED;
            } else {
                return mark == Mark.REVEALED;
            }
        }
    }

    private final static Scanner scan = new Scanner(System.in);
    private final static Random rand = new Random();

    private Tile[][] board;
    private int mines;
    private boolean alive = true;
    private int moves = 0;
    private int flags = 0;

    public Minesweeper() {
        state = States.PLAYING;
        board = new Tile[9][9];
        mines = 10;
        ArrayList<Tile> list = new ArrayList<Tile>();

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new Tile();
                list.add(board[y][x]);
            }
        }
        
        System.out.println(board());
        System.out.println("first click: ");
        int[] coords = scanClick();
        list.remove(board[coords[1]][coords[0]]);

        for (int i = 0; i < mines; i++) {
            int n = rand.nextInt(list.size());
            list.get(n).isMine = true;
            list.remove(n);
        }

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x].neighbors = countNeighbors(x, y);
            }
        }

        clickTile(coords[0], coords[1]);
        moves++;
        System.out.println(board());
    }

    public void play() {
        System.out.print("move: ");
        String move = scan.nextLine().toLowerCase();

        if (move.equals("x"))
            state = States.PAUSED;

        int[] coords = scanClick();
        int x = coords[0];
        int y = coords[1];
        Mark old = board[y][x].mark;
        
        switch (move) {
            case "c": {
                state = clickTile(x, y);
                break;
            } case "f": {
                flagTile(x, y, true);
                break;
            } case "u": {
                flagTile(x, y, false);
                break;
            } case "cn": {
                state = clickNeighbors(x, y);
                break;
            } case "fn": {
                flagNeighbors(x, y, true);
                break;
            } case "un": {
                flagNeighbors(x, y, false);
                break;
            }
        }

        if (board[y][x].mark != old) {
            moves++;
            if (old == Mark.FLAGGED) {
                flags--;
            } else if (board[y][x].mark == Mark.FLAGGED) {
                flags++;
            }
        }
        
        System.out.println(board());
    }

    /**returns array of {x,y} */
    private int[] scanClick() {
        int x, y;
        while (true) {
            System.out.print("x,y: ");
            String[] raw = scan.nextLine().split(",");
            try {
                x = Integer.parseInt(raw[0].strip());
                y = Integer.parseInt(raw[1].strip());
            } catch (Exception e) {
                continue;
            }
            if (!tileInBounds(x, y))
                continue;
            break;
        }

        return new int[]{x, y};
    }

    private int countNeighbors(int x, int y) {
        int neighbors = 0;
        for (int yy = y-1; yy <= y+1; yy++) {
            for (int xx = x-1; xx <= x+1; xx++) {
                if (yy == y && xx == x)
                    continue;
                if (!tileInBounds(xx, yy))
                    continue;
                if (board[yy][xx].isMine)
                    neighbors++;
            }
        }
        return neighbors;
    }

    public void flagTile(int x, int y, boolean flag) {
        if (!tileInBounds(x, y))
            return;
        if (board[y][x].mark == Mark.REVEALED)
            return;
        
        board[y][x].mark = flag ? Mark.FLAGGED : Mark.HIDDEN;
    }

    public States clickTile(int x, int y) {
        if (!tileInBounds(x, y))
            return States.PLAYING;
        
        if (board[y][x].mark != Mark.HIDDEN)
            return States.PLAYING;
        
        board[y][x].mark = Mark.REVEALED;
        if (board[y][x].isMine) {
            System.out.println("you lost brah");
            return States.LOST;
        }
        
        if (board[y][x].neighbors == 0)
            clickNeighbors(x, y);
        
        if (won()) {
            System.out.println("you won! it took " + moves + " moves.");
            return States.WON;
        }
        
        return States.PLAYING;
    }

    public void flagNeighbors(int x, int y, boolean flag) {
        if (board[y][x].mark != Mark.REVEALED)
            return;

        for (int yy = y-1; yy <= y+1; yy++) {
            for (int xx = x-1; xx <= x+1; xx++) {
                if (yy == y && xx == x)
                    continue;
                
                flagTile(xx, yy, flag);
            }
        }
    }

    public States clickNeighbors(int x, int y) {
        if (board[y][x].mark != Mark.REVEALED)
            return States.PLAYING;

        for (int yy = y-1; yy <= y+1; yy++) {
            for (int xx = x-1; xx <= x+1; xx++) {
                if (yy == y && xx == x)
                    continue;
                
                States s = clickTile(xx, yy);
                if (s != States.PLAYING)
                    return s;
            }
        }

        return States.PLAYING;
    }

    public boolean won() {
        if (!alive)
            return false;
        
        for (Tile[] row : board)
            for (Tile tile : row)
                if (!tile.isCorrect())
                    return false;

        return true;
    }

    private boolean tileInBounds(int x, int y) {
        if (y < 0 || y > board.length - 1)
            return false;
        if (x < 0 || x > board[y].length - 1)
            return false;
        return true;
    }

    public String board() {
        String s = "mines: " + (mines - flags) + "\t" + (alive ? ":)" : ":(") + "\tmoves: " + moves + "\n\n";
        s += "\t";
        for (int i = 0; i < board[0].length; i++) {
            s += i + " ";
        }
        s += "\n\n";
        for (int y = 0; y < board.length; y++) {
            s += y + "\t";
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == null) {
                    s += "_" + " ";
                } else {
                    s += board[y][x] + " ";
                }
            }
            s += "\n";
        }
        return s;
    }

    public String toString() {
        return "Minesweeper" + super.toString();
    }
}