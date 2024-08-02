package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.views.ClientMenuOption;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button account_btn;
    public Button logout_btn;
    public TextArea report_txt;
    public Button report_btn;
    public Button request_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransaction());
        account_btn.setOnAction(event -> onAccounts());
        logout_btn.setOnAction(event -> onLogout());
        request_btn.setOnAction(event -> onRequest());
    }

    private void onDashboard(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOption.DASHBOARD);
    }

    private void onTransaction(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOption.TRANSACTION);
    }

    private void onAccounts(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOption.ACCOUNTS);
    }

    private void onRequest(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOption.REQUEST);
    }

    private void onLogout(){
        //Get Stage
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        //Close stage
        Model.getInstance().getViewFactory().closeStage(stage);
        //Show Login window
        Model.getInstance().getViewFactory().showLoginWindow();
        //Set Client Login Success Login Flag to false
        Model.getInstance().setClientLoginSuccessFlag(false);

        Model.getInstance().reset();
    }
}
