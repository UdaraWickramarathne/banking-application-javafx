module com.paylink.paylink {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires de.jensd.fx.glyphs.fontawesome;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.paylink.paylink to javafx.fxml;
    exports com.paylink.paylink;
    exports com.paylink.paylink.controllers;
    exports com.paylink.paylink.models;
    exports com.paylink.paylink.services;
    exports com.paylink.paylink.views;
    exports com.paylink.paylink.controllers.Client;
    exports com.paylink.paylink.controllers.Admin;
}