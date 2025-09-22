#  Application de Gestion Commerciale – Java JDBC

Ce projet est une application de **gestion commerciale** développée en **Java** avec **IntelliJ IDEA** et une base de données **MySQL**.  
Elle permet de gérer les employés, les produits et les ventes au sein d’une entreprise.

---

##  Fonctionnalités principales

- **Authentification** (écran de connexion)
- **Gestion des employés** : ajout, modification, suppression, recherche
- **Gestion des produits** : ajout, modification, suppression, recherche
- **Gestion des ventes** : enregistrement des ventes, suivi et statistiques
- **Tableau de bord / statistiques** pour visualiser les données

---

## 🛠 Technologies utilisées
- **Langage :** Java 11+  
- **IDE :** IntelliJ IDEA  
- **Base de données :** MySQL  
- **Connexion :** JDBC  
- **Interface graphique :** Swing (JFrame)

---

##  Structure du projet

src/
 ├─ dao/                      # Couche d'accès aux données
 │   ├─ impl/                  # Implémentations des DAO
 │   │   ├─ EmployeDAOImpl.java
 │   │   ├─ ProduitDAOImpl.java
 │   │   └─ VenteDAOImpl.java
 │   ├─ IEmployeDAO.java
 │   ├─ IProduitDAO.java
 │   └─ IVenteDAO.java
 │
 ├─ metier/entity/             # Classes métier (entités)
 │   ├─ Employe.java
 │   ├─ Produit.java
 │   └─ Vente.java
 │
 ├─ service/                   # Logique de gestion (services)
 │   └─ (classes de service si présentes)
 │
 ├─ presentation/              # Interface utilisateur (Swing)
 │   ├─ AuthFrame.java         # Fenêtre de connexion
 │   ├─ EmployeFrame.java      # Gestion des employés
 │   ├─ ProduitFrame.java      # Gestion des produits
 │   ├─ VenteFrame.java        # Gestion des ventes
 │   └─ StatistiqueFrame.java  # Tableau de statistiques
 │
 └─ SingletonConnection.java   # Classe de connexion à MySQL

 ---

##  Installation & Exécution

### 1 Prérequis
- **Java JDK 11+** installé
- **MySQL** installé et en cours d'exécution
- **IntelliJ IDEA** ou autre IDE compatible

### 2 Cloner le projet
```bash
git clone https://github.com/<ton-utilisateur>/gestion-commerciale.git
cd gestion-commerciale

---

##  Captures d'écran
### authentification



