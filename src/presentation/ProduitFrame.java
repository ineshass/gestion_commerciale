package presentation;

import dao.IProduitDAO;
import dao.impl.ProduitDAOImpl;
import metier.entity.Produit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProduitFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final IProduitDAO produitDAO;

    public ProduitFrame() {
        this.produitDAO = new ProduitDAOImpl(); // Initialisation du DAO
        initializeUI();
        refreshData();
    }

    private void initializeUI() {
        setTitle("Gestion des Produits");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configuration du tableau
        model = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Prix", "Quantité"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Actualiser");
        JButton ruptureButton = new JButton("Voir rupture");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(ruptureButton);

        // Assemblage des composants
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Gestion des événements
        addButton.addActionListener(this::addProduct);
        editButton.addActionListener(this::editProduct);
        deleteButton.addActionListener(this::deleteProduct);
        refreshButton.addActionListener(e -> refreshData());
        ruptureButton.addActionListener(e -> showOutOfStock());

        add(mainPanel);
    }

    private void addProduct(ActionEvent e) {
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField nomField = new JTextField();
        JTextField prixField = new JTextField();
        JTextField quantiteField = new JTextField();

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prix:"));
        formPanel.add(prixField);
        formPanel.add(new JLabel("Quantité:"));
        formPanel.add(quantiteField);

        int result = JOptionPane.showConfirmDialog(
                this,
                formPanel,
                "Ajouter un produit",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                Produit p = new Produit();
                p.setNom(nomField.getText());
                p.setPrix(Double.parseDouble(prixField.getText()));
                p.setQuantite(Integer.parseInt(quantiteField.getText()));

                produitDAO.ajouterProduit(p);
                refreshData();
                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer des valeurs numériques valides pour le prix et la quantité",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editProduct(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un produit à modifier",
                    "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        Produit p = produitDAO.getProduitById(id);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField nomField = new JTextField(p.getNom());
        JTextField prixField = new JTextField(String.valueOf(p.getPrix()));
        JTextField quantiteField = new JTextField(String.valueOf(p.getQuantite()));

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prix:"));
        formPanel.add(prixField);
        formPanel.add(new JLabel("Quantité:"));
        formPanel.add(quantiteField);

        int result = JOptionPane.showConfirmDialog(
                this,
                formPanel,
                "Modifier produit ID: " + id,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                p.setNom(nomField.getText());
                p.setPrix(Double.parseDouble(prixField.getText()));
                p.setQuantite(Integer.parseInt(quantiteField.getText()));

                produitDAO.updateProduit(p);
                refreshData();
                JOptionPane.showMessageDialog(this, "Produit modifié avec succès");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer des valeurs numériques valides",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un produit à supprimer",
                    "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer ce produit?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            produitDAO.deleteProduit(id);
            refreshData();
            JOptionPane.showMessageDialog(this, "Produit supprimé avec succès");
        }
    }

    private void showOutOfStock() {
        List<Produit> produitsEnRupture = produitDAO.getProduitsEnRupture();

        if (produitsEnRupture.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Aucun produit en rupture de stock",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Créer un message avec la liste des IDs
            StringBuilder message = new StringBuilder();
            message.append("Produits en rupture de stock (IDs): \n");

            produitsEnRupture.forEach(p -> {
                message.append("• ID: ").append(p.getId()).append(" - ").append(p.getNom()).append("\n");
            });

            // Afficher le message
            JOptionPane.showMessageDialog(this,
                    message.toString(),
                    "Rupture de stock", JOptionPane.INFORMATION_MESSAGE);

            // Mettre à jour le tableau
            model.setRowCount(0);
            produitsEnRupture.forEach(p ->
                    model.addRow(new Object[]{
                            p.getId(),
                            p.getNom(),
                            p.getPrix(),
                            p.getQuantite()
                    })
            );
        }
    }
    private void refreshData() {
        model.setRowCount(0);
        List<Produit> produits = produitDAO.getAllProduits();
        produits.forEach(p ->
                model.addRow(new Object[]{
                        p.getId(),
                        p.getNom(),
                        p.getPrix(),
                        p.getQuantite()
                })
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ProduitFrame().setVisible(true);
        });
    }
}