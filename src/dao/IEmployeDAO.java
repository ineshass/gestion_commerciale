package dao;

import metier.entity.Employe;
import java.util.List;

public interface IEmployeDAO {

    void ajouterEmploye(Employe e);


    List<Employe> getAllEmployes();


    List<Employe> getEmployesBySalaire(double salaireMinimum);


    Employe getEmployeById(int id);


    void updateEmploye(Employe e);


    void deleteEmploye(int id);
}
