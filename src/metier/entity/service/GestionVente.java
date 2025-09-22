package metier.entity.service;

import dao.IVenteDAO;
import dao.impl.VenteDAOImpl;
import metier.entity.Vente;
import java.util.Date;
import java.util.List;

public class GestionVente {
    private final IVenteDAO venteDao = new VenteDAOImpl();

    public void enregistrerVente(int idProduit, int idEmploye, int quantite) {
        Vente v = new Vente();
        v.setIdProduit(idProduit);
        v.setIdEmploye(idEmploye);
        v.setQuantite(quantite);
        v.setDateVente(new Date());
        venteDao.enregistrerVente(v);
    }

    public List<Vente> consulterVentesParPeriode(Date debut, Date fin) {
        return venteDao.getVentesByPeriode(debut, fin);
    }

    public double calculerChiffreAffaires() {
        List<Vente> ventes = venteDao.getAllVentes();
        return ventes.stream()
                .mapToDouble(v -> v.getQuantite() * getPrixProduit(v.getIdProduit()))
                .sum();
    }

    private double getPrixProduit(int idProduit) {
        // Implémentation simplifiée (normalement via ProduitDAO)
        return 0.0; // À remplacer par la vraie logique
    }
}
