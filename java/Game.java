public interface Game {
    public enum State {LOST, WON, PLAYING};

    /**returns whether game should continue */
    public abstract State play();
    
    public abstract String score();
}
