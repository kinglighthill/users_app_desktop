module com.scholarly.utme {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;
    requires io.reactivex.rxjava3;
//    requires org.xerial.sqlitejdbc;

    opens com.scholarly.utme to javafx.fxml;
    exports com.scholarly.utme;
    exports com.scholarly.utme.controller;
    opens com.scholarly.utme.controller to javafx.fxml;
}