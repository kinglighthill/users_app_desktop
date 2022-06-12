package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.viewmodels.VideosGridScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/VideosGridScreen.fxml")
public class VideosGridScreenController implements FxmlView<VideosGridScreenVM>, Initializable {

    @FXML
    private StackPane searchPane;

    @FXML
    private Button backButton, mostPopularButton, newButton, highlyRatedButton;

    @FXML
    private ImageView searchIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFont();

    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        searchIcon.setImage(new Image(getClass().getResource("/drawable/search_icon2.png").toString()));

//        searchPane.setBackground(Background.EMPTY);
    }

    private void initializeFont() {
        mostPopularButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        newButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        highlyRatedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

    }
}
