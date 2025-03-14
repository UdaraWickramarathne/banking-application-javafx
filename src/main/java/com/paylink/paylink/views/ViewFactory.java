package com.paylink.paylink.views;

import com.paylink.paylink.controllers.Admin.AdminController;
import com.paylink.paylink.controllers.Client.ClientController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewFactory {
    private AccountType loginAccountType;

    //Client views
    private ObjectProperty<ClientMenuOption>     clientSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane transactionView;
    private AnchorPane accountsView;
    private AnchorPane requestView;

    //Admin views
    private AnchorPane createClientView;
    private AnchorPane clientsView;
    private AnchorPane depositView;
    private ObjectProperty<AdminMenuOption> adminSelectedMenuItem;

    //Reset password
    private Stage passwordStage;

    public ViewFactory(){
        this.loginAccountType = AccountType.CLIENT;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    /* Client view section*/

    public ObjectProperty<ClientMenuOption> getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    public AnchorPane getDashboardView(){
        if (dashboardView == null){
            try{
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane getTransactionView() {
        if (transactionView == null) {
            try {
                transactionView = new FXMLLoader(getClass().getResource("/Fxml/Client/Transactions.fxml")).load();
            }catch (Exception e){e.printStackTrace();}
        }
        return transactionView;
    }

    public AnchorPane getAccountsView() {
        if (accountsView == null) {
            try {
                accountsView = new FXMLLoader(getClass().getResource("/Fxml/Client/Accounts.fxml")).load();
            }catch (Exception e){e.printStackTrace();}
        }
        return accountsView;
    }

    public AnchorPane getRequestView() {
        if(requestView == null){
            try {
                requestView = new FXMLLoader(getClass().getResource("/Fxml/Client/RequestMoney.fxml")).load();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return requestView;
    }


    public void showClientWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }


    /*
     * Admin view section
     */

    public ObjectProperty<AdminMenuOption> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public AnchorPane getCreateClientView() {
        if(createClientView == null){
            try{
                createClientView = new FXMLLoader(getClass().getResource("/Fxml/Admin/CreateClient.fxml")).load();
            }catch (Exception e){e.printStackTrace();}
        }
        return createClientView;
    }

    public AnchorPane getClientsView() {
        if(clientsView == null){
            try{
                clientsView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Clients.fxml")).load();
            }catch (Exception e){e.printStackTrace();}
        }
        return clientsView;
    }

    public AnchorPane getDepositView() {
        if(depositView == null){
            try {
                depositView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Deposit.fxml")).load();
            }catch (Exception e) {e.printStackTrace();}
        }

        return depositView;
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);

        resetState();
    }

    //Reset current states
    private void resetState() {
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
        this.dashboardView = null;
        this.transactionView = null;
        this.accountsView = null;
        this.createClientView = null;
        this.clientsView = null;
        this.depositView = null;
    }

    public void showMessageWindow(String pAddress, String messageText){

        StackPane pane = new StackPane();
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        Label sender = new Label(pAddress);
        sender.setStyle("-fx-font-weight: bold");
        Label message = new Label(messageText);
        hBox.getChildren().addAll(sender,message);
        pane.getChildren().add(hBox);
        Scene scene = new Scene(pane,300,100);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/logo.png"))));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Message");
        stage.setScene(scene);
        stage.show();

    }


    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        }catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/logo.png"))));
        stage.setResizable(false);
        stage.setTitle("PayLink Bank");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }


    // Reset password and change password methods

    public void showResetPasswordView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Utils/ResetPassword.fxml"));
            AnchorPane root = loader.load();
            passwordStage = new Stage();
            passwordStage.setScene(new Scene(root));
            passwordStage.setTitle("Reset Password");
            passwordStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/logo.png"))));
            passwordStage.initModality(Modality.APPLICATION_MODAL);
            passwordStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showChangePasswordView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Utils/ChangePassword.fxml"));
            AnchorPane root = loader.load();
            passwordStage = new Stage();
            passwordStage.setScene(new Scene(root));
            passwordStage.setTitle("Change Password");
            passwordStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/logo.png"))));
            passwordStage.initModality(Modality.APPLICATION_MODAL);
            passwordStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showVerifyCode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Utils/VerifyCode.fxml"));
            AnchorPane root = loader.load();
            passwordStage = new Stage();
            passwordStage.setScene(new Scene(root));
            passwordStage.setTitle("Change Password");
            passwordStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/logo.png"))));
            passwordStage.initModality(Modality.APPLICATION_MODAL);
            passwordStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
