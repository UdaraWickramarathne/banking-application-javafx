package com.paylink.paylink.controllers.Admin;

import com.paylink.paylink.models.Client;
import com.paylink.paylink.models.Model;
import com.paylink.paylink.utils.CustomAlertBox;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DepositController implements Initializable {


    public TextField pAddress_fld;
    public TextField amount_fld;
    public Button deposit_btn;
    public Button search_btn;
    public Label info_lbl;
    public Button edit_btn;
    public Button update_btn;
    public HBox result_list;
    public TextField fName_fld;
    public TextField lName_fld;
    public TextField payee_add_fld;
    public TextField email_fld;
    public TextField ch_acc_num;
    public TextField ch_bal_fld;
    public TextField sv_bal_fld;
    public TextField sv_acc_num;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        search_btn.setOnAction(event -> onClientSearch());
        deposit_btn.setOnAction(event -> onDeposit());
        result_list.setVisible(false);
        edit_btn.setVisible(false);
        update_btn.setVisible(false);
        textFieldDisable();
        edit_btn.setOnAction(event -> textFieldEnable());
        update_btn.setOnAction(event -> updateDetails());

    }

    private void onClientSearch(){
        info_lbl.setText("");
        ObservableList<Client> searchResult = Model.getInstance().searchClient(pAddress_fld.getText());
        if(searchResult.isEmpty()){
            client = null;
            info_lbl.setStyle("-fx-text-fill: red");
            info_lbl.setText("No client found with the provided payee address.");
        }
        else {
            result_list.setVisible(true);
            edit_btn.setVisible(true);
            update_btn.setVisible(true);
            client = searchResult.get(0);
            fName_fld.setText(client.firstNameProperty().get());
            lName_fld.setText(client.lastNameProperty().get());
            payee_add_fld.setText(client.payeeAddressProperty().get());
            email_fld.setText(client.emailAddressProperty().get());

            ch_acc_num.setText(client.checkingAccountProperty().get().accountNumberProperty().get());
            ch_bal_fld.setText(String.valueOf(client.checkingAccountProperty().get().balanceProperty().get()));
            sv_acc_num.setText(client.savingsAccountProperty().get().accountNumberProperty().get());
            sv_bal_fld.setText(String.valueOf(client.savingsAccountProperty().get().balanceProperty().get()));
        }

    }

    private void onDeposit(){
        if (!amount_fld.getText().isEmpty() && !payee_add_fld.getText().isEmpty()){
            double amount = Double.parseDouble(amount_fld.getText());
            double newBalance = amount + client.savingsAccountProperty().get().balanceProperty().get();
            if(amount_fld.getText() != null){
                Model.getInstance().getDatabaseDriver().depositSavings(client.payeeAddressProperty().get(), newBalance);
            }
            emptyFields();
            info_lbl.setStyle("-fx-text-fill: green");
            info_lbl.setText("Deposit Successfully");
        }

    }

    private void emptyFields(){
        pAddress_fld.setText("");
        amount_fld.setText("");
        fName_fld.setText("");
        lName_fld.setText("");
        payee_add_fld.setText("");
        email_fld.setText("");
        ch_acc_num.setText("");
        ch_bal_fld.setText("");
        sv_bal_fld.setText("");
        sv_acc_num.setText("");
        pAddress_fld.setText("");
    }

    private void textFieldDisable(){
        fName_fld.setDisable(true);
        lName_fld.setDisable(true);
        payee_add_fld.setDisable(true);
        email_fld.setDisable(true);
        ch_acc_num.setDisable(true);
        ch_bal_fld.setDisable(true);
        sv_bal_fld.setDisable(true);
        sv_acc_num.setDisable(true);
    }

    private void textFieldEnable(){
        fName_fld.setDisable(false);
        lName_fld.setDisable(false);
        email_fld.setDisable(false);
    }

    private void updateDetails(){
        if(!fName_fld.getText().isEmpty() && !lName_fld.getText().isEmpty() && !email_fld.getText().isEmpty()){
            Model.getInstance().getDatabaseDriver().updateClient(payee_add_fld.getText(), fName_fld.getText(), lName_fld.getText(), email_fld.getText());
            emptyFields();
            info_lbl.setStyle("-fx-text-fill: green");
            info_lbl.setText("Client Details Updated!");
            textFieldDisable();
            update_btn.setVisible(false);
            edit_btn.setVisible(false);
        }
        else {
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid Information!", "Please ensure all necessary fields are filled out before submitting your update.");
        }
    }
}
