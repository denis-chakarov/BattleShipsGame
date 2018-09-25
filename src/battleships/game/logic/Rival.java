package battleships.game.logic;

public class Rival {
    private String name = "";
    private boolean flag;
    private GameTable  gameTable;
    public Rival(String name, boolean flag, GameTable gameTable) {
        this.name = name;
        this.flag = flag;
        this.gameTable = gameTable;
    }
    public Rival() {
        this.name = "";
        this.flag = false;
        gameTable = null;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public GameTable getGameTable() {
        return gameTable;
    }

    public void setGameTable(GameTable gameTable) {
        this.gameTable = gameTable;
    }
}
