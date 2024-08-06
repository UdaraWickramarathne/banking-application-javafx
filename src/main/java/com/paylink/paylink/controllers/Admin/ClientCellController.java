package com.paylink.paylink.controllers.Admin;

import com.paylink.paylink.models.Client;
import com.paylink.paylink.models.Model;
import com.paylink.paylink.utils.CustomAlertBox;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label sv_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;

    private final Client client;

    public ClientCellController(Client client){
        this.client = client;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fName_lbl.textProperty().bind(client.firstNameProperty());
        lName_lbl.textProperty().bind(client.lastNameProperty());
        pAddress_lbl.textProperty().bind(client.payeeAddressProperty());
        ch_acc_lbl.textProperty().bind(client.checkingAccountProperty().asString());
        sv_acc_lbl.textProperty().bind(client.savingsAccountProperty().asString());
        date_lbl.textProperty().bind(client.dateCreatedProperty().asString());
        delete_btn.setOnAction(event -> deleteClientDetails());
    }

    private void deleteClientDetails(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Client Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this client? This action cannot be undone and the client's information will be permanently removed from the system.");
        if (alert.showAndWait().get() == ButtonType.OK){
            Model.getInstance().getDatabaseDriver().deleteCleint(pAddress_lbl.getText());
            Model.getInstance().getDatabaseDriver().deleteAccounts(pAddress_lbl.getText(),"CheckingAccounts");
            Model.getInstance().getDatabaseDriver().deleteAccounts(pAddress_lbl.getText(), "SavingsAccounts");

            CustomAlertBox.showAlert(Alert.AlertType.INFORMATION,"Delete successfully ","The client has been successfully deleted from the system.");

            Model.getInstance().getClients().clear();
            Model.getInstance().setClients();
        }
    }
}
