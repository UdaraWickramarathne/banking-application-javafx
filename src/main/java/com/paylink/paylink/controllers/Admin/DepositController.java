package com.paylink.paylink.controllers.Admin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepositController implements Initializable {


    public TextField pAddress_fld;
    public FontAwesomeIconView search_btn;
    public ListView result_listview;
    public TextField amount_fld;
    public Button deposit_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
