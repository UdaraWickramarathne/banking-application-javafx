package com.paylink.paylink.controllers.Client;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.utils.CustomAlertBox;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class RequestMoneyController implements Initializable {
    public ImageView qrImage;
    public Button cp_link_btn;
    public Label result_lbl;
    public TextField amount_txt;
    public Button generate_btn;
    public Label description_lbl;
    public Button reset_btn;

    Image qrCodeImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFormatter<String> textFormatter1 = new TextFormatter<>(filter);
        amount_txt.setTextFormatter(textFormatter1);
        qrCodeImage = new Image(getClass().getResourceAsStream("/Images/qr-code.gif"));
        qrImage.setImage(qrCodeImage);
        hideFields();

        generate_btn.setOnAction(event -> generateQrCode());
        reset_btn.setOnAction(event -> {
            qrImage.setImage(qrCodeImage);
            hideFields();
            amount_txt.setText("");
        });

        cp_link_btn.setOnAction(event -> copyToClipboard());

    }


    private void generateQrCode(){
        if(amount_txt.getText().isEmpty()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Please fill the amount field before you continue.");
            return;
        }
        try {
            Double amount = Double.parseDouble(amount_txt.getText());
            qrImage.setImage(Model.getInstance().generateQRCode(Model.getInstance().getClient().payeeAddressProperty().get(),amount));
            amount_txt.setText("");
            description_lbl.setVisible(true);
            cp_link_btn.setVisible(true);
        }
        catch (NumberFormatException e){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Please provide valid amount!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void hideFields(){
        cp_link_btn.setVisible(false);
        description_lbl.setVisible(false);
        result_lbl.setVisible(false);

    }

    private void copyToClipboard(){
        // Get the system clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();

        // Create a ClipboardContent object and set the text
        ClipboardContent content = new ClipboardContent();
        content.putString(Model.getInstance().getUrl());

        // Set the clipboard content
        clipboard.setContent(content);
        result_lbl.setVisible(true);
    }
    UnaryOperator<TextFormatter.Change> filter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) { // Allow only digits
            return change;
        }
        CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid", "You can only enter numbers!");
        return null; // Reject the change
    };
}
