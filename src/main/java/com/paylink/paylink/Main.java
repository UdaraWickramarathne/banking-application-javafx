package com.paylink.paylink;

import com.paylink.paylink.controllers.Client.TransactionController;
import com.paylink.paylink.models.Model;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


public class Main extends Application{


    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showLoginWindow();
    }


}
