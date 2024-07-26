package com.paylink.paylink.utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomAlertBox {

    public static void showAlert(Alert.AlertType alertType, String title, String contentText){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);

        //Set title icon
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(CustomAlertBox.class.getResourceAsStream("/Images/logo.png"));
        stage.getIcons().add(icon);

        //Add css styles for AlertBox
        alert.getDialogPane().getStylesheets().add(CustomAlertBox.class.getResource("/Styles/Alert.css").toExternalForm());

        stage.initModality(Modality.APPLICATION_MODAL); //Prevent outside clicks
        alert.showAndWait();
    }
}
