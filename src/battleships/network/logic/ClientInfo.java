package battleships.network.logic;

import java.net.Socket;

public class ClientInfo {
    private Socket clientSocket = null;
    private GameClientListener clientListener = null;
    private GameClientSender clientSender = null;
    private String user = "";
    private String game = "";

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public GameClientListener getClientListener() {
        return clientListener;
    }

    public void setClientListener(GameClientListener clientListener) {
        this.clientListener = clientListener;
    }

    public GameClientSender getClientSender() {
        return clientSender;
    }

    public void setClientSender(GameClientSender clientSender) {
        this.clientSender = clientSender;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
