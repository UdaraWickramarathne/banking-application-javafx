package com.paylink.paylink.controllers.Utils;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.utils.CustomAlertBox;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class VerifyCodeController implements Initializable {
    public TextField code_fld;
    public Button verify_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        verify_btn.setOnAction(event -> checkVerificationCode());
    }

    private void checkVerificationCode(){
        String code = code_fld.getText();
        if(Objects.equals(code, "")){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid Field", "Please enter Verification code");
            return;
        }
        if(Objects.equals(code, Model.getInstance().getVerificationCode())){
            Stage stage = (Stage) verify_btn.getScene().getWindow();
            stage.close();

            Model.getInstance().getViewFactory().showChangePasswordView();
        }
        else {
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid!", "Invalid Verification code");
            code_fld.setText("");
        }
    }
}
