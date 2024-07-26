package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.models.Transaction;
import com.paylink.paylink.views.TransactionCellFactory;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
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
    public ListView<Transaction> transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextArea message_fld;
    public Button send_money_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindData();
        initLatestTransactionsList();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
        send_money_btn.setOnAction(event -> onSendMoney());
    }

    private void bindData(){
        username.textProperty().bind(Bindings.concat("Hi, ").concat(Model.getInstance().getClient().firstNameProperty()));
        login_date.setText("Today, " + LocalDate.now());
        checking_bal.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString());
        checking_acc_num.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());
        saving_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
        saving_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());
    }

    private void initLatestTransactionsList(){
        if (Model.getInstance().getLatestTransactions().isEmpty()){
            Model.getInstance().setLatestTransactions();
        }
    }

    private void onSendMoney(){
        if (!payee_fld.getText().isEmpty() && !amount_fld.getText().isEmpty()){
            String receiver = payee_fld.getText();
            double amount = Double.parseDouble(amount_fld.getText());
            String message = "This is default message";
            if(!message_fld.getText().isEmpty()){
                message = message_fld.getText();
            }
            String sender = Model.getInstance().getClient().payeeAddressProperty().get();
            ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
            try {
                if (resultSet.next()){
                    Model.getInstance().getDatabaseDriver().updateBalance(receiver,amount, "ADD");
                    //Subtract from Sender's savings account
                    Model.getInstance().getDatabaseDriver().updateBalance(sender,amount,"SUB");

                    //Update the savings account balance in the client object

                    Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().savingAccountBalance(sender));

                    //Record new transaction

                    Model.getInstance().getDatabaseDriver().newTransaction(sender, receiver, amount, message);

                    //Clear fields
                    payee_fld.setText("");
                    amount_fld.setText("");
                    message_fld.setText("");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else{

        }
    }
}
