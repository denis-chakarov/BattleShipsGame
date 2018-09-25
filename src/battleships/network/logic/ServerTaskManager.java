package battleships.network.logic;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerTaskManager extends Thread {
    private BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(1000);
    private CopyOnWriteArrayList clients = new CopyOnWriteArrayList();
    private String currUser = "";

    public synchronized void addClient(ClientInfo client) {
        clients.add(client);
    }

    public synchronized void deleteClient(ClientInfo client) {
        int clientIndex = clients.indexOf(client);
        if (clientIndex != -1) {
            clients.remove(clientIndex);
        }
    }

    public synchronized void dispatchMessage(ClientInfo client, String message) {
        String[] temp = message.split(":");
        currUser = client.getUser();
        if(temp.length > 1) {
            currUser = temp[0];
            message = temp[1];
        }
        messageQueue.add(message);
        notify();
    }
    private synchronized String getNextMessageFromQueue() throws InterruptedException {
        while (messageQueue.size() == 0) {
            wait();
        }
        String message = messageQueue.peek();
        messageQueue.remove();
        return message;
    }

    private void sendMessageToClient(String message) {
        for (int i = 0; i < clients.size(); i++) {
            ClientInfo client = (ClientInfo) clients.get(i);
            if(client.getUser().equals(currUser)) {
                client.getClientSender().sendMessage(message);
                break;
            }
        }
    }

    public void run() {
        try {
            while (true) {
                String message = getNextMessageFromQueue();
                sendMessageToClient(message);
            }
        } catch (InterruptedException ie) {
            System.out.println("ServerTaskManager has stopped due to interruption of thread");
        }
    }
}
