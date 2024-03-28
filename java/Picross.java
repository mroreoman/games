import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class Picross {
    final static Random rand = new Random();
    final static Scanner scan = new Scanner(System.in);

    enum Marks {HIDDEN, FILLED, EMPTY};

    public static void main(String[] args) {
        System.out.print("enter board size: ");
        int size = scan.nextInt();
        scan.nextLine();
        Board board = new Board(size);

        System.out.println(board);
        play(board);
    }

    private static void play(Board board) {
        String in;
        while (true) {
            System.out.print("move: ");
            in = scan.nextLine();
            
            switch (in.toLowerCase()) {
                case "x":
                    return;
                case "f": {
                    int[] xy = scanXY();
                    board.markTile(xy[0], xy[1], Marks.FILLED);
                    break;
                } case "fr": {
                    int y = scanY();
                    board.markRow(y, Marks.FILLED);
                    break;
                } case "fc": {
                    int x = scanX();
                    board.markCol(x, Marks.FILLED);
                    break;
                } case "e": {
                    int[] xy = scanXY();
                    board.markTile(xy[0], xy[1], Marks.EMPTY);
                    break;
                } case "er": {
                    int y = scanY();
                    board.markRow(y, Marks.EMPTY);
                    break;
                } case "ec": {
                    int x = scanX();
                    board.markCol(x, Marks.EMPTY);
                    break;
                } case "u": {
                    int[] xy = scanXY();
                    board.markTile(xy[0], xy[1], Marks.HIDDEN);
                    break;
                } case "ur": {
                    int y = scanY();
                    board.markRow(y, Marks.HIDDEN);
                    break;
                } case "uc": {
                    int x = scanX();
                    board.markCol(x, Marks.HIDDEN);
                    break;
                } default:
                    continue;
            }
            
            System.out.println(board);
        }
    }

    public static int[] scanXY() {
        System.out.print("x,y: ");
        String[] coords = scan.nextLine().split(",");
        int x = Integer.parseInt(coords[0]) - 1;
        int y = Integer.parseInt(coords[1]) - 1;
        return new int[]{x,y};
    }

    public static int scanX() {
        System.out.print("x: ");
        String coord = scan.nextLine();
        int x = Integer.parseInt(coord) - 1;
        return x;
    }

    public static int scanY() {
        System.out.print("y: ");
        String coord = scan.nextLine();
        int y = Integer.parseInt(coord) - 1;
        return y;
    }

    static class Tile {
        private Marks mark = Marks.HIDDEN;
        private boolean filled = rand.nextBoolean();

        public void mark(Marks mark) {
            this.mark = mark;
        }

        public boolean isFilled() {
            return filled;
        }

        public boolean isSolved() {
            if (filled) {
                return mark == Marks.FILLED;
            } else {
                return mark != Marks.FILLED;
            }
        }

        public String toString() {
            switch (mark) {
                case FILLED:
                    return "■";
                case EMPTY:
                    return "□"; //TODO: prints as ?
                default:
                    return "_";
            }
        }
    }

    static class Board {
        private Tile[][] tiles;
        private String[] rowLabels;
        private int[] colCounts;

        public Board(int size) {
            tiles = new Tile[size][size];
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    tiles[i][j] = new Tile();
                }
            }
            countRows();
            countCols();
        }

private void countRows() {
    rowLabels = new String[tiles.length];
    int max = 0;

    for (int y = 0; y < tiles.length; y++) {
        ArrayList<Integer> rowCount = new ArrayList<Integer>();
        boolean prev = false;
        rowLabels[y] = "";

        for (int x = 0; x < tiles.length; x++) {
            if (tiles[y][x].isFilled()) {
                if (prev) {
                    rowCount.set(rowCount.size() - 1, rowCount.getLast() + 1);
                } else {
                    rowCount.add(1);
                    prev = true;
                }
            } else {
                prev = false;
            }
        }

        if (rowCount.size() > max) {
            max = rowCount.size();
        }

        for (int i = 0; i < rowCount.size(); i++) {
            rowLabels[y] = rowCount.get(i) + " " + rowLabels[y];
        }
    }
    
    for (int i = 0; i < rowLabels.length; i++) {
        if (max * 2 > rowLabels[i].length()) {
            rowLabels[i] = " ".repeat(max * 2 - rowLabels[i].length()) + rowLabels[i];
        }
    }
}

        private void countCols() {
            colCounts = new int[tiles.length];
            for (int x = 0; x < colCounts.length; x++) {
                for (int y = 0; y < colCounts.length; y++) {
                    if (tiles[y][x].isFilled()) {
                        colCounts[x]++;
                    }
                }
            }
        }

        public void markTile(int x, int y, Marks mark) {
            try {
                tiles[y][x].mark(mark);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("coordinates not in board!");
            }
        }

        public void markCol(int x, Marks mark) {
            for (int y = 0; y < tiles.length; y++) {
                markTile(x, y, mark);
            }
        }

        public void markRow(int y, Marks mark) {
            for (int x = 0; x < tiles.length; y++) {
                markTile(x, y, mark);
            }
        }

        public boolean isSolved() {
            for (Tile[] row : tiles) {
                for (Tile tile : row) {
                    if (!tile.isSolved()) {
                        return false;
                    }
                }
            }
            return true;
        }

        public String toString() { //TODO: add row/col counts
            String out = "";
            for (int y = 0; y < tiles.length; y++) {
                out += rowLabels[y];
                for (int x = 0; x < tiles.length; x++) {
                    out += tiles[y][x] + " ";
                }
                out += "\n";
            }
            return out;
        }
    }
}