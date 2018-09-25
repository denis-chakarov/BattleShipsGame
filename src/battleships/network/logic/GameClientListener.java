package battleships.network.logic;

import battleships.game.logic.GameParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GameClientListener extends Thread {
    private ServerTaskManager serverTaskManager;
    private ClientInfo clientInfo;
    private BufferedReader socketReader;
    private GameParser gameParser;
    public GameClientListener(ClientInfo clientInfo, ServerTaskManager
            serverTaskManager, GameParser gameParser) throws IOException {
        this.clientInfo = clientInfo;
        this.serverTaskManager = serverTaskManager;
        Socket socket = clientInfo.getClientSocket();
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.gameParser = gameParser;
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                String input = socketReader.readLine();
                String message;
                if (input == null) {
                    break;
                }
                else {
                    message = gameParser.parseMessage(input, this);
                    serverTaskManager.dispatchMessage(clientInfo, message);
                }

            }
        } catch (IOException ioex) {
            System.err.println("Problem reading from socket (broken connection)");
        }
        clientInfo.getClientSender().interrupt();
        serverTaskManager.deleteClient(clientInfo);
    }

    public void setSocketReader(BufferedReader socketReader) {
        this.socketReader = socketReader;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }
}