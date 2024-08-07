package com.paylink.paylink.controllers.Utils;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.models.SharedData;
import com.paylink.paylink.utils.CustomAlertBox;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChangePassword implements Initializable {
    public PasswordField password_fld;
    public TextField password_txt;
    public PasswordField new_password_fld;
    public TextField new_password_txt;
    public Button change_btn;
    public Label error_lbl;
    public CheckBox password_chk;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        password_txt.setVisible(false);
        new_password_txt.setVisible(false);

        password_chk.setOnAction(event -> onShowPassword());
        change_btn.setOnAction(event -> onChangePassword());
    }

    private void onChangePassword(){
        error_lbl.setText("");
        if(password_chk.isSelected()){
            checkPasswords(password_txt, new_password_txt);
        }
        else {
            checkPasswords(password_fld, new_password_fld);
        }

    }

    private void checkPasswords(TextField passwordFld, TextField newPasswordFld) {
        if(passwordFld.getText().isEmpty() || newPasswordFld.getText().isEmpty()){
            error_lbl.setText("Please fill al the fields");
            return;
        }
        if(Objects.equals(passwordFld.getText(), newPasswordFld.getText())){
            String hashPassword = Model.getInstance().hashText(newPasswordFld.getText());
            Model.getInstance().getDatabaseDriver().updatePassword(hashPassword, SharedData.getInstance().getPayee());
            passwordFld.setText("");
            newPasswordFld.setText("");
            CustomAlertBox.showAlert(Alert.AlertType.INFORMATION, "Successful", "Password Reset Success. You can Login again!");

            Stage stage = (Stage) change_btn.getScene().getWindow();
            stage.close();
        }
        else {
            error_lbl.setText("Passwords are mismatch. Try again");
        }
    }

    private void onShowPassword(){
        if(password_chk.isSelected()){
            password_txt.setText(password_fld.getText());
            new_password_txt.setText(new_password_fld.getText());

            password_fld.setVisible(false);
            new_password_fld.setVisible(false);

            password_txt.setVisible(true);
            new_password_txt.setVisible(true);
        }
        else {
            password_fld.setText(password_txt.getText());
            new_password_fld.setText(new_password_txt.getText());

            password_txt.setVisible(false);
            new_password_txt.setVisible(false);

            password_fld.setVisible(true);
            new_password_fld.setVisible(true);
        }
    }
}
