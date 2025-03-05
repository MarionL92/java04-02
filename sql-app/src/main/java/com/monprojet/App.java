package com.monprojet;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World !");

        Connexion connexion = new Connexion();
        int choix;
        Scanner sc = new Scanner(System.in);
        GestionUtilisateur gu = new GestionUtilisateur();

        do {
            System.out.println("\nQue voulez-vous faire ?");
            System.out.println("1 - Ajouter un utilisateur");
            System.out.println("2 - Lister les utilisateurs");
            System.out.println("3 - Supprimer un utilisateur");
            System.out.println("4 - Modifier un utilisateur");
            System.out.println("5 - Chercher un utilisateur");
            System.out.println("0 - Quitter");
            System.out.print("Votre choix : ");
            choix = sc.nextInt();
            sc.nextLine(); // Pour éviter les problèmes avec nextInt()

            switch (choix) {
                case 1:
                    gu.add(connexion, sc);
                    break;
                case 2:
                    gu.liste(connexion);
                    break;
                case 3:
                    gu.supprimer(connexion, sc);
                    break;
                case 4:
                    gu.modifier(connexion, sc);
                    break;
                case 5:
                    gu.chercher(connexion, sc);
                    break;
                case 0:
                    System.out.println("Fermeture de l application...");
                    break;
                default:
                    System.out.println("L action demandee n  existe pas !");
            }
        } while (choix != 0);

        connexion.close();
        sc.close();
        System.exit(0);
    }
}