package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AuthFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AuthFrame() { //Constructeur de la classe AuthFrame
        // Configuration de base
        setTitle("Authentification");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Composants
        panel.add(new JLabel("Nom d'utilisateur:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Connexion");
        loginButton.addActionListener(this::authentifier);
        panel.add(loginButton);

        add(panel);
    }

    private void authentifier(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Connexion réussie !");
            new EmployeFrame().setVisible(true);//t7elk l interface employe
            dispose();//tsakarlk linterface login
        } else {
            JOptionPane.showMessageDialog(this, "Échec de connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            AuthFrame frame = new AuthFrame();
            frame.setVisible(true);
        });
    }
}//rendre l'application plus cohérente avec le système d'exploitation de l'utilisateur.