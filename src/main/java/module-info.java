module com.scholarly.utme {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    requires io.reactivex.rxjava3;
    requires org.pdfsam.rxjavafx;
    requires sqlite.jdbc;
    requires de.saxsys.mvvmfx;
//    requires kotlin.stdlib;

    exports com.scholarly.utme;
    exports com.scholarly.utme.controller;
    exports com.scholarly.utme.ui.listcells;
    exports com.scholarly.utme.viewmodels;
    exports com.scholarly.utme.data.dao;
    exports com.scholarly.utme.data.util;
    exports com.scholarly.utme.data.model;

    opens com.scholarly.utme.ui.listcells to javafx.fxml;
    opens com.scholarly.utme to de.saxsys.mvvmfx, javafx.fxml;
    opens com.scholarly.utme.viewmodels to de.saxsys.mvvmfx, javafx.fxml;
    opens com.scholarly.utme.controller to de.saxsys.mvvmfx, javafx.fxml;
    opens layouts to de.saxsys.mvvmfx, javafx.fxml;
}