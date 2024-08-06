package com.paylink.paylink.models;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;

    public DatabaseDriver(){
        try{
            this.conn = DriverManager.getConnection("jdbc:mysql://udara-mssql-udniko25-1cfd.e.aivencloud.com:14502/paylink_db","avnadmin","AVNS_Q5mHKsjZjlm1NeRLbdj");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    Client Section
     */

    public ResultSet getClientData(String pAddress, String password){
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            String query = "SELECT * FROM Clients WHERE PayeeAddress = ?";
            preparedStatement = this.conn.prepareStatement(query);
            preparedStatement.setString(1,pAddress);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String storedHash = resultSet.getString("Password");

                if(Model.getInstance().compareHash(password, storedHash)){
                    return resultSet;
                }
                else {
                    return null;
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  null;
    }

    public ResultSet getTransaction(String pAddress, int limit){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            String sql;
            if (limit == -1) {
                // No limit, get all transactions
                sql = "SELECT * FROM Transactions WHERE Sender = '" + pAddress + "' OR Receiver = '" + pAddress + "';";
            } else {
                // Use limit
                sql = "SELECT * FROM Transactions WHERE Sender = '" + pAddress + "' OR Receiver = '" + pAddress + "' ORDER BY ID DESC LIMIT " + limit + ";";
            }
            resultSet = statement.executeQuery(sql);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getClientEmail(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT Email FROM Clients WHERE PayeeAddress = '"+pAddress+"';");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }



    //Method returns savings account Balance

    public double savingAccountBalance(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        double balance = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner = '"+pAddress+"';");
            if(resultSet.next()){
               balance = resultSet.getDouble("Balance");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return balance;
    }


    // Method to either add or subtract from balance given operation

    public int updateBalance(String pAddress, double amount, String operation) {
        Statement statement;
        ResultSet resultSet = null;
        int rows = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner = '"+pAddress+"';");
            if(resultSet.next()){
                double newBalance;
                if(operation.equals("ADD")){
                    newBalance = resultSet.getDouble("Balance") + amount;
                    rows = statement.executeUpdate("UPDATE SavingsAccounts SET Balance = "+newBalance+" WHERE Owner = '"+pAddress+"'");
                }
                else {
                    if(resultSet.getDouble("Balance") >= amount){
                        newBalance = resultSet.getDouble("Balance") - amount;
                        rows = statement.executeUpdate("UPDATE SavingsAccounts SET Balance = "+newBalance+" WHERE Owner = '"+pAddress+"'");
                    }
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return rows;
    }

    //  Creates and record  new transaction

    public void newTransaction(String sender, String receiver, double amount, String message){
        PreparedStatement statement;
        String sql = "INSERT INTO Transactions (Sender, Receiver, Amount, Date, Message) VALUES (?, ?, ?, ?, ?)";
        try {
            statement = this.conn.prepareStatement(sql);
            LocalDate date = LocalDate.now();
            statement.setString(1,sender);
            statement.setString(2, receiver);
            statement.setDouble(3, amount);
            statement.setString(4, date.toString());
            statement.setString(5, message);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    /*
    Admin Section

     */

    public void createNewAdmin(String username, String password){
        PreparedStatement statement;
        String hashPassword = Model.getInstance().hashText(password);
        String sql = "INSERT INTO Admins(Username, Password) VALUES (?, ?)";
        try {
            statement = this.conn.prepareStatement(sql);
            statement.setString(1,username);
            statement.setString(2,hashPassword);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getAdminData(String username, String password){
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            String query = "SELECT * FROM Admins WHERE Username = ?";
            preparedStatement = this.conn.prepareStatement(query);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String storedHash = resultSet.getString("Password");

                if(Model.getInstance().compareHash(password, storedHash)){
                    return resultSet;
                }
                else {
                    return null;
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return  null;

    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date, String email){
        PreparedStatement statement;
        String hashPassword = Model.getInstance().hashText(password);
        String sql = "INSERT INTO Clients(FirstName, LastName, PayeeAddress, Password, Date, Email) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            statement = this.conn.prepareStatement(sql);
            statement.setString(1, fName);
            statement.setString(2, lName);
            statement.setString(3, pAddress);
            statement.setString(4, hashPassword);
            statement.setString(5, date.toString());
            statement.setString(6, email);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(String owner, String number, double lLimit, double balance){
        PreparedStatement statement;
        String sql = "INSERT INTO CheckingAccounts(Owner, AccountNumber, TransactionLimit, Balance) VALUES (?, ?, ?, ?)";
        try {
            statement =  this.conn.prepareStatement(sql);
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, lLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createSavingAccount(String owner, String number, double lLimit, double balance){
        PreparedStatement statement;
        String sql = "INSERT INTO SavingsAccounts(Owner, AccountNumber, WithdrawalLimit, Balance) VALUES (?, ?, ?, ?)";
        try {
            statement =  this.conn.prepareStatement(sql);
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, lLimit);
            statement.setDouble(4, balance);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getAllClientData(){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients;");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }



    public void depositSavings(String pAddress, double amount){
        Statement statement;
        try {
            statement = this.conn.createStatement();
            statement.executeUpdate("UPDATE SavingsAccounts SET Balance = "+amount+" WHERE Owner = '"+pAddress+"'");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteCleint(String pAddres){
        PreparedStatement statement;
        String sql = "DELETE FROM Clients WHERE PayeeAddress = ?";
        try {
            statement = this.conn.prepareStatement(sql);
            statement.setString(1,pAddres);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteAccounts(String pAddress, String tableName){
        PreparedStatement statement;
        String sql = "DELETE FROM "+tableName+" WHERE Owner = ?";
        try {
            statement = this.conn.prepareStatement(sql);
            statement.setString(1,pAddress);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    /*
    Utility Section

     */

    public ResultSet searchClient(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress = '"+pAddress+"'");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }


    public int getLastClientsId(){
        Statement statement;
        ResultSet resultSet;
        int id = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM sequence_tracking WHERE name = 'Clients'");
            if(resultSet.next()) {
                id = resultSet.getInt("seq");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return id;
    }

    public ResultSet getCheckingAccountData(String pAddress){
        PreparedStatement statement;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM CheckingAccounts WHERE owner = ?";
        try {
            statement = this.conn.prepareStatement(sql);
            statement.setString(1,pAddress);
            resultSet = statement.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getSavingsAccountData(String pAddress){
        PreparedStatement statement;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM SavingsAccounts WHERE owner = ?";
        try {
            statement = this.conn.prepareStatement(sql);
            statement.setString(1,pAddress);
            resultSet = statement.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    public void updateClient(String pAddress, String fName, String lName, String email){
        PreparedStatement statement;
        String sql = "UPDATE Clients SET FirstName = ?, LastName = ?, Email = ? WHERE PayeeAddress = ?";
        try {
            statement = this.conn.prepareStatement(sql);
            statement.setString(1,fName);
            statement.setString(2,lName);
            statement.setString(3,email);
            statement.setString(4,pAddress);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}
