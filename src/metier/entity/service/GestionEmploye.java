package metier.entity.service;

import dao.IEmployeDAO;
import dao.impl.EmployeDAOImpl;
import metier.entity.Employe;
import java.util.List;

public class GestionEmploye {
    private final IEmployeDAO employeDao = new EmployeDAOImpl();

    public void ajouterEmploye(String nom, String prenom, String poste, double salaire) {
        Employe e = new Employe();
        e.setNom(nom);
        e.setPrenom(prenom);
        e.setPoste(poste);
        e.setSalaire(salaire);
        employeDao.ajouterEmploye(e);
    }

    public List<Employe> listerTousEmployes() {
        return employeDao.getAllEmployes();
    }

    public List<Employe> chercherEmployesParSalaire(double salaireMinimum) {
        return employeDao.getEmployesBySalaire(salaireMinimum);
    }

    public void modifierEmploye(int id, String nom, String prenom, String poste, double salaire) {
        Employe e = employeDao.getEmployeById(id);
        if (e != null) {
            e.setNom(nom);
            e.setPrenom(prenom);
            e.setPoste(poste);
            e.setSalaire(salaire);
            employeDao.updateEmploye(e);
        }
    }

    public void supprimerEmploye(int id) {
        employeDao.deleteEmploye(id);
    }


}
