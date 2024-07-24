package com.paylink.paylink.controllers;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField username_txt;
    public PasswordField password_txt;
    public CheckBox password_chk;
    public Button login_btn;
    public Label forget_lbl;
    public Label error_lbl;
    public ChoiceBox<AccountType> choice_box;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choice_box.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        choice_box.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        choice_box.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoginAccountType(choice_box.getValue()));
        login_btn.setOnAction(event -> onLogin());
    }

    private void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT){
            Model.getInstance().getViewFactory().showClientWindow();
        }else {
            Model.getInstance().getViewFactory().showAdminWindow();
        }


    }
}
