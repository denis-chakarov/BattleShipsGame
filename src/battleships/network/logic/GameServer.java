package battleships.network.logic;

import battleships.game.logic.GameParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static final int LISTENING_PORT = 4444;
    private static ServerSocket serverSocket;

    private static ServerTaskManager serverTaskManager;
    private static GameParser gameParser;

    public static void main(String[] args) {
        bindServerSocket();
        gameParser = new GameParser();
        serverTaskManager = new ServerTaskManager();
        serverTaskManager.start();
        handleClientConnections();
    }


    private static void bindServerSocket() {
        try {
            serverSocket = new ServerSocket(LISTENING_PORT);
            System.out.println("Game server started");
        } catch (IOException ioe) {
            System.err.println("Cannot start server ");
            System.exit(-1);
        }
    }

    private static void handleClientConnections() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientInfo client = new ClientInfo();
                client.setClientSocket(socket);
                GameClientListener clientListener = new
                        GameClientListener(client, serverTaskManager, gameParser);
                GameClientSender clientSender =
                        new GameClientSender(client, serverTaskManager);
                client.setClientListener(clientListener);
                clientListener.start();
                client.setClientSender(clientSender);
                clientSender.start();
                serverTaskManager.addClient(client);
            } catch (IOException ioe) {
                System.err.println("Problem with socket connection");
            }
        }
    }
}
