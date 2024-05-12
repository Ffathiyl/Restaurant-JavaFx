package com.example.restaurant.menu;

import com.example.restaurant.Helpers;
import com.example.restaurant.login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CashierController extends Helpers {
    @FXML
    private AnchorPane Content;
    @FXML
    private BorderPane borderPane;

    public void onHomeClicked(MouseEvent mouseEvent) {
        Content.getChildren().clear();
    }

    public void onMenuClicked(MouseEvent mouseEvent) {
        loadPage("menu");
    }

    public void onTransactionClicked(MouseEvent mouseEvent) {
        loadPage("transaction");
    }

    public void onKeluarClicked(ActionEvent event) {
        LoginController loginController = new LoginController();
        loginController.showMessage(Alert.AlertType.INFORMATION, "Berhasil Keluar", "Terima Kasih, " + LoginController.getLoggedInCashierName() + ".", 18);
        changePage(event,"login");
    }

    private void loadPage(String page){
        try {
            String resourcePath = "/com/example/restaurant/"+page+".fxml";
            System.out.println("Resource Path: " + resourcePath); // Cetak path file FXML

            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent root = loader.load();

            Content.getChildren().setAll(root);
        } catch (IOException e) {
            Logger.getLogger(CashierController.class.getName()).log(Level.SEVERE, null, e);
        }
    }


}