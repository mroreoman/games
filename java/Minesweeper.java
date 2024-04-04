import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    enum Marks {HIDDEN, FLAGGED, REVEALED};

    enum Colors {
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

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Board b = new Board(9,9,10);
        System.out.println("\n" + b);
        play(b);
    }

    private static void play(Board board) {
        while (true) {
            System.out.print("move: ");
            String move = scan.nextLine().toLowerCase();

            if (move.equals("x"))
                return;

            int x, y;
            while (true) {
                System.out.print("x,y: ");
                String[] raw = scan.nextLine().split(",");
                try {
                    x = Integer.parseInt(raw[0]);
                    y = Integer.parseInt(raw[1]);
                } catch (NumberFormatException e) {
                    continue;
                }
                break;
            }
            
            
            
            switch (move) {
                case "c": {
                    if (board.clickTile(x, y)) {
                        System.out.println("you lost brah");
                        return;
                    }
                    break;
                } case "f": {
                    board.flagTile(x, y, true);
                    break;
                } case "u": {
                    board.flagTile(x, y, false);
                    break;
                } case "cn": {
                    if (board.clickNeighbors(x, y)) {
                        System.out.println("you lost brah");
                        return;
                    }
                    break;
                } case "fn": {
                    board.flagNeighbors(x, y, true);
                    break;
                } case "un": {
                    board.flagNeighbors(x, y, false);
                    break;
                } default:
                    continue;
            }
            
            System.out.println(board);
        }
    }

    private static class Tile {
        private boolean isMine = false;
        private int neighbors;
        private Marks mark = Marks.HIDDEN;

        public String toString() {
            switch (mark) {
                case Marks.HIDDEN:
                    return "_";
                case Marks.FLAGGED:
                    return "F";
                case Marks.REVEALED:
                    if (isMine) {
                        return Colors.RED + "x" + Colors.RESET;
                    } else {
                        return Colors.values()[neighbors] + String.valueOf(neighbors) + Colors.RESET;
                    }
                default:
                    return "_";
            }
        }
    }

    private static class Board {
        private Tile[][] tiles;

        public Board(int width, int height, int mines) {
            tiles = new Tile[height][width];
            setMines(mines);
            setNeighbors();
        }

        public void flagTile(int x, int y, boolean flag) {
            if (!tileInBounds(x, y))
                return;
            if (tiles[y][x].mark == Marks.REVEALED)
                return;
            
            tiles[y][x].mark = flag ? Marks.FLAGGED : Marks.HIDDEN;
        }

        // returns true if u die
        public boolean clickTile(int x, int y) {
            if (!tileInBounds(x, y))
                return false;
            if (tiles[y][x].mark != Marks.HIDDEN)
                return false;
                
            tiles[y][x].mark = Marks.REVEALED;
            if (tiles[y][x].isMine)
                return true;
            
            if (tiles[y][x].neighbors == 0)
                clickNeighbors(x, y);
            
            return false;
        }

        public void flagNeighbors(int x, int y, boolean flag) {
            if (tiles[y][x].mark != Marks.REVEALED)
                return;

            for (int yy = y-1; yy <= y+1; yy++) {
                for (int xx = x-1; xx <= x+1; xx++) {
                    if (yy == y && xx == x)
                        continue;
                    
                    flagTile(xx, yy, flag);
                }
            }
        }

        public boolean clickNeighbors(int x, int y) {
            if (tiles[y][x].mark != Marks.REVEALED)
                return false;

            for (int yy = y-1; yy <= y+1; yy++) {
                for (int xx = x-1; xx <= x+1; xx++) {
                    if (yy == y && xx == x)
                        continue;
                    
                    if (clickTile(xx, yy))
                        return true;
                }
            }

            return false;
        }

        private void setMines(int mines) {
            ArrayList<Tile> list = new ArrayList<Tile>();
            Random rand = new Random();

            for (int y = 0; y < tiles.length; y++) {
                for (int x = 0; x < tiles[y].length; x++) {
                    tiles[y][x] = new Tile();
                    list.add(tiles[y][x]);
                }
            }

            for (int i = 0; i < mines; i++) {
                int n = rand.nextInt(list.size());
                list.get(n).isMine = true;
                list.remove(n);
            }
        }

        private void setNeighbors() {
            for (int y = 0; y < tiles.length; y++) {
                for (int x = 0; x < tiles[y].length; x++) {
                    int neighbors = 0;

                    for (int yy = y-1; yy <= y+1; yy++) {
                        for (int xx = x-1; xx <= x+1; xx++) {
                            if (yy == y && xx == x)
                                continue;
                            if (!tileInBounds(xx, yy))
                                continue;
                            if (tiles[yy][xx].isMine)
                                neighbors++;
                        }
                    }

                    tiles[y][x].neighbors = neighbors;
                }
            }
        }

        private boolean tileInBounds(int x, int y) {
            if (y < 0 || y > tiles.length - 1)
                return false;
            if (x < 0 || x > tiles[y].length - 1)
                return false;
            return true;
        }

        public String toString() {
            String s = "  ";
            for (int i = 0; i < tiles[0].length; i++) {
                System.out.println(i);
                s += "h";
                // s += String.valueOf(i+1) + " ";
            }
            s = "\n";
            for (int y = 0; y < tiles.length; y++) {
                s += y + " ";
                for (int x = 0; x < tiles[y].length; x++) {
                    s += tiles[y][x] + " ";
                }
                s += "\n";
            }
            return s;
        }
    }
}
