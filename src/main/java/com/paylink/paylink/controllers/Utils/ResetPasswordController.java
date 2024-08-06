package com.paylink.paylink.controllers.Utils;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.utils.CustomAlertBox;
import com.paylink.paylink.utils.NotificationUtil;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {
    public Button send_code_btn;
    public TextField payee_address;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        send_code_btn.setOnAction(event -> onSendCode());
    }

    private void onSendCode(){
        String payee = payee_address.getText();

        if(payee.isEmpty()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid Field", "Please enter Your payee Address first!");
            return;
        }
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientEmail(payee);
        try{
            if(resultSet.next()){
                String email = resultSet.getString("Email");
                Model.getInstance().generateVerificationCode();
                NotificationUtil.sendVerification(email, payee, Model.getInstance().getVerificationCode());
                Stage stage = (Stage) send_code_btn.getScene().getWindow();
                stage.close();

                Model.getInstance().getViewFactory().showVerifyCode();
            }
            else {
                CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid!", "Invalid payee address");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
