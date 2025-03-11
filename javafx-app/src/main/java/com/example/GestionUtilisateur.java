package com.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GestionUtilisateur {
    public void add(Connexion connect, Scanner sc) {
        System.out.print("Nom de l'utilisateur : ");
        String lastName = sc.nextLine();

        System.out.print("Email de l'utilisateur : ");
        String email = sc.nextLine();

        try {
            String sqlInsert = "INSERT INTO utilisateurs (nom, email) VALUES (?, ?)";
            PreparedStatement pstmtInsert = connect.connexion.prepareStatement(sqlInsert);
            pstmtInsert.setString(1, lastName);
            pstmtInsert.setString(2, email);

            int rowsAffected = pstmtInsert.executeUpdate();
            System.out.println("Insertion réussie, lignes affectées : " + rowsAffected);
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }
}
