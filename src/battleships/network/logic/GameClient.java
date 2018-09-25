package battleships.network.logic;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient {
    public static final String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 4444;

    private static BufferedReader socketReader;
    private static PrintWriter socketWriter;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            socketReader = new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));
            socketWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connected to server " + SERVER_PORT);
        }
        catch(UnknownHostException e1) {
            e1.printStackTrace(); //!!
        }
        catch (IOException ioe) {
            System.err.println("Can not connect to " + SERVER_PORT);
            System.exit(-1);
        }

        PrintWriter consoleWriter = new PrintWriter(System.out);
        DataTransferThread socketToConsole = new
                DataTransferThread(socketReader, consoleWriter);
        socketToConsole.start();

        BufferedReader consoleReader = new BufferedReader(
                new InputStreamReader(System.in));
        DataTransferThread consoleToSocket = new
                DataTransferThread(consoleReader, socketWriter);
        consoleToSocket.start();
    }
} 