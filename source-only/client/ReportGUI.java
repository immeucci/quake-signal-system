package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * ReportGUI allows users to submit earthquake magnitude reports with timestamps.
 */
public class ReportGUI extends JFrame {
    private final ClientController controller;
    private final String city;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ReportGUI(ClientController controller, String city) {
        this.controller = controller;
        this.city = city;

        setTitle("Report Earthquake - " + city);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Report earthquake for " + city, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(100, 150, 240));
        title.setBorder(new EmptyBorder(10, 0, 10, 0));

        JTextField magnitudeField = new JTextField();
        JLabel magnitudeLabel = new JLabel("Magnitude:");
        JLabel timeLabel = new JLabel("Report Time:");
        JLabel timeValue = new JLabel(LocalDateTime.now().format(timeFormatter));

        JPanel dataPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        dataPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        dataPanel.add(magnitudeLabel);
        dataPanel.add(magnitudeField);
        dataPanel.add(timeLabel);
        dataPanel.add(timeValue);

        JButton sendButton = new JButton("Send Report");
        sendButton.setEnabled(false);
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setPreferredSize(new Dimension(180, 30));
        sendButton.setBackground(new Color(100, 150, 240));
        sendButton.setOpaque(true);
        sendButton.setBorderPainted(false);

        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        disconnectButton.setPreferredSize(new Dimension(180, 30));
        disconnectButton.setBackground(new Color(100, 150, 240));
        disconnectButton.setOpaque(true);
        disconnectButton.setBorderPainted(false);

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        buttonPanel.add(sendButton, BorderLayout.CENTER);
        buttonPanel.add(disconnectButton, BorderLayout.EAST);
        buttonPanel.add(statusLabel, BorderLayout.SOUTH);

        magnitudeField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                try {
                    Double.parseDouble(magnitudeField.getText());
                    sendButton.setEnabled(true);
                } catch (Exception e) {
                    sendButton.setEnabled(false);
                }
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });

        sendButton.addActionListener(e -> {
            double magnitude = Double.parseDouble(magnitudeField.getText());
            String time = LocalDateTime.now().format(timeFormatter);
            controller.sendReport(magnitude, time);
            statusLabel.setText("Report sent at " + time);
            magnitudeField.setText("");
        });

        disconnectButton.addActionListener(e -> {
            controller.closeConnection();
            dispose();
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(title, BorderLayout.NORTH);
        getContentPane().add(dataPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 300);
        setVisible(true);
    }
}
