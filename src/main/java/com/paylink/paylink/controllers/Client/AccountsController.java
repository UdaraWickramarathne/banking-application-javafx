package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label withdraw_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public TextField amount_to_ch;
    public Button trans_to_sv_btn;
    public Button trans_to_ch_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    private void setData(){
        ch_acc_num.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());
        ch_acc_date.textProperty().bind(Model.getInstance().getClient().dateCreatedProperty().asString());
        ch_acc_bal.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString());
        sv_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());
        sv_acc_date.textProperty().bind(Model.getInstance().getClient().dateCreatedProperty().asString());
        sv_acc_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
    }
}
