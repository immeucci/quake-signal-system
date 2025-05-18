package client;

import javax.swing.*;
import java.awt.*;

/**
 * QuakeClient builds the initial GUI for entering server details and a city.
 */
public class QuakeClient extends JFrame {
    private final JTextField ipField, portField, cityField;

    public QuakeClient() {
        setTitle("Detector Client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 300));
        setLocationRelativeTo(null);

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createLabel("IP Address:", labelFont));
        ipField = createTextField(fieldFont);
        formPanel.add(ipField);
        formPanel.add(createLabel("Port:", labelFont));
        portField = createTextField(fieldFont);
        formPanel.add(portField);
        formPanel.add(createLabel("City:", labelFont));
        cityField = createTextField(fieldFont);
        formPanel.add(cityField);

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(100, 150, 240));
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setPreferredSize(new Dimension(100, 30));
        sendButton.addActionListener(new ClientController(this, ipField, portField, cityField));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(formPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuakeClient::new);
    }
}
