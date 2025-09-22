
package presentation;

import dao.IVenteDAO;
import dao.impl.VenteDAOImpl;
import metier.entity.Vente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

public class VenteFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final IVenteDAO venteDAO;
    private JComboBox<String> filterComboBox;
    private JTextField filterField;
    private JButton filterButton;

    public VenteFrame() {
        this.venteDAO = new VenteDAOImpl();
        initializeUI();
        refreshData();
    }

    private void initializeUI() {
        setTitle("Gestion des Ventes");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de filtrage
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filterComboBox = new JComboBox<>(new String[]{"Toutes", "Par Employé", "Par Période"});
        filterField = new JTextField(15);
        filterButton = new JButton("Filtrer");

        filterPanel.add(new JLabel("Filtrer par:"));
        filterPanel.add(filterComboBox);
        filterPanel.add(filterField);
        filterPanel.add(filterButton);

        // Configuration du tableau
        model = new DefaultTableModel(
                new Object[]{"ID", "ID Produit", "ID Employé", "Quantité", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Date.class;
                return Object.class;
            }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Nouvelle Vente");
        JButton refreshButton = new JButton("Actualiser");

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        // Assemblage des composants
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Gestion des événements
        addButton.addActionListener(this::addVente);
        refreshButton.addActionListener(e -> refreshData());
        filterButton.addActionListener(this::applyFilter);

        add(mainPanel);
    }

    private void addVente(ActionEvent e) {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField idProduitField = new JTextField();
        JTextField idEmployeField = new JTextField();
        JTextField quantiteField = new JTextField();
        JTextField dateField = new JTextField(new Date().toString()); // Date actuelle par défaut

        formPanel.add(new JLabel("ID Produit:"));
        formPanel.add(idProduitField);
        formPanel.add(new JLabel("ID Employé:"));
        formPanel.add(idEmployeField);
        formPanel.add(new JLabel("Quantité:"));
        formPanel.add(quantiteField);
        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this,
                formPanel,
                "Enregistrer une nouvelle vente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                Vente v = new Vente();
                v.setIdProduit(Integer.parseInt(idProduitField.getText()));
                v.setIdEmploye(Integer.parseInt(idEmployeField.getText()));
                v.setQuantite(Integer.parseInt(quantiteField.getText()));
                v.setDateVente(new Date()); // Utilise la date actuelle

                venteDAO.enregistrerVente(v);
                refreshData();
                JOptionPane.showMessageDialog(this, "Vente enregistrée avec succès");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer des valeurs numériques valides",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void applyFilter(ActionEvent e) {
        String filterType = (String) filterComboBox.getSelectedItem();
        String filterValue = filterField.getText();

        switch (filterType) {
            case "Par Employé":
                try {
                    int idEmploye = Integer.parseInt(filterValue);
                    List<Vente> ventes = venteDAO.getVentesByEmploye(idEmploye);
                    updateTable(ventes);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Veuillez entrer un ID employé valide",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "Par Période":
                // Implémentez la logique de filtrage par période ici
                JOptionPane.showMessageDialog(this,
                        "Fonctionnalité à implémenter",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                break;

            default:
                refreshData();
        }
    }

    private void updateTable(List<Vente> ventes) {
        model.setRowCount(0);
        ventes.forEach(v ->
                model.addRow(new Object[]{
                        v.getId(),
                        v.getIdProduit(),
                        v.getIdEmploye(),
                        v.getQuantite(),
                        v.getDateVente()
                })
        );
    }

    private void refreshData() {
        List<Vente> ventes = venteDAO.getAllVentes();
        updateTable(ventes);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new VenteFrame().setVisible(true);
        });
    }
}