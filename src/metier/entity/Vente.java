package metier.entity;

import java.util.Date;

public class Vente {
    private int id;
    private int idProduit;
    private int idEmploye;
    private int quantite;
    private Date dateVente;


    public Vente() {}

    public Vente(int idProduit, int idEmploye, int quantite, Date dateVente) {
        this.idProduit = idProduit;
        this.idEmploye = idEmploye;
        this.quantite = quantite;
        this.dateVente = dateVente;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Date getDateVente() {
        return dateVente;
    }

    public void setDateVente(Date dateVente) {
        this.dateVente = dateVente;
    }


}
