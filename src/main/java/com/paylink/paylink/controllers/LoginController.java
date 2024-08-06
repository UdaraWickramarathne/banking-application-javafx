package com.paylink.paylink.controllers;

import com.paylink.paylink.controllers.Admin.CreateClientController;
import com.paylink.paylink.models.Model;
import com.paylink.paylink.views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public PasswordField password_txt;
    public CheckBox password_chk;
    public Button login_btn;
    public Label forget_lbl;
    public Label error_lbl;
    public ChoiceBox<AccountType> choice_box;
    public TextField payee_address_fld;
    public TextField password_fld;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choice_box.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        choice_box.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        choice_box.valueProperty().addListener(observable -> setChoice_box());
        login_btn.setOnAction(event -> onLogin());
        forget_lbl.setOnMouseClicked(mouseEvent -> onForgotPassword());


        //Handling password field
        password_fld.setVisible(false);
        password_fld.managedProperty().bind(password_fld.visibleProperty());
        password_txt.managedProperty().bind(password_txt.visibleProperty());

        password_chk.setOnAction(event -> onPasswordCheckChanged());
    }

    private void onPasswordCheckChanged(){
        CreateClientController.passwordVisible(password_chk, password_fld, password_txt);
    }

    private void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        boolean loginSuccess = false;
        if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT){
            //Evaluate Client Login Credentials
            if(password_chk.isSelected()){
                Model.getInstance().evaluateClientCred(payee_address_fld.getText(), password_fld.getText());
            }
            else {
                Model.getInstance().evaluateClientCred(payee_address_fld.getText(), password_txt.getText());
            }
            if (Model.getInstance().getClientLoginSuccessFlag()){
                Model.getInstance().getViewFactory().showClientWindow();
                //Close the login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            }
            else {
                payee_address_fld.setText("");
                password_txt.setText("");
                password_fld.setText("");
                error_lbl.setText("Invalid Login Credentials");
            }
        }else {
            //Evaluate admin login credentials
            if(password_chk.isSelected()){
                Model.getInstance().evaluateAdminCred(payee_address_fld.getText(), password_fld.getText());
            }
            else {
                Model.getInstance().evaluateAdminCred(payee_address_fld.getText(), password_txt.getText());
            }
            if(Model.getInstance().getAdminLoginSuccessFlag()){
                Model.getInstance().getViewFactory().showAdminWindow();
                //Close the stage
                Model.getInstance().getViewFactory().closeStage(stage);
            }
            else {
                payee_address_fld.setText("");
                password_txt.setText("");
                password_fld.setText("");
                error_lbl.setText("Invalid Login Credentials");
            }
        }
    }

    private void setChoice_box(){
        Model.getInstance().getViewFactory().setLoginAccountType(choice_box.getValue());
        //Change payee address prompt box
        if (choice_box.getValue() == AccountType.ADMIN){
            payee_address_fld.setPromptText("Username");
        }
        else {
            payee_address_fld.setPromptText("Payee Address");
        }
    }

    private void onForgotPassword() {
        Model.getInstance().getViewFactory().showResetPasswordView();
    }
}
