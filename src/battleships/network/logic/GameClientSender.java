package battleships.network.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GameClientSender extends Thread {
    private BlockingQueue<String> messageQueue;
    private ServerTaskManager serverTaskManager;
    private ClientInfo clientInfo;
    private PrintWriter mOut;

    public GameClientSender(ClientInfo clientInfo, ServerTaskManager
            serverTaskManager) throws IOException {
        this.clientInfo = clientInfo;
        this.serverTaskManager = serverTaskManager;
        Socket socket = clientInfo.getClientSocket();
        mOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        messageQueue = new ArrayBlockingQueue<>(1000);
    }

    public synchronized void sendMessage(String aMessage) {
        messageQueue.add(aMessage);
        notify();
    }

    private synchronized String getNextMessageFromQueue()
            throws InterruptedException {
        while (messageQueue.size() == 0) {
            wait();
        }
        String message = messageQueue.peek();
        messageQueue.remove();
        return message;
    }

    private void sendMessageToClient(String message) {
        mOut.println(message);
        mOut.flush();
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                String message = getNextMessageFromQueue();
                sendMessageToClient(message);
            }
        } catch (Exception e) {
            System.err.println("Communication problem in sender's thread");
        }
        clientInfo.getClientListener().interrupt();
        serverTaskManager.deleteClient(clientInfo);
    }
}
