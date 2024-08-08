package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.models.Transaction;
import com.paylink.paylink.utils.CustomAlertBox;
import com.paylink.paylink.utils.NotificationUtil;
import com.paylink.paylink.views.TransactionCellFactory;
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
        setTransaction_listview();
        send_money_btn.setOnAction(event -> onSendMoney());
        accountSummery();
    }

    private void setTransaction_listview(){
        transaction_listview.getItems().clear();
        initLatestTransactionsList();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
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
            String message = "This is a default message";
            if(!message_fld.getText().isEmpty()){
                message = message_fld.getText();
            }
            String sender = Model.getInstance().getClient().payeeAddressProperty().get();
            ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
            try {
                if (resultSet.next()){

                    //Subtract from Sender's savings account
                    int result = Model.getInstance().getDatabaseDriver().updateBalance(sender,amount,"SUB");
                    if(result == 1){
                        //Add money to receiver savings account
                        Model.getInstance().getDatabaseDriver().updateBalance(receiver,amount, "ADD");
                        //Update the savings account balance in the client object
                        Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().savingAccountBalance(sender));

                        //Record new transaction
                        Model.getInstance().getDatabaseDriver().newTransaction(sender, receiver, amount, message);

                        //Clear fields
                        payee_fld.setText("");
                        amount_fld.setText("");
                        message_fld.setText("");


                        //Send email for receiver
                        NotificationUtil.sendMail(sender,receiver,amount,resultSet.getString("Email"),"Receiver");


                        //I removed because my Email free plan limit is reached

                        //Send email for Sender
                        NotificationUtil.sendMail(sender,receiver,amount,Model.getInstance().getClient().emailAddressProperty().get(),"Sender");

                        CustomAlertBox.showAlert(Alert.AlertType.INFORMATION, "Payment Successfully!", "Thank you for using our service. Your transaction has been successfully completed.");
                        setTransaction_listview();
                        accountSummery();

                    }
                    else {
                        CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Payment Failed!", "Insufficient balance for this transaction. Please enter a smaller amount.");
                    }
                }
                else {
                    CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid Receiver!", "Invalid Receiver. Please double check before you proceed!");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else{
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Fields are Empty!", "You must fill the Payee Address and Amount Field!");
        }
    }

    // Method calculates all expenses and income

    private void accountSummery(){
        double income = 0;
        double expenses = 0;
        Model.getInstance().setAllTransactions();
        for (Transaction transaction : Model.getInstance().getAllTransactions()){
            if (transaction.senderProperty().get().equals(Model.getInstance().getClient().payeeAddressProperty().get())){
                expenses = expenses + transaction.amountProperty().get();
            }
            else {
                income = income + transaction.amountProperty().get();
            }

        }
        income_lbl.setText("+ $" + income);
        expense_lbl.setText("- $" + expenses);
    }
}






















