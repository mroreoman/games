public abstract class Game {
    public enum States {LOST, WON, PLAYING, PAUSED};

    public enum Colors {
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

    protected States state;

    public abstract void play();

    public String toString() {
        return hashCode() + " - " + state;
    }
}
