package metier.entity.service;

import dao.IProduitDAO;
import dao.impl.ProduitDAOImpl;
import metier.entity.Produit;
import java.util.List;

public class GestionProduit {
    private final IProduitDAO produitDao = new ProduitDAOImpl();

    public void ajouterProduit(String nom, double prix, int quantite) {
        Produit p = new Produit();
        p.setNom(nom);
        p.setPrix(prix);
        p.setQuantite(quantite);
        produitDao.ajouterProduit(p);
    }

    public List<Produit> listerTousProduits() {
        return produitDao.getAllProduits();
    }

    public List<Produit> listerProduitsEnRupture() {
        return produitDao.getProduitsEnRupture();
    }

    public void mettreAJourStock(int idProduit, int nouvelleQuantite) {
        Produit p = produitDao.getProduitById(idProduit);
        if (p != null) {
            p.setQuantite(nouvelleQuantite);
            produitDao.updateProduit(p);
        }
    }
}