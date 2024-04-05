import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class Picross {
    final static Random rand = new Random();
    final static Scanner scan = new Scanner(System.in);

    enum Marks {HIDDEN, FILLED, EMPTY, REVEALED};

    public static void main(String[] args) {
        int size = scanNum("enter board size");
        Board board = new Board(size);
        System.out.println("\n" + board);
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
                    int[] xy = scanNum();
                    board.markTile(xy[0], xy[1], Marks.FILLED);
                    break;
                } case "fr": {
                    int y = scanNum("y");
                    board.markRow(y, Marks.FILLED);
                    break;
                } case "fc": {
                    int x = scanNum("x");
                    board.markCol(x, Marks.FILLED);
                    break;
                } case "e": {
                    int[] xy = scanNum();
                    board.markTile(xy[0], xy[1], Marks.EMPTY);
                    break;
                } case "er": {
                    int y = scanNum("y");
                    board.markRow(y, Marks.EMPTY);
                    break;
                } case "ec": {
                    int x = scanNum("x");
                    board.markCol(x, Marks.EMPTY);
                    break;
                } case "u": {
                    int[] xy = scanNum();
                    board.markTile(xy[0], xy[1], Marks.HIDDEN);
                    break;
                } case "ur": {
                    int y = scanNum("y");
                    board.markRow(y, Marks.HIDDEN);
                    break;
                } case "uc": {
                    int x = scanNum("x");
                    board.markCol(x, Marks.HIDDEN);
                    break;
                } default:
                    continue;
            }
            
            System.out.println(board);
        }
    }

    public static int[] scanNum() {
        int x, y;
        while (true) {
            System.out.print("x,y: ");
            try {
                String[] nums = scan.nextLine().split(",");
                x = Integer.parseInt(nums[0]);
                y = Integer.parseInt(nums[1]);
                break;
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return new int[]{x,y};
    }

    public static int scanNum(String name) {
        System.out.print(name + ": ");
        int num;
        while (!scan.hasNextInt()) {
            System.out.print(name + ": ");
            scan.next();
        }
        num = scan.nextInt();
        scan.nextLine();
        return num;
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

        public boolean isHidden() {
            return mark == Marks.HIDDEN;
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
                    return "x";
                case REVEALED:
                    return filled ? "■" : "x";
                default:
                    return "_";
            }
        }
    }

    static class Board {
        private Tile[][] tiles;
        private String[] rowLabels;
        private String topLabel;

        public Board(int size) {
            tiles = new Tile[size][size];
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    tiles[i][j] = new Tile();
                }
            }
            rowLabels = countRows();
            topLabel = countCols(rowLabels[0].length());
        }

        private String[] countRows() {
            String[] rowLabels = new String[tiles.length];
            int max = 0;

            for (int y = 0; y < tiles.length; y++) {
                ArrayList<Integer> rowCount = new ArrayList<Integer>();
                boolean prev = false;
                rowLabels[y] = "";

                for (int x = 0; x < tiles.length; x++) {
                    if (tiles[y][x].isFilled()) {
                        if (prev) {
                            rowCount.set(0, rowCount.get(0) + 1);
                        } else {
                            rowCount.add(0, 1);
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

            return rowLabels;
        }

        private String countCols(int leftMargin) {
            int[][] colCounts = new int[tiles.length][];
            String topLabel = "";
            int max = 0;

            for (int x = 0; x < tiles.length; x++) {
                ArrayList<Integer> counts = new ArrayList<Integer>();
                int cnt = 0;

                for (int y = 0; y < tiles.length; y++) {
                    if (tiles[y][x].isFilled()) {
                        cnt++;
                    } else if (cnt > 0) {
                        counts.add(cnt);
                        cnt = 0;
                    }
                }

                if (cnt > 0) {
                    counts.add(cnt);
                }
                
                if (counts.size() > max) {
                    max = counts.size();
                }

                colCounts[x] = new int[counts.size()];
                for (int i = 0; i < counts.size(); i++) {
                    colCounts[x][i] = counts.get(counts.size() - i - 1);
                }
            }

            for (int i = 0; i < max; i++) {
                String s = " ".repeat(leftMargin);
                for (int x = 0; x < colCounts.length; x++) {
                    try {
                        s += colCounts[x][i] + " ";
                    } catch (ArrayIndexOutOfBoundsException e) {
                        s += "  ";
                    }
                }
                topLabel = s + "\n" + topLabel;
            }
            
            return topLabel;
        }

        public void markTile(int x, int y, Marks mark) {
            try {
                tiles[y-1][x-1].mark(mark);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("coordinates not on board!");
            }
        }

        public void markCol(int x, Marks mark) {
            for (int y = 0; y < tiles.length; y++)
                if (tiles[y][x].isHidden())
                    markTile(x, y, mark);
        }

        public void markRow(int y, Marks mark) {
            for (int x = 0; x < tiles.length; x++)
                if (tiles[y][x].isHidden())
                    markTile(x, y, mark);
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

        public String toString() {
            String out = topLabel;
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