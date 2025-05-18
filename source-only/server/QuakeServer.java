package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * QuakeServer listens for incoming detector clients on a TCP port and
 * spawns a handler thread for each connection.
 */
public class QuakeServer {
    public static void main(String[] args) {
        try {
            // Create a server socket listening on port 5000
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server open on port 5000");

            // Continuously accept client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                // Handle each client on its own thread
                ClientHandler handler = new ClientHandler(clientSocket);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
