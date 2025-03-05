package com.monprojet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GestionUtilisateur {

    public void add(Connexion connect, Scanner sc) {
        System.out.print("Nom de l'utilisateur : ");
        String lastName = sc.nextLine();

        System.out.print("Prénom de l'utilisateur : ");
        String firstName = sc.nextLine();

        System.out.print("Email de l'utilisateur : ");
        String email = sc.nextLine();

        try {
            String sqlInsert = "INSERT INTO utilisateurs (prenom, nom, email) VALUES (?, ?, ?)";
            PreparedStatement pstmtInsert = connect.connexion.prepareStatement(sqlInsert);
            pstmtInsert.setString(1, firstName);
            pstmtInsert.setString(2, lastName);
            pstmtInsert.setString(3, email);

            int rowsAffected = pstmtInsert.executeUpdate();
            System.out.println("Insertion réussie, lignes affectées : " + rowsAffected);
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }

    public void liste(Connexion connect) {
        try {
            String sqlSelect = "SELECT * FROM utilisateurs";
            PreparedStatement pstmtSelect = connect.connexion.prepareStatement(sqlSelect);
            ResultSet rs = pstmtSelect.executeQuery();

            System.out.println("\nListe des utilisateurs :");
            while (rs.next()) {
                int id = rs.getInt("id");
                String prenom = rs.getString("prenom");
                String nom = rs.getString("nom");
                String email = rs.getString("email");

                System.out.println("ID: " + id + " | Nom: " + nom + " | Prénom: " + prenom + " | Email: " + email);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }

    public void supprimer(Connexion connect, Scanner sc) {
        System.out.print("ID de l'utilisateur à supprimer : ");
        int id = sc.nextInt();
        sc.nextLine(); // Nettoyer le buffer

        try {
            String sqlDelete = "DELETE FROM utilisateurs WHERE id = ?";
            PreparedStatement pstmtDelete = connect.connexion.prepareStatement(sqlDelete);
            pstmtDelete.setInt(1, id);

            int rowsAffected = pstmtDelete.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Utilisateur supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }

    public void modifier(Connexion connect, Scanner sc) {
        System.out.print("ID de lutilisateur a modifier : ");
        int id = sc.nextInt();
        sc.nextLine(); // Nettoyer le buffer

        System.out.print("Nouveau prénom : ");
        String newPrenom = sc.nextLine();

        System.out.print("Nouveau nom : ");
        String newNom = sc.nextLine();

        System.out.print("Nouvel email : ");
        String newEmail = sc.nextLine();

        try {
            String sqlUpdate = "UPDATE utilisateurs SET prenom = ?, nom = ?, email = ? WHERE id = ?";
            PreparedStatement pstmtUpdate = connect.connexion.prepareStatement(sqlUpdate);
            pstmtUpdate.setString(1, newPrenom);
            pstmtUpdate.setString(2, newNom);
            pstmtUpdate.setString(3, newEmail);
            pstmtUpdate.setInt(4, id);

            int rowsAffected = pstmtUpdate.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Utilisateur mis à jour avec succes !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }
    
    public void chercher(Connexion connect, Scanner sc) {
        System.out.print("Email de l'utilisateur à rechercher : ");
        String email = sc.nextLine();

        try {
            String sqlSearch = "SELECT * FROM utilisateurs WHERE email = ?";
            PreparedStatement pstmtSearch = connect.connexion.prepareStatement(sqlSearch);
            pstmtSearch.setString(1, email);

            ResultSet rs = pstmtSearch.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String prenom = rs.getString("prenom");
                String nom = rs.getString("nom");

                System.out.println("Utilisateur trouvé :");
                System.out.println("ID: " + id + " | Nom: " + nom + " | Prenom: " + prenom + " | Email: " + email);
            } else {
                System.out.println("Aucun utilisateur trouve avec cet email.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }

    
}