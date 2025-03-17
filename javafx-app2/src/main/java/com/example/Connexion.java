package com.monprojet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private final String url = "jdbc:mysql://localhost:8889/mabasejava"; // Vérifie le port ici
    private final String utilisateur = "root";
    private final String motDePasse = "root";

    public Connection connexion;

    public Connexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
            System.out.println("Connexion réussie à la base de données !");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println(" Erreur de connexion à la base : " + e.getMessage());
        }
    }

    public void close() {
        if (this.connexion != null) { 
            try { 
                this.connexion.close(); 
                System.out.println("Connexion fermée avec succès."); 
            } catch (SQLException e) { 
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage()); 
            } 
        } 
    }
}