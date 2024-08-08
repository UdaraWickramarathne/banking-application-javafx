package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.utils.CustomAlertBox;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

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
        TextFormatter<String> textFormatter1 = new TextFormatter<>(filter);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);

        amount_to_sv.setTextFormatter(textFormatter1);
        amount_to_ch.setTextFormatter(textFormatter2);
        trans_to_sv_btn.setOnAction(event -> onCheckingToSaving());

    }

    private void setData(){
        ch_acc_num.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());
        ch_acc_date.textProperty().bind(Model.getInstance().getClient().dateCreatedProperty().asString());
        ch_acc_bal.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString());
        sv_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());
        sv_acc_date.textProperty().bind(Model.getInstance().getClient().dateCreatedProperty().asString());
        sv_acc_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
    }

    public void onCheckingToSaving(){
        if(amount_to_sv.getText().isEmpty()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Field is Empty!", "You must fill the Amount Field!");
            return;
        }
        double checkingAcc = Double.parseDouble(ch_acc_bal.getText());
        double transferAmount = Double.parseDouble(amount_to_sv.getText());
        if(transferAmount < checkingAcc){
            //Adding to savings account
            Model.getInstance().getDatabaseDriver().updateBalance(Model.getInstance().getClient().payeeAddressProperty().get(),transferAmount,"ADD");

            //Subtract from checking account
            Model.getInstance().getDatabaseDriver().updateCheckingBalance(Model.getInstance().getClient().payeeAddressProperty().get(),transferAmount,"SUB");

            CustomAlertBox.showAlert(Alert.AlertType.INFORMATION, "Money Transfer success!", "Money transfer success. It will update soon!");
            amount_to_sv.setText("");
        }
        else {
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Payment Failed!", "Insufficient balance for this transaction. Please enter a smaller amount.");
        }


    }

    UnaryOperator<TextFormatter.Change> filter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) { // Allow only digits
            return change;
        }
        CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid", "You can only enter numbers!");
        return null; // Reject the change
    };

}
