use paylink_db;
CREATE TABLE Admins (
    ID INT NOT NULL AUTO_INCREMENT,
    Username VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE CheckingAccounts (
    ID INT NOT NULL AUTO_INCREMENT,
    Owner VARCHAR(255) NOT NULL,
    AccountNumber VARCHAR(255) NOT NULL,
    TransactionLimit DECIMAL(15, 2) NOT NULL,
    Balance DECIMAL(15, 2) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE Clients (
    ID INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    PayeeAddress VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Date DATE NOT NULL,
    Email VARCHAR(255),
    PRIMARY KEY (ID)
);

CREATE TABLE SavingsAccounts (
    ID INT NOT NULL AUTO_INCREMENT,
    Owner VARCHAR(255) NOT NULL,
    AccountNumber VARCHAR(255) NOT NULL,
    WithdrawalLimit DECIMAL(15, 2) NOT NULL,
    Balance DECIMAL(15, 2) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE Transactions (
    ID INT NOT NULL AUTO_INCREMENT,
    Sender VARCHAR(255) NOT NULL,
    Receiver VARCHAR(255) NOT NULL,
    Amount DECIMAL(15, 2) NOT NULL,
    Date DATE NOT NULL,
    Message TEXT,
    PRIMARY KEY (ID)
);

CREATE TABLE sequence_tracking (
    name VARCHAR(255) PRIMARY KEY,
    seq INT NOT NULL
);

INSERT INTO sequence_tracking (name, seq) 
VALUES 
    ('Admins', 0),
    ('CheckingAccounts', 0),
    ('Clients', 0),
    ('SavingsAccounts', 0),
    ('Transactions', 0);
DELIMITER //

CREATE TRIGGER admins_after_insert
AFTER INSERT ON Admins
FOR EACH ROW
BEGIN
    UPDATE sequence_tracking
    SET seq = NEW.ID + 1
    WHERE name = 'Admins';
END//

CREATE TRIGGER checking_accounts_after_insert
AFTER INSERT ON CheckingAccounts
FOR EACH ROW
BEGIN
    UPDATE sequence_tracking
    SET seq = NEW.ID + 1
    WHERE name = 'CheckingAccounts';
END//

CREATE TRIGGER clients_after_insert
AFTER INSERT ON Clients
FOR EACH ROW
BEGIN
    UPDATE sequence_tracking
    SET seq = NEW.ID + 1
    WHERE name = 'Clients';
END//

CREATE TRIGGER savings_accounts_after_insert
AFTER INSERT ON SavingsAccounts
FOR EACH ROW
BEGIN
    UPDATE sequence_tracking
    SET seq = NEW.ID + 1
    WHERE name = 'SavingsAccounts';
END//

CREATE TRIGGER transactions_after_insert
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    UPDATE sequence_tracking
    SET seq = NEW.ID + 1
    WHERE name = 'Transactions';
END//

DELIMITER ;


    
 -- TABLE Admins AUTO_INCREMENT = 1;
 





