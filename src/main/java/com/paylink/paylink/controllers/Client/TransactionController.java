package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.models.Transaction;
import com.paylink.paylink.views.TransactionCellFactory;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController extends Thread implements Initializable {
    public ListView<Transaction> transaction_listview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAllTransactionList();
        transaction_listview.setItems(Model.getInstance().getAllTransactions());
        transaction_listview.setCellFactory(e -> new TransactionCellFactory());

        this.start();

    }

    private void initAllTransactionList(){
        if(Model.getInstance().getAllTransactions().isEmpty()){
            Model.getInstance().setAllTransactions();
        }
    }

    public void updateTransactionView(){
        Platform.runLater(() -> {
            if (transaction_listview != null) {
                transaction_listview.getItems().clear();
                Model.getInstance().setAllTransactions();
                transaction_listview.setItems(Model.getInstance().getAllTransactions());
                transaction_listview.setCellFactory(e -> new TransactionCellFactory());
            }
        });
    }

    @Override
    public void run() {
        while (Thread.activeCount() != 6){
            try {
                Thread.sleep(5000);
            }
            catch (Exception e){

            }
            updateTransactionView();

        }

    }

}
