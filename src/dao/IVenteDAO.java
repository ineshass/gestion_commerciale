package dao;

import metier.entity.Vente;
import java.util.List;
import java.util.Date;

public interface IVenteDAO {
    // Enregistrer une nouvelle vente
    void enregistrerVente(Vente v);

    // Récupérer toutes les ventes
    List<Vente> getAllVentes();

    // Récupérer les ventes par employé
    List<Vente> getVentesByEmploye(int idEmploye);

    // Récupérer les ventes par période
    List<Vente> getVentesByPeriode(Date debut, Date fin);

    // Calculer le total des ventes par produit
    double getTotalVentesByProduit(int idProduit);

    // Récupérer une vente par son ID
    Vente getVenteById(int id);
}
