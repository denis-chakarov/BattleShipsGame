package battleships.network.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class DataTransferThread extends Thread {
    private BufferedReader dataReader;
    private PrintWriter dataWriter;

    public DataTransferThread(BufferedReader dataReader, PrintWriter dataWriter) {
        this.dataReader = dataReader;
        this.dataWriter = dataWriter;
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                String data = dataReader.readLine();
                outputHandler(data);
            }
        } catch (IOException ioe) {
            System.err.println("Lost connection to server.");
            System.exit(-1);
        }
    }

    private void outputHandler(String data) {
        String[] result = data.split("\\\\n");
        for (int i = 0; i < result.length; i++) {
            dataWriter.println(result[i]);
        }
        dataWriter.flush();
    }
}