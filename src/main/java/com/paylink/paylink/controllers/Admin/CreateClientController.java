package com.paylink.paylink.controllers.Admin;

import com.paylink.paylink.models.Model;
import com.paylink.paylink.utils.CustomAlertBox;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
    public Label lbl_usd_c;
    public Label lbl_usd_s;

    private String payeeAddress;
    private boolean createCheckingAccountFlag;
    private boolean createSavingsAccountFlag;
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFormatter<String> textFormatter1 = new TextFormatter<>(filter);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
        sv_amount_fld.setTextFormatter(textFormatter1);
        ch_amount_fld.setTextFormatter(textFormatter2);
        //Default hide the account types
        sv_amount_fld.setVisible(false);
        ch_amount_fld.setVisible(false);
        lbl_usd_s.setVisible(false);
        lbl_usd_c.setVisible(false);
        pAddress_lbl.setText("");

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
        lbl_usd_s.setVisible(sv_acc_box.isSelected());
    }

    private void onCheckingAccountChkChange(){
        ch_amount_fld.setVisible(ch_acc_box.isSelected());
        lbl_usd_c.setVisible(ch_acc_box.isSelected());
    }

    private void onPasswordCheckChanged(){
        passwordVisible(show_password_box, password_txt_fld, password_fld);
    }

    public static void passwordVisible(CheckBox showPasswordBox, TextField passwordTxtFld, PasswordField passwordFld) {
        if(showPasswordBox.isSelected()){
            //show password
            passwordTxtFld.setText(passwordFld.getText());
            passwordTxtFld.setVisible(true);
            passwordFld.setVisible(false);
        }
        else {
            //Hide Password
            passwordFld.setText(passwordTxtFld.getText());
            passwordFld.setVisible(true);
            passwordTxtFld.setVisible(false);
        }
    }

    private void createClient(){
        String fName = fName_fld.getText();
        String lName = lName_fld.getText();
        String password = password_fld.getText();
        String email = email_fld.getText();

        if(fName.isEmpty() || lName.isEmpty() || password.isEmpty() || email.isEmpty()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Please fill all the fields before you continue.");
            return;
        }

        if(!isValidEmail(email)){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","\""+email + "\" is not a valid email address.");
            return;
        }

        if(pAddress_lbl.getText().isEmpty()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Please check Get Payee Address box before you continue.");
            return;
        }

        if(!ch_acc_box.isSelected()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Please check checking account property.");
            return;
        }
        if(!sv_acc_box.isSelected()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Please check savings account property.");
            return;
        }

        //Create Checking Account
        if(ch_amount_fld.getText().isEmpty()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Please enter Checking Account amount.");
            return;
        }
        else if(Double.parseDouble(ch_amount_fld.getText()) < 1000){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Checking Account minimum deposit balance is $1000.");
            return;
        }
        if(createCheckingAccountFlag){
            createAccount("Checking");
        }

        //Create savings account
        if(sv_amount_fld.getText().isEmpty()){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Please enter Savings Account amount.");
            return;
        }
        else if(Double.parseDouble(sv_amount_fld.getText()) < 1000){
            CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Error","Savings Account minimum deposit balance is $1000.");
            return;
        }

        if (createSavingsAccountFlag){
            createAccount("Savings");
        }

        //Create Client

        Model.getInstance().getDatabaseDriver().createClient(fName, lName,payeeAddress, password, LocalDate.now(),email);
        error_lbl.setStyle("-fx-text-fill: #28A745; -fx-font-weight: bold; -fx-font-size: 1.3em;");
        error_lbl.setText("Client Created Successfully!");
        emptyFields();
        onCheckingAccountChkChange();
        onSavingAccountChkChange();
    }

    private void createAccount(String accountType){
        double balance = 0;
        //Generate Account number
        String firstSection = "4135";
        String lastSection = Integer.toString(new Random().nextInt((9999 - 1000) + 1) + 1000);
        String accNumber = firstSection + " " + lastSection;

        //Create Checking or Saving account
        if (accountType.equals("Checking")){
            balance = Double.parseDouble(ch_amount_fld.getText());
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accNumber, 10, balance);
        }
        else {
            balance = Double.parseDouble(sv_amount_fld.getText());
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

    public boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

    UnaryOperator<TextFormatter.Change> filter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) { // Allow only digits
            return change;
        }
        CustomAlertBox.showAlert(Alert.AlertType.ERROR, "Invalid", "You can only enter numbers!");
        return null; // Reject the change
    };

}
