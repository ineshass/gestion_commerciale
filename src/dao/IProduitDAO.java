package dao;

import metier.entity.Produit;
import java.util.List;

public interface IProduitDAO {
    // Ajouter un nouveau produit
    void ajouterProduit(Produit p);

    // Récupérer tous les produits
    List<Produit> getAllProduits();

    // Récupérer les produits en rupture de stock
    List<Produit> getProduitsEnRupture();

    // Récupérer un produit par son ID
    Produit getProduitById(int id);

    // Mettre à jour un produit
    void updateProduit(Produit p);

    // Supprimer un produit
    void deleteProduit(int id);
}