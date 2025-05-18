import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Controller for client GUI actions: establishes connection, sends city queries
 * and handles subsequent earthquake reports.
 */
public class ClientController implements ActionListener {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String city;

    private final QuakeClient parent;
    private final JTextField ipField;
    private final JTextField portField;
    private final JTextField cityField;

    /**
     * @param parent Reference to main GUI window
     * @param ipField Text field for server IP
     * @param portField Text field for server port
     * @param cityField Text field for target city
     */
    public ClientController(QuakeClient parent, JTextField ipField, JTextField portField, JTextField cityField) {
        this.parent = parent;
        this.ipField = ipField;
        this.portField = portField;
        this.cityField = cityField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Validate input fields
        if (ipField.getText().isEmpty() || portField.getText().isEmpty() || cityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ip = ipField.getText();
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.city = cityField.getText().toLowerCase();

        try {
            // Establish connection to server
            this.socket = new Socket(ip, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send initial city registration
            out.println(city);
            String response = in.readLine();
            if (response == null) {
                JOptionPane.showMessageDialog(null, "Connection error.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, response, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Open reporting window and close setup GUI
            new ReportGUI(this, city);
            parent.dispose();

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connection error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sends an earthquake report with magnitude and timestamp.
     * @param magnitude Earthquake magnitude
     * @param time Timestamp of report
     */
    public void sendReport(double magnitude, String time) {
        new Thread(() -> {
            try {
                if (in == null || out == null) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "No connection established.", "Error", JOptionPane.ERROR_MESSAGE)
                    );
                    return;
                }

                String report = time + ";" + magnitude;
                out.println(report);

                String serverResponse = in.readLine();
                SwingUtilities.invokeLater(() -> {
                    if (serverResponse == null) {
                        JOptionPane.showMessageDialog(null, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Server: " + serverResponse, "Report Status", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            } catch (IOException e) {
                if (!e.getMessage().toLowerCase().contains("socket closed")) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Error sending report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                    );
                }
            }
        }).start();
    }

    /**
     * Closes the connection gracefully by sending an exit command.
     */
    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                out.println("exit");
                out.close();
                in.close();
                socket.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error closing connection: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}