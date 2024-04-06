package com.scholarly;

import com.scholarly.ui.utils.FontUtil;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.io.InputStream;

public class SplashScreen {
    final private Pane pane;
    final private SequentialTransition seqT;

    public SplashScreen() {
        pane = new StackPane();
        pane.setStyle("-fx-background-color:white");
        seqT = new SequentialTransition();
    }

    public void show() {
        Paint textColor = Paint.valueOf("#053500");

        InputStream iconStream = MainApplication.class.getResourceAsStream("/drawable/app_logo.png");
        assert iconStream != null;
        Image icon = new Image(iconStream);
        ImageView iconImage = new ImageView(icon);
        iconImage.setFitWidth(48);
        iconImage.setFitHeight(48);

        Label title = new Label("Scholarly");
        title.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 36));
        title.setTextFill(textColor);

        Label tagLine = new Label("Learning is simple...");
        tagLine.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.REGULAR, 16));
        tagLine.setTextFill(textColor);

        VBox vBox = new VBox(title, tagLine);
        vBox.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(8, iconImage, vBox);
        hBox.setAlignment(Pos.CENTER);

        double mid = 1.0f;

        ScaleTransition st = new ScaleTransition(Duration.millis(100), hBox);

        st.setFromX(mid);
        st.setFromY(mid);
        st.setByX(mid);
        st.setByY(mid);
        st.setToX(mid);
        st.setToY(mid);

        st.setCycleCount(10);
        st.setAutoReverse(true);

        pane.getChildren().addAll(hBox);

        seqT.getChildren().add(st);
        seqT.play();
        seqT.setNode(hBox);
    }

    public Parent getRoot() {
        return pane;
    }

    public SequentialTransition getSequentialTransition() {
        return seqT;
    }
}