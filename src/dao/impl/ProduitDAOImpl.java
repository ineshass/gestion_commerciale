package dao.impl;

import dao.SingletonConnection;
import metier.entity.Employe;
import metier.entity.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import dao.IProduitDAO;
public class ProduitDAOImpl implements IProduitDAO {

    private List<Produit> produits = new ArrayList<>();
    private int lastId = 0; // Pour simuler un auto-increment

    @Override
    public void ajouterProduit(Produit p) {
        try (Connection connection = SingletonConnection.getConnection()) {
            String sql ="INSERT INTO produits(ID_Produit, Nom, Prix, Quantite) VALUES(?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, p.getId());
            ps.setString(2, p.getNom());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getQuantite());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout d'un employé");
            ex.printStackTrace();
        }
    }

    @Override
    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        try (Connection connection = SingletonConnection.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM produits")) {

            while (rs.next()) {
                Produit p = new Produit();
                p.setId(rs.getInt("ID_Produit"));
                p.setNom(rs.getString("Nom"));
                p.setPrix(rs.getDouble("Prix"));
                p.setQuantite(rs.getInt("Quantite"));
                produits.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des employés");
            e.printStackTrace();
        }
        return produits;
    }

    @Override
    public List<Produit> getProduitsEnRupture() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT p.ID_Produit, p.Nom, p.Prix, p.Quantite, " +
                "IFNULL(SUM(v.quantite), 0) as total_vendu " +
                "FROM produits p " +
                "LEFT JOIN ventes v ON p.ID_Produit = v.ID_Produit " +
                "GROUP BY p.ID_Produit, p.Nom, p.Prix, p.Quantite " +
                "HAVING p.Quantite <= total_vendu OR p.Quantite = 0";

        try (Connection conn = SingletonConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Produits en rupture de stock:"); // Log pour débogage

            while (rs.next()) {
                Produit p = new Produit();
                p.setId(rs.getInt("ID_Produit"));
                p.setNom(rs.getString("Nom"));
                p.setPrix(rs.getDouble("Prix"));
                p.setQuantite(rs.getInt("Quantite"));
                produits.add(p);

                // Affiche l'ID du produit en rupture dans la console
                System.out.println("ID Produit en rupture: " + p.getId());
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits en rupture:");
            e.printStackTrace();
        }
        return produits;
    }


    @Override
    public Produit getProduitById(int id) {
        Produit p = null;
        try (Connection connection = SingletonConnection.getConnection()) {
            String sql = "SELECT * FROM produits WHERE ID_Produit = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new Produit();
                p.setId(rs.getInt("ID_Produit"));
                p.setNom(rs.getString("Nom"));
                p.setPrix(rs.getDouble("Prix"));
                p.setQuantite(rs.getInt("Quantite"));
            }

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération du produit par ID");
            ex.printStackTrace();
        }
        return p;
    }


    @Override
    public void updateProduit(Produit p) {
        try (Connection connection = SingletonConnection.getConnection()) {
            String sql = "UPDATE produits SET Nom = ?, Prix = ?, Quantite = ? WHERE ID_Produit = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPrix());
            ps.setInt(3, p.getQuantite());
            ps.setInt(4, p.getId());

            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour du produit");
            ex.printStackTrace();
        }
    }


    @Override
    public void deleteProduit(int id) {
        try (Connection connection = SingletonConnection.getConnection()) {
            // Supprimer les ventes associées
            String deleteVentes = "DELETE FROM ventes WHERE ID_Produit = ?";
            PreparedStatement psVente = connection.prepareStatement(deleteVentes);
            psVente.setInt(1, id);
            psVente.executeUpdate();

            // Supprimer le produit ensuite
            String deleteProduit = "DELETE FROM produits WHERE ID_Produit = ?";
            PreparedStatement psProduit = connection.prepareStatement(deleteProduit);
            psProduit.setInt(1, id);
            psProduit.executeUpdate();

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression du produit");
            ex.printStackTrace();
        }
    }


}