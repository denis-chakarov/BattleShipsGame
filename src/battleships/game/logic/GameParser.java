package battleships.game.logic;
import battleships.network.logic.*;
import java.util.ArrayList;
import java.util.List;

public class GameParser {
    private static GameDatabase gameDatabase;
    private static String user;
    public GameParser() {
        gameDatabase  = new GameDatabase();
        user = "";
    }

    public synchronized String parseMessage(String message, GameClientListener listener) {
        String result = "";
        if(message.contains("--username")) {
            listener.getClientInfo().setUser(message.substring(11));
            user = listener.getClientInfo().getUser();
            if (!gameDatabase.getLogged().contains(user)) {
                gameDatabase.getLogged().add(user);
                if(gameDatabase.getPlayers().containsKey(user)) {
                    return "Available commands \\ncreate-game\\njoin-game\\n" +
                            "saved-games\\nload-game\\ndelete-game\\nlist-games\\nmenu>\n";
                }
                else {
                    return "No such player!\n";
                }
            }
            else {
                return "Player is logged in!\n";
            }
        }
        else if(!user.equals("") && !gameDatabase.getPlayers().get(user).isPlaying()) {
            result = gameMenu(message, listener);
            return result;
        }
        if (gameDatabase.getPlayers().get(user).isPlaying()) {
            result = gameBattle(message, listener);
            return result;
        }
        System.out.println(user);
        return result;
    }

    private synchronized String gameMenu(String message, GameClientListener listener) {
        String game;
        if(listener != null) {
            user = listener.getClientInfo().getUser();
        }
        String output = "";
        if(message.contains("create-game")) {
            System.out.println("inside create-game " + user);
            game = message.substring(12);
            Rival first = new Rival(user, false, new GameTable());
            gameDatabase.getActiveGames().add(new Playground(first,
                    new Rival(), game, "pending", 1, "none"));
            gameDatabase.getPlayers().get(user).setPlaying(true);
            output = "Created game "+ game +", players 1/2\n";
            return output;
        }

        if(message.contains("join-game")) {
            System.out.println("inside join-game " + user);
            game = message.substring(10);

            for(Playground pg : gameDatabase.getActiveGames()) {
                if(pg.getName().equals(game) && pg.getStatus().equals("pending")
                        && pg.getPlayersCount() < 2) {

                    pg.setPlayersCount(1);
                    pg.setStatus("in progress");
                    Rival second = new Rival(user, false, new GameTable());
                    pg.setSecondPlayer(second);
                    gameDatabase.getPlayers().get(user).setPlaying(true);
                    output = "Joined game " + game + " PLAYERS 2/2, type start to start the game\n";
                    return output;
                }
                else {
                    output = "No such game!\n";
                    return output;
                }

            }
        }

        if(message.contains("saved-games")) {
            System.out.println("inside saved-game " + user);
            for(Playground games : gameDatabase.getPlayers().get(user).getSavedGames()) {
                output += games.getName() + "\\n";
            }
            output += "\n";
            return output;
        }

        if(message.contains("load-game")) {
            System.out.println("inside load-game " + user);
            game = message.substring(10);
            List<Playground> savedGames = new ArrayList<>(gameDatabase.getPlayers()
                    .get(user).getSavedGames());
            for (int i = 0; i < savedGames.size(); i++) {
                if(savedGames.get(i).getName().equals(game)) {
                    gameDatabase.getActiveGames().add(savedGames.get(i));
                    gameDatabase.getPlayers().get(user).getSavedGames().remove(savedGames.get(i));
                    gameDatabase.getPlayers().get(user).setPlaying(true);
                    output = "type \"start\" to continue\n";
                    return output;
                }
            }
        }

        if(message.contains("delete-game")) {
            System.out.println("inside delete-game " + user);
            game = message.substring(12);
            for (int i = 0; i < gameDatabase.getPlayers().get(user).getSavedGames().size(); i++) {
                if(gameDatabase.getPlayers().get(user).getSavedGames().get(i).getName().equals(game)) {
                    gameDatabase.getPlayers().get(user).getSavedGames()
                            .remove(gameDatabase.getPlayers().get(user).getSavedGames().get(i));
                    return "Game deleted!\n";
                }
            }
        }

        if(message.contains("list-games")) {
            System.out.println("inside list-game " + user);
            output = "| NAME     | CREATOR | STATUS      | PLAYERS |\\n" +
                    "|----------+---------+-------------+---------|\\n";
            for (int i = 0; i < gameDatabase.getActiveGames().size(); i++) {
                output += "| " + gameDatabase.getActiveGames().get(i).getName() + "  | " +
                    gameDatabase.getActiveGames().get(i).getFirstPlayer().getName() + "  | " +
                    gameDatabase.getActiveGames().get(i).getStatus() + "  | " +
                    gameDatabase.getActiveGames().get(i).getPlayersCount() + "/2  |\\n";
            }
            output +="\n";
            return output;
        }
        System.out.println("game menu issue");
        return "Unavailable command!\n";
    }

