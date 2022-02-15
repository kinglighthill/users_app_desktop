module com.scholarly.utme {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    requires io.reactivex.rxjava3;
    requires org.pdfsam.rxjavafx;
    requires sqlite.jdbc;
    requires de.saxsys.mvvmfx;
    requires freetts;
    requires com.google.gson;

    exports com.scholarly.utme;
    exports com.scholarly.utme.controller;
    exports com.scholarly.utme.ui.listcells;
    exports com.scholarly.utme.viewmodels;
    exports com.scholarly.utme.data.dao;
    exports com.scholarly.utme.data.util;
    exports com.scholarly.utme.data.model;

    opens com.scholarly.utme.ui.listcells to javafx.fxml;
    opens com.scholarly.utme.data.model.newDb.contentType to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.text to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.image to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.table to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.audio to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.video to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.webview to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.html to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.cbt to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.orderedList to com.google.gson;
    opens com.scholarly.utme.data.model.newDb.contentType.unorderedList to com.google.gson;
    opens com.scholarly.utme to de.saxsys.mvvmfx, javafx.fxml;
    opens com.scholarly.utme.viewmodels to de.saxsys.mvvmfx, javafx.fxml;
    opens com.scholarly.utme.controller to de.saxsys.mvvmfx, javafx.fxml;
    opens layouts to de.saxsys.mvvmfx, javafx.fxml;
    exports com.scholarly.utme.data.model.newDb;
}