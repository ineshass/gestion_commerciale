package metier.entity;

public class Employe {
    private int id;
    private String nom, prenom, poste;
    private double salaire;
    public Employe() {} // Quand vous récupérez un produit depuis la base de données, JDBC crée d'abord un objet vide, puis remplit ses attributs via les setters.
    public Employe(int id, String nom, String prenom, String poste, double salaire) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.salaire = salaire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }
}