    private synchronized String gameBattle(String message, GameClientListener listener) {
        if(listener != null) {
            setUser(listener.getClientInfo().getUser());
        }
        System.out.println("inside gameBattle " + user);
        Playground currGame = null;
        for(Playground pg : gameDatabase.getActiveGames()) {
            if((pg.getFirstPlayer() != null && pg.getFirstPlayer().getName().equals(user)) ||
                    (pg.getSecondPlayer() != null && pg.getSecondPlayer().getName().equals(user))) {
                currGame = pg;
                break;
            }
        }

        if(message.contains("start") && currGame != null) {
            return startingGame(currGame);
        }

        if(message.contains("save") && currGame != null) {
            return savingGame(currGame);
        }

        if((message.charAt(0) >= 'A' && message.charAt(0) <= 'J'
                && Character.getNumericValue(message.charAt(1)) >= 1
                && Character.getNumericValue(message.charAt(1)) <= 10)) {
            return attackingEnemy(currGame, message);
        }
        System.out.println("game battle issue");
        return "Unavailable command!\n";
    }

    private synchronized String savingGame(Playground currGame) {
        Playground saved;
        if(currGame.getFirstPlayer().getName().equals(user)) {
            saved = Playground.copyPlayground(currGame, true);
        } else {
            saved = Playground.copyPlayground(currGame, false);
        }
        gameDatabase.getPlayers().get(user).getSavedGames().add(saved);
        return "Game saved!\n";
    }

    private synchronized String startingGame(Playground currGame) {
        String output = "";
        if(currGame.getPlayersCount() < 2) {
            output = "No second player!\n";
            return output;
        }
        else {
            if(currGame.getFirstPlayer().getName().equals(user)) {
                output += currGame.getFirstPlayer().getGameTable().printTable();
            }
            else {
                output += currGame.getSecondPlayer().getGameTable().printTable();
            }
            output += "\\nEnter your turn \n";
            return output;
        }
    }

    private synchronized String attackingEnemy(Playground currGame, String message) {
        Rival playerOne;
        Rival playerTwo;
        String output;
        if(currGame.getFirstPlayer().getName().equals(user)) {
            playerOne = currGame.getFirstPlayer();
            playerTwo = currGame.getSecondPlayer();
        }
        else {
            playerOne = currGame.getSecondPlayer();
            playerTwo = currGame.getFirstPlayer();
        }
        if(!playerOne.isFlag()) {
            currGame.setTurn(message);
            playerTwo.getGameTable().attacked(message);
            playerOne.setFlag(true);
            playerTwo.setFlag(false);
            output = playerTwo.getName() + ":" + playerTwo.getGameTable().printTable() +
                    "\\n" + playerOne.getName() + "'s last turn " + currGame.getTurn();
            if(playerTwo.getGameTable().lost()) {
                output += "\\nYou have lost the game!\n";
                gameDatabase.getPlayers().get(user).setPlaying(false);
                gameDatabase.getPlayers().get(playerTwo.getName()).setPlaying(false);
                gameDatabase.getActiveGames().remove(currGame);
                currGame = null;

            }
            else {
                output += "\\nEnter your turn \n";
            }
        }
        else {
            output = "Wait for the other player to make his/her turn!\n";
        }
        return output;
    }

    public synchronized GameDatabase getGameDatabase() {
        return gameDatabase;
    }

    public synchronized static void setUser(String user) {
        GameParser.user = user;
    }
}
