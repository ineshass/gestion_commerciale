package dao.impl;

import dao.IVenteDAO;
import metier.entity.Vente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import dao.SingletonConnection;

public class VenteDAOImpl implements IVenteDAO {

    @Override
    public void enregistrerVente(Vente v) {
        try (Connection connection = SingletonConnection.getConnection()){
             String sql="INSERT INTO VENTES(ID_Produit, ID_Employe, quantite, date_vente) VALUES(?,?,?,?)";
             PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, v.getIdProduit());
            ps.setInt(2, v.getIdEmploye());
            ps.setInt(3, v.getQuantite());
            ps.setDate(4, new java.sql.Date(v.getDateVente().getTime()));
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement d'une vente");
            e.printStackTrace();
        }
    }

    @Override
    public List<Vente> getAllVentes() {
        List<Vente> ventes = new ArrayList<>();
        try (Connection connection = SingletonConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM VENTES")) {

            while (rs.next()) {
                Vente v = new Vente();
                v.setId(rs.getInt("ID_Vente"));
                v.setIdProduit(rs.getInt("ID_Produit"));
                v.setIdEmploye(rs.getInt("ID_Employe"));
                v.setQuantite(rs.getInt("quantite"));
                v.setDateVente(rs.getDate("date_vente"));
                ventes.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des ventes");
            e.printStackTrace();
        }
        return ventes;
    }

    @Override
    public List<Vente> getVentesByEmploye(int idEmploye) {
        List<Vente> ventes = new ArrayList<>();
        try (Connection connection = SingletonConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM VENTES WHERE ID_Employe = ?")) {

            ps.setInt(1, idEmploye);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vente v = new Vente();
                v.setId(rs.getInt("ID_Vente"));
                v.setIdProduit(rs.getInt("ID_Produit"));
                v.setIdEmploye(rs.getInt("ID_Employe"));
                v.setQuantite(rs.getInt("quantite"));
                v.setDateVente(rs.getDate("date_vente"));
                ventes.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des ventes par employé");
            e.printStackTrace();
        }
        return ventes;
    }

    @Override
    public List<Vente> getVentesByPeriode(Date debut, Date fin) {
        List<Vente> ventes = new ArrayList<>();
        try (Connection connection = SingletonConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM VENTES WHERE date_vente BETWEEN ? AND ?")) {

            ps.setDate(1, new java.sql.Date(debut.getTime()));
            ps.setDate(2, new java.sql.Date(fin.getTime()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vente v = new Vente();
                v.setId(rs.getInt("ID_Vente"));
                v.setIdProduit(rs.getInt("ID_Produit"));
                v.setIdEmploye(rs.getInt("ID_Employe"));
                v.setQuantite(rs.getInt("quantite"));
                v.setDateVente(rs.getDate("date_vente"));
                ventes.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des ventes par période");
            e.printStackTrace();
        }
        return ventes;
    }

    @Override
    public double getTotalVentesByProduit(int idProduit) {
        double total = 0;
        try (Connection connection = SingletonConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT SUM(quantite) as total FROM VENTES WHERE ID_Produit = ?")) {

            ps.setInt(1, idProduit);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du total des ventes par produit");
            e.printStackTrace();
        }
        return total;
    }

    @Override
    public Vente getVenteById(int id) {
        Vente vente = null;
        try (Connection connection = SingletonConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM VENTES WHERE ID_Vente = ?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vente = new Vente();
                vente.setId(rs.getInt("ID_Vente"));
                vente.setIdProduit(rs.getInt("ID_Produit"));
                vente.setIdEmploye(rs.getInt("ID_Employe"));
                vente.setQuantite(rs.getInt("quantite"));
                vente.setDateVente(rs.getDate("date_vente"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche d'une vente par ID");
            e.printStackTrace();
        }
        return vente;
    }
}
