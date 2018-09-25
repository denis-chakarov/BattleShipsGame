package battleships.game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDatabase {
    private Map<String, Player> players;
    private List<Playground> activeGames;
    private List<String> logged;

    public GameDatabase() {
        players = new HashMap<>();
        activeGames = new ArrayList<>();
        logged = new ArrayList<>();
        players.put("gosho", new Player(false, new ArrayList<>()));
        players.put("pesho", new Player(false, new ArrayList<>()));
        players.put("severina", new Player(false, new ArrayList<>()));
        players.put("katq", new Player(false, new ArrayList<>()));
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public List<Playground> getActiveGames() {
        return activeGames;
    }

    public List<String> getLogged() {
        return logged;
    }
}
