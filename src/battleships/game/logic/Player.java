package battleships.game.logic;
import java.util.List;

public class Player {

    private boolean playing;
    private List<Playground> savedGames;

    public Player(boolean playing, List<Playground> savedGames) {
        this.playing = playing;
        this.savedGames = savedGames;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public List<Playground> getSavedGames() {
        return savedGames;
    }

}
