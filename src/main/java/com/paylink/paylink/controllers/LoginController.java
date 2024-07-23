package com.paylink.paylink.controllers;

import com.paylink.paylink.models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField username_txt;
    public PasswordField password_txt;
    public CheckBox password_chk;
    public Button login_btn;
    public Label forget_lbl;
    public Label error_lbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login_btn.setOnAction(event -> Model.getInstance().getViewFactory().showClientWindow());
    }
}
