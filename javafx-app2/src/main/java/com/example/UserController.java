package com.monprojet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {

  
    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TextField searchField; // 💡 Ajout de cette ligne pour éviter l'erreur
    @FXML private TextField deleteIdField;
    @FXML private Button deleteButton;
    @FXML private TextField nameField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final Connexion connexion = new Connexion();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadUsers();

        // Permettre l'édition des colonnes
        tableView.setEditable(true);
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colPrenom.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());

        colName.setOnEditCommit(event -> updateUser(event.getRowValue().getId(), "nom", event.getNewValue()));
        colPrenom.setOnEditCommit(event -> updateUser(event.getRowValue().getId(), "prenom", event.getNewValue()));
        colEmail.setOnEditCommit(event -> updateUser(event.getRowValue().getId(), "email", event.getNewValue()));
    }


    private void clearFields() {
        nameField.clear();
        prenomField.clear();
        emailField.clear();
        deleteIdField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void searchUser() {
        String searchText = searchField.getText().toLowerCase();    
        // Si le champ de recherche est vide, recharger tous les utilisateurs
        if (searchText.isEmpty()) {
            loadUsers();
            return;
        }
        
        String sql = "SELECT * FROM utilisateurs WHERE LOWER(nom) LIKE ? OR LOWER(email) LIKE ?";
        try (PreparedStatement pstmt = connexion.connexion.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchText + "%");
            pstmt.setString(2, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();
        
            users.clear(); // Efface l'ancienne liste d'utilisateurs
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                users.add(new User(id, nom, prenom, email));
            }
            tableView.setItems(users); // Met à jour la table avec les résultats de recherche
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la recherche des utilisateurs : " + e.getMessage());
        }
    }

    private void loadUsers() {
        String sql = "SELECT * FROM utilisateurs";
        try (PreparedStatement pstmt = connexion.connexion.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            users.clear(); // Efface l'ancienne liste d'utilisateurs
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                users.add(new User(id, nom, prenom, email));
            }
            tableView.setItems(users); // Met à jour la table avec les utilisateurs récupérés
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des utilisateurs : " + e.getMessage());
        }   
    }


    @FXML
    private void addUser() {
        String nom = nameField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

    String sql = "INSERT INTO utilisateurs (nom, prenom, email, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connexion.connexion.prepareStatement(sql,  PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, prenom); 
            pstmt.setString(3, email);
            pstmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();
            
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId= generatedKeys.getInt(1);
                users.add(new User(generatedId, nom, prenom, email));
                tableView.setItems(users);
            }

            clearFields();
            showAlert("Succès", "Utilisateur ajouté avec succès.");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    private void updateUser(int id, String column, String newValue) {
        String sql = "UPDATE utilisateurs SET " + column + " = ? WHERE id = ?";
        try (PreparedStatement pstmt = connexion.connexion.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            showAlert("Succès", "Utilisateur mis à jour avec succès.");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    @FXML
    private void deleteUser() {
        String idText = deleteIdField.getText();
        if (idText.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un ID d'utilisateur à supprimer.");
            return;
        }

        int id = Integer.parseInt(idText);
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (PreparedStatement pstmt = connexion.connexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                users.removeIf(user -> user.getId() == id);
                tableView.setItems(FXCollections.observableArrayList(users));
                showAlert("Succès", "Utilisateur supprimé avec succès.");
            } else {
                showAlert("Erreur", "Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
       }
    }
}