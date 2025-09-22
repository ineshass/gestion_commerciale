package dao.impl;

import dao.IEmployeDAO;
import metier.entity.Employe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dao.SingletonConnection;


public class EmployeDAOImpl implements IEmployeDAO {
    @Override
    public void ajouterEmploye(Employe e) {
        try (Connection connection = SingletonConnection.getConnection()) {
             String sql ="INSERT INTO employes(Nom, Prenom, Poste, Salaire) VALUES(?,?,?,?)";
             PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getPoste());
            ps.setDouble(4, e.getSalaire());
            ps.executeUpdate(); // exécute la requête pour insérer l’employé dans la base
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout d'un employé");
            ex.printStackTrace();
        }
    }

    @Override
    public List<Employe> getAllEmployes() {
        List<Employe> employes = new ArrayList<>();
        try (Connection connection = SingletonConnection.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM EMPLOYES")) { //On récupère tous les employés depuis la base (avec un SELECT *) et on met le résultat dans rs.
// "executeQuery"exécuter des requêtes qui retournent des résultats (comme SELECT).
            while (rs.next()) {
                Employe e = new Employe();
                e.setId(rs.getInt("ID_Employe"));
                e.setNom(rs.getString("Nom"));
                e.setPrenom(rs.getString("Prenom"));
                e.setPoste(rs.getString("Poste"));
                e.setSalaire(rs.getDouble("Salaire"));
                employes.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des employés");
            e.printStackTrace();
        }
        return employes;
    }

    @Override
    public List<Employe> getEmployesBySalaire(double salaireMinimum) {
        List<Employe> employes = new ArrayList<>();
        try (Connection connection = SingletonConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM EMPLOYES WHERE salaire >= ?")) {

            ps.setDouble(1, salaireMinimum);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employe e = new Employe();
                e.setId(rs.getInt("ID_Employe"));
                e.setNom(rs.getString("Nom"));
                e.setPrenom(rs.getString("Prenom"));
                e.setPoste(rs.getString("Poste"));
                e.setSalaire(rs.getDouble("Salaire"));
                employes.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par salaire");
            e.printStackTrace();
        }
        return employes;
    }

    @Override
    public Employe getEmployeById(int id) {
        Employe e = null;
        try (Connection connection = SingletonConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM EMPLOYES WHERE ID_Employe = ?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                e = new Employe();
                e.setId(rs.getInt("ID_Employe"));
                e.setNom(rs.getString("Nom"));
                e.setPrenom(rs.getString("Prenom"));
                e.setPoste(rs.getString("Poste"));
                e.setSalaire(rs.getDouble("Salaire"));
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche par ID");
            ex.printStackTrace();
        }
        return e;
    }

    @Override
    public void updateEmploye(Employe e) {
        try (Connection connection = SingletonConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE EMPLOYES SET nom=?, prenom=?, poste=?, salaire=? WHERE ID_Employe=?")) {

            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getPoste());
            ps.setDouble(4, e.getSalaire());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour");
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteEmploye(int id) {
        try (Connection connection = SingletonConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM EMPLOYES WHERE ID_Employe=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression");
            e.printStackTrace();
        }
    }
}
