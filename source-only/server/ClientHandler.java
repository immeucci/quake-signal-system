package server;

import java.io.*;
import java.net.Socket;

/**
 * Handles communication with a single client, writes reports to city-specific files.
 */
public class ClientHandler implements Runnable {
    private final PrintWriter out;
    private final BufferedReader in;
    private final Socket clientSocket;
    private final String city;

    /**
     * Initializes streams and reads initial city registration.
     */
    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.city = in.readLine();
        out.println("Server connected to detector for " + city);
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null && !message.equals("exit")) {
                System.out.println("Awaiting report...");
                String[] parts = message.split(";");
                appendReport(parts[0], parts[1]);
            }
            System.out.println("Client disconnected.");
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    /**
     * Appends the report to a text file named after the city.
     */
    private void appendReport(String time, String magnitude) {
        String filename = city + ".txt";
        String line = String.format("Reported earthquake of magnitude %s at %s%n", magnitude, time);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(line);
            System.out.println("Report appended to " + filename);
        } catch (IOException e) {
            System.err.println("File write error: " + e.getMessage());
        }
    }
}
