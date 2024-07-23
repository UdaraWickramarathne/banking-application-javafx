package com.paylink.paylink.controllers.Client;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text username;
    public Label login_date;
    public Label saving_acc_num;
    public Label saving_bal;
    public Label checking_acc_num;
    public Label checking_bal;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextArea message_fld;
    public Button send_money_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
