package com.paylink.paylink.controllers.Admin;

import com.paylink.paylink.models.Client;
import com.paylink.paylink.models.Model;
import com.paylink.paylink.views.ClientCellFactory;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {

    public ListView<Client> clients_listview;
    public Button refresh_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClientList();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(e -> new ClientCellFactory());

        refresh_btn.setOnAction(event -> updateClientsView());
    }

    private void initClientList(){
        if(Model.getInstance().getClients().isEmpty()){
            Model.getInstance().setClients();
        }
    }

    public void updateClientsView(){
        if (clients_listview != null) {
            clients_listview.getItems().clear();
            Model.getInstance().setClients();
            clients_listview.setItems(Model.getInstance().getClients());
            clients_listview.setCellFactory(e -> new ClientCellFactory());
        }
    }


}
