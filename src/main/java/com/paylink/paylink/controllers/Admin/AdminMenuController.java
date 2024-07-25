package com.paylink.paylink.controllers.Admin;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.views.AdminMenuOption;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button create_client_btn;
    public Button clients_btn;
    public Button deposit_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
    }

    private void addListeners(){
        create_client_btn.setOnAction(event -> onCreateClient());
        clients_btn.setOnAction(event -> onClients());
        deposit_btn.setOnAction(event -> onDeposit());
        logout_btn.setOnAction(event -> onLogout());
    }
    private void onCreateClient(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.CREATE_CLIENT);
    }

    private void onClients(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.CLIENTS);
    }

    private void onDeposit(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.DEPOSIT);
    }
    private void onLogout(){
        //Get Stage
        Stage stage = (Stage) clients_btn.getScene().getWindow();
        //Close admin window
        Model.getInstance().getViewFactory().closeStage(stage);
        //Show Login window
        Model.getInstance().getViewFactory().showLoginWindow();
        //Set Admin Login Success Login Flag to false
        Model.getInstance().setAdminLoginSuccessFlag(false);
    }
}
