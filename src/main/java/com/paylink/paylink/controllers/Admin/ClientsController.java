package com.paylink.paylink.controllers.Admin;

import com.paylink.paylink.models.Client;
import com.paylink.paylink.models.Model;
import com.paylink.paylink.views.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {

    public ListView<Client> clients_listview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClientList();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(e -> new ClientCellFactory());
    }

    private void initClientList(){
        if(Model.getInstance().getClients().isEmpty()){
            Model.getInstance().setClients();
        }
    }
}
