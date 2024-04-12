import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper implements Game {
    private static enum Mark {HIDDEN, FLAGGED, REVEALED};

    private static enum Colors {
        WHITE("\u001B[37m"),
        BLUE("\u001B[34m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        MAGENTA("\u001B[35m"),
        RED("\u001B[31m"),
        CYAN("\u001B[36m"),
        GREY("\u001B[37m"),
        BLACK("\u001B[30m"),
        RESET("\u001B[0m");
        
        private String s;
        
        Colors(String s) {
            this.s = s;
        }
        
        public String toString() {
            return s;
        }
    }

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
        board = new Tile[9][9];
        mines = 10;
        ArrayList<Tile> list = new ArrayList<Tile>();

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new Tile();
                list.add(board[y][x]);
            }
        }
        
        System.out.println(sBoard());
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
        System.out.println(sBoard());
    }

    public State play() {
        System.out.print("move: ");
        String move = scan.nextLine().toLowerCase();

        if (move.equals("x"))
            return State.PAUSED;

        int[] coords = scanClick();
        int x = coords[0];
        int y = coords[1];
        State s = State.PLAYING;
        Mark old = board[y][x].mark;
        
        switch (move) {
            case "c": {
                s = clickTile(x, y);
                break;
            } case "f": {
                flagTile(x, y, true);
                break;
            } case "u": {
                flagTile(x, y, false);
                break;
            } case "cn": {
                s = clickNeighbors(x, y);
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
        
        System.out.println(sBoard());
        return s;
    }

    public String score() {
        if (won()) {
            return "you won! it took " + moves + " moves.";
        } else {
            return "you lost brah";
        }
    }

    /**returns array of {x,y} */
    private int[] scanClick() {
        int x, y;
        while (true) {
            System.out.print("x,y: ");
            String[] raw = scan.nextLine().split(",");
            try {
                x = Integer.parseInt(raw[0].trim());
                y = Integer.parseInt(raw[1].trim());
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

    public State clickTile(int x, int y) {
        if (!tileInBounds(x, y))
            return State.PLAYING;
        
        if (board[y][x].mark != Mark.HIDDEN)
            return State.PLAYING;
        
        board[y][x].mark = Mark.REVEALED;
        if (board[y][x].isMine)
            return State.LOST;
        
        if (board[y][x].neighbors == 0)
            clickNeighbors(x, y);
        
        if (won())
            return State.WON;
        
        return State.PLAYING;
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

    public State clickNeighbors(int x, int y) {
        if (board[y][x].mark != Mark.REVEALED)
            return State.PLAYING;

        for (int yy = y-1; yy <= y+1; yy++) {
            for (int xx = x-1; xx <= x+1; xx++) {
                if (yy == y && xx == x)
                    continue;
                
                State s = clickTile(xx, yy);
                if (s != State.PLAYING)
                    return s;
            }
        }

        return State.PLAYING;
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

    public String sBoard() {
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
}