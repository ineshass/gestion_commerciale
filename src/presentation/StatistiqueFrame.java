package presentation;

import dao.IEmployeDAO;
import dao.IProduitDAO;
import dao.IVenteDAO;
import dao.impl.EmployeDAOImpl;
import dao.impl.ProduitDAOImpl;
import dao.impl.VenteDAOImpl;
import metier.entity.Employe;
import metier.entity.Produit;
import metier.entity.Vente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatistiqueFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private final IEmployeDAO employeDAO;
    private final IProduitDAO produitDAO;
    private final IVenteDAO venteDAO;

    public StatistiqueFrame() {
        this.employeDAO = new EmployeDAOImpl();
        this.produitDAO = new ProduitDAOImpl();
        this.venteDAO = new VenteDAOImpl();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Statistiques de Vente");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Création des onglets avec données dynamiques
        tabbedPane.addTab("Classement Employés", createEmployesStatsPanel());
        tabbedPane.addTab("Produits Vendus", createProduitsStatsPanel());
        tabbedPane.addTab("Revenus", createRevenusStatsPanel());

        add(tabbedPane);
    }

    private JPanel createEmployesStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Récupération et traitement des données
        List<Vente> ventes = venteDAO.getAllVentes();
        List<Employe> employes = employeDAO.getAllEmployes();

        // Calcul du CA par employé
        Map<Integer, Double> caParEmploye = ventes.stream()
                .collect(Collectors.groupingBy(
                        Vente::getIdEmploye,
                        Collectors.summingDouble(v -> {
                            Produit p = produitDAO.getProduitById(v.getIdProduit());
                            return p != null ? p.getPrix() * v.getQuantite() : 0;
                        })
                ));

        // Création du modèle de tableau
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Classement", "ID", "Nom", "Prénom", "Chiffre d'affaires (DT)"}, 0);

        // Remplissage avec données triées
        employes.stream()
                .filter(e -> caParEmploye.containsKey(e.getId()))
                .sorted(Comparator.comparingDouble(e -> -caParEmploye.get(e.getId())))//Trie les employés par chiffre d'affaires décroissant.
                .forEachOrdered(e -> {
                    double ca = caParEmploye.get(e.getId());
                    model.addRow(new Object[]{
                            model.getRowCount() + 1,
                            e.getId(),
                            e.getNom(),
                            e.getPrenom(),
                            String.format("%,.2f", ca)
                    });
                });

        JTable table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bouton d'actualisation
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refreshTab(0));
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createProduitsStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Récupération et traitement des données
        List<Vente> ventes = venteDAO.getAllVentes();
        List<Produit> produits = produitDAO.getAllProduits();

        // Calcul des statistiques produits
        Map<Integer, Long> quantitesVendues = ventes.stream()
                .collect(Collectors.groupingBy(
                        Vente::getIdProduit,
                        Collectors.summingLong(Vente::getQuantite)
                ));

        Map<Integer, Double> caParProduit = ventes.stream()
                .collect(Collectors.groupingBy(
                        Vente::getIdProduit,
                        Collectors.summingDouble(v -> {
                            Produit p = produitDAO.getProduitById(v.getIdProduit());
                            return p != null ? p.getPrix() * v.getQuantite() : 0;
                        })
                ));

        // Création du modèle de tableau
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Quantité Vendue", "Chiffre d'affaires (DT)", "Stock Restant"}, 0);

        // Remplissage avec données triées
        produits.stream()
                .filter(p -> quantitesVendues.containsKey(p.getId()))
                .sorted(Comparator.comparingLong(p -> -quantitesVendues.get(p.getId())))
                .forEach(p -> {
                    long quantiteVendue = quantitesVendues.get(p.getId());
                    double ca = caParProduit.get(p.getId());
                    model.addRow(new Object[]{
                            p.getId(),
                            p.getNom(),
                            quantiteVendue,
                            String.format("%,.2f", ca),
                            p.getQuantite()
                    });
                });

        JTable table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bouton d'actualisation
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refreshTab(1));
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRevenusStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Récupération des données
        List<Vente> ventes = venteDAO.getAllVentes();
        List<Produit> produits = produitDAO.getAllProduits();

        // Calcul des statistiques
        double revenusTotaux = ventes.stream()
                .mapToDouble(v -> {
                    Produit p = produitDAO.getProduitById(v.getIdProduit());
                    return p != null ? p.getPrix() * v.getQuantite() : 0;
                })
                .sum();

        long nombreVentes = ventes.size();

        // Affichage des statistiques
        JPanel statsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistiques Globales"));

        statsPanel.add(new JLabel("Revenus totaux:"));
        statsPanel.add(new JLabel(String.format("%,.2f dt", revenusTotaux)));

        statsPanel.add(new JLabel("Nombre total de ventes:"));
        statsPanel.add(new JLabel(String.valueOf(nombreVentes)));

        statsPanel.add(new JLabel("Produits en stock:"));
        statsPanel.add(new JLabel(String.valueOf(
                produits.stream().mapToInt(Produit::getQuantite).sum())));

        statsPanel.add(new JLabel("Produits vendus:"));
        statsPanel.add(new JLabel(String.valueOf(
                ventes.stream().mapToInt(Vente::getQuantite).sum())));

        statsPanel.add(new JLabel("Employés actifs:"));
        statsPanel.add(new JLabel(String.valueOf(employeDAO.getAllEmployes().size())));

        panel.add(statsPanel, BorderLayout.CENTER);

        // Bouton d'actualisation
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refreshTab(2));
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshTab(int index) {
        switch (index) {
            case 0:
                tabbedPane.setComponentAt(0, createEmployesStatsPanel());
                break;
            case 1:
                tabbedPane.setComponentAt(1, createProduitsStatsPanel());
                break;
            case 2:
                tabbedPane.setComponentAt(2, createRevenusStatsPanel());
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new StatistiqueFrame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}