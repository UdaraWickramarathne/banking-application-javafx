package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestMoneyController implements Initializable {
    public ImageView qrImage;
    public Button cp_link_btn;
    public Label result_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getImage();
    }

    private void getImage(){
        try{
            qrImage.setImage(Model.getInstance().generateQRCode(Model.getInstance().getClient().payeeAddressProperty().get()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
