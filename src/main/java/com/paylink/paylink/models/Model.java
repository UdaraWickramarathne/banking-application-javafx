package com.paylink.paylink.models;

import com.paylink.paylink.views.AccountType;
import com.paylink.paylink.views.ViewFactory;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalDate;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;

    //Client Data Section
    private final Client client;
    private boolean clientLoginSuccessFlag;
    private final ObservableList<Transaction> latestTransactions;
    private final ObservableList<Transaction> allTransactions;
    //Admin Data Section
    private boolean adminLoginSuccessFlag;
    private final ObservableList<Client> clients;

    private Model() {
        this.databaseDriver = new DatabaseDriver();
        this.viewFactory = new ViewFactory();

        //Client data section
        this.clientLoginSuccessFlag = false;
        this.client = new Client("","","",null,null, null, "");
        this.latestTransactions = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();

        //Admin data section
        this.adminLoginSuccessFlag = false;
        this.clients = FXCollections.observableArrayList();
    }

    public static synchronized Model getInstance(){
        if (model == null){
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    /*
    Client Method Section
     */

    public boolean getClientLoginSuccessFlag(){
        return this.clientLoginSuccessFlag;
    }

    public void setClientLoginSuccessFlag(boolean clientLoginSuccessFlag) {
        this.clientLoginSuccessFlag = clientLoginSuccessFlag;
    }

    public Client getClient() {
        return client;
    }

    public void evaluateClientCred(String pAddress, String password){
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;

        ResultSet resultSet = databaseDriver.getClientData(pAddress,password);

        try {
            if (resultSet.next()){
                this.client.firstNameProperty().set(resultSet.getString("FirstName"));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.payeeAddressProperty().set(resultSet.getString("PayeeAddress"));
                this.client.emailAddressProperty().set(resultSet.getString("Email"));
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                this.client.dateCreatedProperty().set(date);
                checkingAccount = getCheckingAccount(pAddress);
                savingsAccount = getSavingsAccount(pAddress);
                this.client.checkingAccountProperty().set(checkingAccount);
                this.client.savingsAccountProperty().set(savingsAccount);
                this.clientLoginSuccessFlag = true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void prepareTransactions(ObservableList<Transaction> transactions, int limit){
        ResultSet resultSet = databaseDriver.getTransaction(this.client.payeeAddressProperty().get(), limit);
        try {
            while (resultSet.next()){
                String sender = resultSet.getString("Sender");
                String receiver = resultSet.getString("Receiver");
                double amount = resultSet.getDouble("Amount");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                String message = resultSet.getString("Message");
                transactions.add(new Transaction(sender, receiver, amount, date, message));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setLatestTransactions(){
        prepareTransactions(this.latestTransactions, 4);
    }

    public ObservableList<Transaction> getLatestTransactions(){
        return latestTransactions;
    }

    public void setAllTransactions(){
        prepareTransactions(this.allTransactions, -1);
    }

    public ObservableList<Transaction> getAllTransactions() {
        return allTransactions;
    }

    /*
    Admin Method Section
     */

    public boolean getAdminLoginSuccessFlag() {
        return adminLoginSuccessFlag;
    }

    public void setAdminLoginSuccessFlag(boolean adminLoginSuccessFlag) {
        this.adminLoginSuccessFlag = adminLoginSuccessFlag;
    }

    public void evaluateAdminCred(String username, String password){
        ResultSet resultSet = databaseDriver.getAdminData(username, password);
        try {
            if (resultSet.next()){
                this.adminLoginSuccessFlag = true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void setClients(){
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;

        ResultSet resultSet = databaseDriver.getAllClientData();
        try {
            while (resultSet.next()){
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String pAddress = resultSet.getString("PayeeAddress");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                String email = resultSet.getString("Email");
                checkingAccount = getCheckingAccount(pAddress);
                savingsAccount = getSavingsAccount(pAddress);
                clients.add(new Client(fName, lName, pAddress, checkingAccount, savingsAccount, date, email));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ObservableList<Client> searchClient(String pAddress){
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        ResultSet resultSet = databaseDriver.searchClient(pAddress);
        try {
            CheckingAccount checkingAccount = getCheckingAccount(pAddress);
            SavingsAccount savingsAccount = getSavingsAccount(pAddress);
            if(resultSet.next()){
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                String email = resultSet.getString("Email");
                searchResults.add(new Client(fName, lName, pAddress, checkingAccount, savingsAccount, date, email));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return searchResults;
    }

    /*

    Utility Methods

     */

    public CheckingAccount getCheckingAccount(String pAddress){
        CheckingAccount account = null;
        ResultSet resultSet = databaseDriver.getCheckingAccountData(pAddress);
        String num = "00000000";
        int tLimit = 0;
        double balance = 0;
        try{
            if(resultSet.next()){
                num = resultSet.getString("AccountNumber");
                tLimit = (int) resultSet.getDouble("TransactionLimit");
                balance = resultSet.getDouble("Balance");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        account = new CheckingAccount(pAddress, num, balance, tLimit);
        return account;
    }

    public SavingsAccount getSavingsAccount(String pAddress){
        SavingsAccount account = null;
        ResultSet resultSet = databaseDriver.getSavingsAccountData(pAddress);
        String num = "00000000";
        double wLimit = 0;
        double balance = 0;
        try{
            if(resultSet.next()){
                num = resultSet.getString("AccountNumber");
                wLimit = (int) resultSet.getDouble("WithdrawalLimit");
                balance = resultSet.getDouble("Balance");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        account = new SavingsAccount(pAddress, num, balance, wLimit);
        return account;
    }
}
