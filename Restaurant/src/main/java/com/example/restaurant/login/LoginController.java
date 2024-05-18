package com.example.restaurant.login;

import com.example.restaurant.Helpers;
import com.example.restaurant.database.DBConnect;
import com.example.restaurant.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class LoginController extends Helpers {
    private static String loggedInAdminName; // Declare the loggedInAdminName variable
    private static String loggedInCashierName; // Declare the loggedInAdminName variable
    DBConnect connection = new DBConnect();

    @FXML
    private Label lblUsername, lblPassword;
    @FXML
    private TextField txtUsername, txtPassword;

    @FXML
    private ObservableList<User> usrList = FXCollections.observableArrayList();

    @FXML
    protected void onLoginClick(ActionEvent event){
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String password1 = txtPassword.getPromptText().trim();
        if (username.isEmpty() || (password.isEmpty() && password1.isEmpty())) {
            showMessage(Alert.AlertType.WARNING, "Login Failed", "Username Atau Password Kosong!", 18);
        } else {
            try {
                connection.stat = connection.conn.createStatement();
                String query = "SELECT password, usr_role, usr_nama FROM ms_user WHERE username = '"+username+"'";
                connection.result = connection.stat.executeQuery(query);

                if (connection.result.next()){
                    String usr_password = connection.result.getString("password");
                    String usr_role = connection.result.getString("usr_role");

                    if (password.equals(usr_password) || password1.equals(usr_password)) {

                        if (usr_role.equals("1")) {
                            String loggedInAdminName = connection.result.getString("usr_nama");
                            setLoggedInAdminName(loggedInAdminName); // Set the logged-in admin's name

                            showMessage(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + loggedInAdminName + "!", 18);
                            changePage(event, "manager");
                        } else if (usr_role.equals("0")) {
                            String loggedInCashierName = username;
                            setLoggedInCashierName(loggedInCashierName); // Set the logged-in admin's name

                            showMessage(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + loggedInCashierName + "!", 18);
                            changePage(event, "cashier");
                        }
                    } else {
                        showMessage(Alert.AlertType.ERROR, "Login Failed", "Incorrect password", 18);
                    }
                } else {
                    showMessage(Alert.AlertType.ERROR, "Login Failed", "Username not found", 18);
                }

                connection.stat.close();
            } catch (SQLException e) {
                System.out.println("An error occurred while logging in: " + e.getMessage());
            }
        }

    }

    public void showMessage(Alert.AlertType alertType, String title, String message, double fontSize) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
//        dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); // Ganti "style.css" dengan path CSS Anda
//        dialogPane.getStyleClass().add("custom-alert");
//        dialogPane.lookup(".content.label").setStyle("-fx-font-size: " + fontSize + "px");

        alert.showAndWait();
    }

    private static void setLoggedInAdminName(String adminName) {
        loggedInAdminName = adminName;
    }
    public static String getLoggedInAdminName() {
        return loggedInAdminName;
    }
    private static void setLoggedInCashierName(String cashierName) {
        loggedInCashierName = cashierName;
    }
    public static String getLoggedInCashierName() {
        return loggedInCashierName;
    }
}
