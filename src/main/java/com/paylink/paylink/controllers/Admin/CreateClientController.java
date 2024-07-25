package com.paylink.paylink.controllers.Admin;

import com.paylink.paylink.models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField fName_fld;
    public TextField lName_fld;
    public PasswordField password_fld;
    public CheckBox pAddress_box;
    public CheckBox show_password_box;
    public Label pAddress_lbl;
    public CheckBox ch_acc_box;
    public TextField ch_amount_fld;
    public CheckBox sv_acc_box;
    public TextField sv_amount_fld;
    public Button create_client_btn;
    public Label error_lbl;
    public TextField password_txt_fld;
    public TextField email_fld;

    private String payeeAddress;
    private boolean createCheckingAccountFlag;
    private boolean createSavingsAccountFlag;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Default hide the account types
        sv_amount_fld.setVisible(false);
        ch_amount_fld.setVisible(false);

        password_txt_fld.setVisible(false);

        sv_acc_box.setOnAction(event -> onSavingAccountChkChange());
        ch_acc_box.setOnAction(event -> onCheckingAccountChkChange());
        show_password_box.setOnAction(event -> onPasswordCheckChanged());

        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue){
                    payeeAddress = createPayeeAddress();
                    onCreatePayeeAddress();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        });

        ch_acc_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                createCheckingAccountFlag = true;
            }
        });

        sv_acc_box.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                createSavingsAccountFlag = true;
            }
        });

    }

    //Show hide Accounts create types
    private void onSavingAccountChkChange(){
        sv_amount_fld.setVisible(sv_acc_box.isSelected());
    }

    private void onCheckingAccountChkChange(){
        ch_amount_fld.setVisible(ch_acc_box.isSelected());
    }

    private void onPasswordCheckChanged(){
        if(show_password_box.isSelected()){
            //show password
            password_txt_fld.setText(password_fld.getText());
            password_txt_fld.setVisible(true);
            password_fld.setVisible(false);
        }
        else {
            //Hide Password
            password_fld.setText(password_txt_fld.getText());
            password_fld.setVisible(true);
            password_txt_fld.setVisible(false);
        }
    }

    private void createClient(){
        //Create Checking Account
        if(createCheckingAccountFlag){
            createAccount("Checking");
        }

        if (createSavingsAccountFlag){
            createAccount("Savings");
        }

        //Create Client
        String fName = fName_fld.getText();
        String lName = lName_fld.getText();
        String password = password_fld.getText();
        String email = email_fld.getText();
        Model.getInstance().getDatabaseDriver().createClient(fName, lName,payeeAddress, password, LocalDate.now(),email);
        error_lbl.setStyle("-fx-text-fill: #28A745; -fx-font-weight: bold; -fx-font-size: 1.3em;");
        error_lbl.setText("Client Created Successfully!");
        emptyFields();
    }

    private void createAccount(String accountType){
        double balance = Double.parseDouble(ch_amount_fld.getText());
        //Generate Account number
        String firstSection = "4135";
        String lastSection = Integer.toString(new Random().nextInt((9999 - 1000) + 1) + 1000);
        String accNumber = firstSection + " " + lastSection;

        //Create Checking or Saving account
        if (accountType.equals("Checking")){
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accNumber, 10, balance);
        }
        else {
            Model.getInstance().getDatabaseDriver().createSavingAccount(payeeAddress, accNumber, 2000, balance);
        }

    }

    private void onCreatePayeeAddress(){
        if (fName_fld.getText() != null && lName_fld.getText() != null){
            pAddress_lbl.setText(payeeAddress);
        }
    }

    private String createPayeeAddress() {
        String firstName = fName_fld.getText();
        String lastName = lName_fld.getText();

        // Check if both fields are not null and not empty
        if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
            int id = Model.getInstance().getDatabaseDriver().getLastClientsId() + 1;
            char fChar = Character.toLowerCase(firstName.charAt(0));
            return "@" + fChar + lastName + id;
        }
        return "";
    }


    private void emptyFields(){
        fName_fld.setText("");
        lName_fld.setText("");
        password_fld.setText("");
        password_txt_fld.setText("");
        pAddress_box.setSelected(false);
        pAddress_lbl.setText("");
        ch_acc_box.setSelected(false);
        ch_amount_fld.setText("");
        sv_amount_fld.setText("");
        sv_acc_box.setSelected(false);
        email_fld.setText("");
    }

}
