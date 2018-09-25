package battleships.game.tests;

import battleships.game.logic.GameParser;
import battleships.game.logic.GameTable;
import battleships.game.logic.Playground;
import battleships.game.logic.Rival;
import battleships.network.logic.GameClient;
import battleships.network.logic.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class Tests {

    private static GameClientListener listener;
    private static GameParser gameParser;
    private Playground saved;
    @Before
    public void intitializer() throws IOException{
        ClientInfo clientInfo = new ClientInfo();
        gameParser = new GameParser();
        gameParser.setUser("pesho");
        Rival rival = new Rival("pesho", false, new GameTable());
        saved = new Playground(rival, null, "peshoGame", "pending", 1, "none");
        gameParser.getGameDatabase().getPlayers().get("pesho").getSavedGames().add(saved);
        listener = null;

    }
    @Test
    public void testFunctionAttackedFromGameTable() {
        GameTable gameTable = new GameTable();
        gameTable.getTable()[1][2] = '#';
        gameTable.attacked("B3");
        Assert.assertTrue(gameTable.getTable()[1][2] == 'X');
    }

    @Test
    public void testFunctionPrintTableFromGameTable() {
        GameTable gameTable = new GameTable();
        String result = gameTable.printTable();
        Assert.assertTrue(result.contains("_|") && !result.contains("."));
    }
    @Test
    public void testLostFunctionFromGameTable() {
        GameTable gameTable = new GameTable();
        Assert.assertTrue(!gameTable.lost());
    }

    @Test
    public void testGameMenuFunctionFromGameParserCaseCreateGame() throws IOException{
        String result;
        result = gameParser.parseMessage("create-game mygame", listener);
        String cmp = "Created game "+ "mygame" +", players 1/2\n";
        Assert.assertTrue(result.equals(cmp));
    }

    @Test
    public void testGameMenuFunctionFromGameParserCaseJoinGame() {
        Rival first = new Rival("gosho", false, new GameTable());
        Playground mygame = new Playground(first, null,
                "mygame", "pending", 1, "none");
        gameParser.getGameDatabase().getActiveGames().add(mygame);
        String result = gameParser.parseMessage("join-game mygame", listener);
        String cmp = "Joined game " + "mygame" + " PLAYERS 2/2, type start to start the game\n";
        Assert.assertTrue(result.equals(cmp) && mygame.getSecondPlayer() != null
                && mygame.getStatus().equals("in progress") && mygame.getPlayersCount() == 2);
    }

    @Test
    public void testGameMenuFunctionFromGameParserCaseListGames() {
        Rival first = new Rival("gosho", false, new GameTable());
        Playground mygame = new Playground(first, null,
                "mygame", "pending", 1, "none");
        gameParser.getGameDatabase().getActiveGames().add(mygame);
        String result = gameParser.parseMessage("list-games", listener);
        String cmp = "| NAME     | CREATOR | STATUS      | PLAYERS |\\n" +
                "|----------+---------+-------------+---------|\\n| mygame  | gosho  | pending  | 1/2  |\\n\n";
        System.out.println(result);
        System.out.println(cmp);
        Assert.assertTrue(result.equals(cmp));
    }

    @Test
    public void testGameMenuFunctionFromGameParserCaseSavedGames() {
        String result = gameParser.parseMessage("saved-games", listener);
        Assert.assertTrue(result.equals("peshoGame\\n\n"));
    }

    @Test
    public void testGameMenuFunctionFromGameParserCaseLoadGame() {
        String result = gameParser.parseMessage("load-game peshoGame", listener);
        String cmp = "type \"start\" to continue\n";
        Assert.assertTrue(result.equals(cmp) &&
                gameParser.getGameDatabase().getPlayers().get("pesho").getSavedGames().isEmpty()
        && gameParser.getGameDatabase().getActiveGames().size() > 0);
    }

    @Test public void testGameMenuFunctionFromGameParserCaseDeleteGame() {
        String result = gameParser.parseMessage("delete-game peshoGame", listener);
        String cmp = "Game deleted!\n";
        Assert.assertTrue(result.equals(cmp)
                && gameParser.getGameDatabase().getPlayers().get("pesho").getSavedGames().isEmpty());
    }

    @Test
    public void testGameMenuFunctionFromGameParserNoCase() {
        String result = gameParser.parseMessage("create-gam", listener);
        String cmp = "Unavailable command!\n";
        Assert.assertTrue(result.equals(cmp));
    }

    @Test
    public void testGameBattleFunctionFromGameParserCaseStartNoSecondPlayer() {
        gameParser.getGameDatabase().getActiveGames().add(saved);
        gameParser.getGameDatabase().getPlayers().get("pesho").setPlaying(true);
        String result = gameParser.parseMessage("start", listener);
        String cmp = "No second player!\n";
        Assert.assertTrue(result.equals(cmp));
    }
    @Test
    public void testGameBattleFunctionFromGameParserCaseStartWithSecondPlayer() {
        saved.setPlayersCount(1);
        gameParser.getGameDatabase().getActiveGames().add(saved);
        gameParser.getGameDatabase().getPlayers().get("pesho").setPlaying(true);
        String result = gameParser.parseMessage("start", listener);
        String cmp = "   1 2 3 4 5 6 7 8 9 10\\n" +
                "   _ _ _ _ _ _ _ _ _ _\\n" +
                "A |_|_|_|_|_|_|_|_|_|_|\\n" +
                "B |_|_|_|_|_|_|_|_|_|_|\\n" +
                "C |_|_|_|_|_|_|_|_|_|_|\\n" +
                "D |#|_|_|_|_|_|_|_|_|_|\\n" +
                "E |#|_|_|_|_|_|_|_|_|_|\\n" +
                "F |_|_|_|_|_|_|_|_|_|_|\\n" +
                "G |_|_|_|_|_|_|_|_|_|_|\\n" +
                "H |_|_|_|_|_|_|_|_|_|_|\\n" +
                "I |_|_|_|_|_|_|_|_|_|_|\\n" +
                "J |_|_|_|_|_|_|_|_|_|_|\\n" +
                "\\nEnter your turn \n";
        Assert.assertTrue(result.equals(cmp));
    }
    @Test
    public void testGameBattleFunctionFromGameParserAttackCommands() {
        saved.setPlayersCount(1);
        gameParser.getGameDatabase().getPlayers().get("pesho").setPlaying(true);
        gameParser.getGameDatabase().getPlayers().get("gosho").setPlaying(true);
        Rival second = new Rival("gosho", false, new GameTable());
        saved.setSecondPlayer(second);
        saved.setStatus("in progress");
        gameParser.getGameDatabase().getActiveGames().add(saved);
        String result1 = gameParser.parseMessage("D1", listener);
        String result2 = gameParser.parseMessage("C2", listener);
        gameParser.setUser("gosho");
        String result3 = gameParser.parseMessage("E1", listener);
        gameParser.setUser("pesho");
        String result4 = gameParser.parseMessage("E1", listener);
        String cmp1 = "gosho:   1 2 3 4 5 6 7 8 9 10\\n" +
                "   _ _ _ _ _ _ _ _ _ _\\n" +
                "A |_|_|_|_|_|_|_|_|_|_|\\n" +
                "B |_|_|_|_|_|_|_|_|_|_|\\n" +
                "C |_|_|_|_|_|_|_|_|_|_|\\n" +
                "D |X|_|_|_|_|_|_|_|_|_|\\n" +
                "E |#|_|_|_|_|_|_|_|_|_|\\n" +
                "F |_|_|_|_|_|_|_|_|_|_|\\n" +
                "G |_|_|_|_|_|_|_|_|_|_|\\n" +
                "H |_|_|_|_|_|_|_|_|_|_|\\n" +
                "I |_|_|_|_|_|_|_|_|_|_|\\n" +
                "J |_|_|_|_|_|_|_|_|_|_|\\n" +
                "\\npesho's last turn D1\\n" +
                "Enter your turn \n";
        String cmp2 = "Wait for the other player to make his/her turn!\n";
        String cmp3 = "pesho:   1 2 3 4 5 6 7 8 9 10\\n" +
                "   _ _ _ _ _ _ _ _ _ _\\n" +
                "A |_|_|_|_|_|_|_|_|_|_|\\n" +
                "B |_|_|_|_|_|_|_|_|_|_|\\n" +
                "C |_|_|_|_|_|_|_|_|_|_|\\n" +
                "D |#|_|_|_|_|_|_|_|_|_|\\n" +
                "E |X|_|_|_|_|_|_|_|_|_|\\n" +
                "F |_|_|_|_|_|_|_|_|_|_|\\n" +
                "G |_|_|_|_|_|_|_|_|_|_|\\n" +
                "H |_|_|_|_|_|_|_|_|_|_|\\n" +
                "I |_|_|_|_|_|_|_|_|_|_|\\n" +
                "J |_|_|_|_|_|_|_|_|_|_|\\n" +
                "\\ngosho's last turn E1\\n" +
                "Enter your turn \n";
        String cmp4 = "gosho:   1 2 3 4 5 6 7 8 9 10\\n" +
                "   _ _ _ _ _ _ _ _ _ _\\n" +
                "A |_|_|_|_|_|_|_|_|_|_|\\n" +
                "B |_|_|_|_|_|_|_|_|_|_|\\n" +
                "C |_|_|_|_|_|_|_|_|_|_|\\n" +
                "D |X|_|_|_|_|_|_|_|_|_|\\n" +
                "E |X|_|_|_|_|_|_|_|_|_|\\n" +
                "F |_|_|_|_|_|_|_|_|_|_|\\n" +
                "G |_|_|_|_|_|_|_|_|_|_|\\n" +
                "H |_|_|_|_|_|_|_|_|_|_|\\n" +
                "I |_|_|_|_|_|_|_|_|_|_|\\n" +
                "J |_|_|_|_|_|_|_|_|_|_|\\n" +
                "\\npesho's last turn E1\\n" +
                "You have lost the game!\n";
        Assert.assertTrue(result1.equals(cmp1) && result2.equals(cmp2)
                && result3.equals(cmp3) && result4.equals(cmp4));
    }
}
