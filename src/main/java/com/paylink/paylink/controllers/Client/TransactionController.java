package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.models.Transaction;
import com.paylink.paylink.views.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    public ListView<Transaction> transaction_listview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllTransactionList();
        transaction_listview.setItems(Model.getInstance().getAllTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());

    }

    private void initAllTransactionList(){
        if(Model.getInstance().getAllTransactions().isEmpty()){
            Model.getInstance().setAllTransactions();
        }
    }
}
