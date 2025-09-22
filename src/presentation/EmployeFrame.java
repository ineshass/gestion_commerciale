package presentation;

import dao.IEmployeDAO;
import dao.impl.EmployeDAOImpl;
import metier.entity.Employe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;//Le TableModel sert à connecter les données (ex: liste d'employés) au tableau JTable, qui les affiche à l'écran.
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EmployeFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;//Tu ajoutes directement des colonnes et des lignes.(defaulttablemodel)

    private final IEmployeDAO employeDAO;

    public EmployeFrame() {
        this.employeDAO = new EmployeDAOImpl();
        initializeUI();
        refreshData();
    }

    private void initializeUI() {
        setTitle("Gestion des Employés");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// Fermer uniquement cette fenêtre, sans quitter toute l'application.3akes EXIT_ON_CLOSE ysakr kol chy
        setLocationRelativeTo(null);//centre la fenêtre sur l’écran.

        //null signifie : "centrer par rapport à l’écran principal"

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//Ça évite que les éléments soient collés directement au bord de la fenêtre.

        // Configuration du tableau
        model = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Prénom", "Poste", "Salaire"}, 0);//Le 0 après la liste des colonnes signifie qu'il n'y a aucune ligne initiale dans le tableau (tu vas remplir ce tableau après).
        table = new JTable(model);//Cette ligne crée un tableau graphique (JTable) qui affichera les données définies dans le model. Le tableau va afficher les données sous forme de lignes et de colonnes.
        JScrollPane scrollPane = new JScrollPane(table);//Si le tableau contient beaucoup de données, il sera possible de faire défiler verticalement et horizontalement.

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton refreshButton = new JButton("Actualiser");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Assemblage des composants
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Gestion des événements
        addButton.addActionListener(this::addEmployee);
        editButton.addActionListener(this::editEmployee);
        deleteButton.addActionListener(this::deleteEmployee);
        refreshButton.addActionListener(e -> refreshData());

        add(mainPanel);//affichage dans la fenetre
    }

    private void addEmployee(ActionEvent e) {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField posteField = new JTextField();
        JTextField salaireField = new JTextField();

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Poste:"));
        formPanel.add(posteField);
        formPanel.add(new JLabel("Salaire:"));
        formPanel.add(salaireField);

        int result = JOptionPane.showConfirmDialog(
                this, formPanel, "Ajouter un employé",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Employe emp = new Employe();
                emp.setNom(nomField.getText());
                emp.setPrenom(prenomField.getText());
                emp.setPoste(posteField.getText());
                emp.setSalaire(Double.parseDouble(salaireField.getText()));

                employeDAO.ajouterEmploye(emp);
                refreshData();
                JOptionPane.showMessageDialog(this, "Employé ajouté avec succès");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editEmployee(ActionEvent e) {
        int selectedRow = table.getSelectedRow();//envoie l'index de la ligne sélectionnée.(si non il retourne -1)
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un employé", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);//Récupérer l'ID de l'employé sélectionné

        Employe emp = employeDAO.getEmployeById(id);//Récupérer l'objet Employe depuis la base de données

        if (emp != null) {
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

            JTextField nomField = new JTextField(emp.getNom());
            JTextField prenomField = new JTextField(emp.getPrenom());
            JTextField posteField = new JTextField(emp.getPoste());
            JTextField salaireField = new JTextField(String.valueOf(emp.getSalaire()));

            formPanel.add(new JLabel("Nom:"));
            formPanel.add(nomField);
            formPanel.add(new JLabel("Prénom:"));
            formPanel.add(prenomField);
            formPanel.add(new JLabel("Poste:"));
            formPanel.add(posteField);
            formPanel.add(new JLabel("Salaire:"));
            formPanel.add(salaireField);

            int result = JOptionPane.showConfirmDialog(
                    this, formPanel, "Modifier employé",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    emp.setNom(nomField.getText());
                    emp.setPrenom(prenomField.getText());
                    emp.setPoste(posteField.getText());
                    emp.setSalaire(Double.parseDouble(salaireField.getText()));

                    employeDAO.updateEmploye(emp);
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Employé modifié avec succès");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteEmployee(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un employé", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this, "Voulez-vous vraiment supprimer cet employé ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                employeDAO.deleteEmploye(id);
                refreshData();
                JOptionPane.showMessageDialog(this, "Employé supprimé avec succès");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshData() {
        model.setRowCount(0); // Effacer les données existantes
        List<Employe> employes = employeDAO.getAllEmployes();//récupère la liste de tous les employés
        for (Employe emp : employes) {
            model.addRow(new Object[]{
                    emp.getId(),
                    emp.getNom(),
                    emp.getPrenom(),
                    emp.getPoste(),
                    emp.getSalaire()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { //Exécute le code suivant sur le thread de l’interface graphique (Event Dispatch Thread) ».
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new EmployeFrame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}