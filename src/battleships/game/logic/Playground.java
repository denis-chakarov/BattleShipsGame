package battleships.game.logic;
public class Playground{
    private Rival firstPlayer;
    private Rival secondPlayer;
    private String name;
    private String status;
    private String turn;
    private int playersCount;

    public Playground(Rival firstPlayer, Rival secondPlayer, String name,
                      String status, int playersCount, String turn) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.name = name;
        this.status = status;
        this.playersCount = playersCount;
        this.turn = turn;
    }

    public static Playground copyPlayground(Playground playground, boolean isFirst) {
        GameTable current;
        GameTable other = new GameTable();
        String rivalName;
        boolean rivalFlag;
        if(isFirst) {
            current = playground.getFirstPlayer().getGameTable();
            rivalName = playground.getFirstPlayer().getName();
            rivalFlag = playground.getFirstPlayer().isFlag();
        }
        else {
            current = playground.getSecondPlayer().getGameTable();
            rivalName = playground.getSecondPlayer().getName();
            rivalFlag = playground.getSecondPlayer().isFlag();
        }
        other.setTable(current.getTable());
        Rival firstRival = new Rival(rivalName, rivalFlag, other);

        return new Playground(firstRival, null, playground.name,
                "pending", 1, "none");
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Playground that = (Playground) o;

        if (playersCount != that.playersCount) return false;
        if (!firstPlayer.equals(that.firstPlayer)) return false;
        if (secondPlayer != null ? !secondPlayer.equals(that.secondPlayer) : that.secondPlayer != null) return false;
        if (!name.equals(that.name)) return false;
        if (!status.equals(that.status)) return false;
        return turn.equals(that.turn);
    }

    @Override
    public int hashCode() {
        int result = firstPlayer.hashCode();
        result = 31 * result + (secondPlayer != null ? secondPlayer.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + turn.hashCode();
        result = 31 * result + playersCount;
        return result;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount += playersCount;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Rival getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Rival firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Rival getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Rival secondPlayer) {
        this.secondPlayer = secondPlayer;
    }
}
