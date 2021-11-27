package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Connect;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        String path = "jamb_utme.db";
        if (Connect.connect(path)) {
            StringBuilder text = new StringBuilder("Application connected to db successfully!");
            ResultSet resultSet  = Connect.getSubjectNames();
            Optional<ResultSet> result = Optional.ofNullable(resultSet);

            if (result.isPresent()) {
                try {
                    ResultSet set  = result.get();
                    while (set.next()) {
                        String subjectName = set.getString("subject_name");
                        text.append("\n").append(subjectName);
                    }
                    welcomeText.setText(text.toString());
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                welcomeText.setText(text.toString());
            }
        } else {
            welcomeText.setText("Welcome to JavaFX Application!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(location.toString());
    }
}