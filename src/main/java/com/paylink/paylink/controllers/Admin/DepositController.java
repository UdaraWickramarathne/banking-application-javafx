package com.paylink.paylink.controllers.Admin;

import com.paylink.paylink.models.Client;
import com.paylink.paylink.models.Model;
import com.paylink.paylink.views.ClientCellFactory;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepositController implements Initializable {


    public TextField pAddress_fld;
    public ListView<Client> result_listview;
    public TextField amount_fld;
    public Button deposit_btn;
    public Button search_btn;
    public Label info_lbl;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        search_btn.setOnAction(event -> onClientSearch());
        deposit_btn.setOnAction(event -> onDeposit());
    }

    private void onClientSearch(){
        info_lbl.setText("");
        ObservableList<Client> searchResult = Model.getInstance().searchClient(pAddress_fld.getText());
        result_listview.setItems(searchResult);
        result_listview.setCellFactory(e -> new ClientCellFactory());
        if(searchResult.isEmpty()){
            client = null;
            info_lbl.setStyle("-fx-text-fill: red");
            info_lbl.setText("No client found with the provided payee address.");
        }
        else {
            client = searchResult.get(0);
        }

    }

    private void onDeposit(){
        double amount = Double.parseDouble(amount_fld.getText());
        double newBalance = amount + client.savingsAccountProperty().get().balanceProperty().get();
        if(amount_fld.getText() != null){
            Model.getInstance().getDatabaseDriver().depositSavings(client.payeeAddressProperty().get(), newBalance);
        }
        emptyFields();
        result_listview.setItems(FXCollections.emptyObservableList());
        info_lbl.setStyle("-fx-text-fill: green");
        info_lbl.setText("Deposit Successfully");
    }

    private void emptyFields(){
        pAddress_fld.setText("");
        amount_fld.setText("");
    }
}
