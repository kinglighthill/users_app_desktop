package com.scholarly.utme.ui.utils;


import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.scene.layout.Panel;

/**
 * Helper class to show alerts
 */
public class Alerts {

    public static Alert info(Class<?> thisClass, String windowTitle, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(windowTitle);
        alert.setHeaderText(header);
        alert.setContentText(description);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(thisClass.getResource("/drawable/app_logo.png").toString()));

        return alert;
    }


    public static Alert error(Class<?> thisClass, String windowTitle, String header, String description) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(windowTitle);
        alert.setHeaderText(header);
        alert.setContentText(description);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(thisClass.getResource("/drawable/app_logo.png").toString()));
        return alert;
    }

    public static Dialog<ButtonType> dialog(Class<?> thisClass, String windowTitle, String header, String description) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(windowTitle);
        dialog.setHeaderText(header);

        DialogPane dialogPane = new DialogPane();
        dialogPane.setContentText(description);
        dialogPane.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        dialogPane.setMinSize(350, 120);
        dialogPane.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        dialog.setDialogPane(dialogPane);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(thisClass.getResource("/drawable/app_logo.png").toString()));
        return dialog;
    }

    public static Dialog<ButtonType> activateDialog(Class<?> thisClass, String windowTitle, String header, String description) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(windowTitle);
        dialog.setHeaderText(header);

        VBox activateDialog =new VBox(25);
        activateDialog.setMaxSize(380, 450);
        activateDialog.setPrefSize(250, 400);
        activateDialog.setStyle("fx-background-color: white; -fx-background-radius: 8;");
        activateDialog.setAlignment(Pos.CENTER);

        ImageView padlockImage = new ImageView(new Image(thisClass.getResource("/drawable/activate_screen_images/padlock_icon.png").toString()));
        padlockImage.setFitHeight(30);
        padlockImage.setFitWidth(40);
        ImageView closeIcon = new ImageView(new Image(thisClass.getResource("/drawable/close_icon.png").toString()));
        closeIcon.setFitHeight(20);
        closeIcon.setFitWidth(20);
        Panel panel = new Panel();
        panel.setLeft(padlockImage);
        panel.setRight(closeIcon);

        Label activateHeaderText = new Label("Activate your App for Unlimited Access");
        activateHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
        activateHeaderText.setTextFill(Paint.valueOf("#12AF20"));
        activateHeaderText.setWrapText(true);

        HBox hBox1 = new HBox(15);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        ImageView greenTick1 = new ImageView(new Image(thisClass.getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTick1.setFitHeight(20);
        greenTick1.setFitWidth(20);
        Label text1 = new Label("Unlock all Note Topics");
        text1.setFont(Font.font(16));
        hBox1.getChildren().addAll(greenTick1, text1);

        HBox hBox2 = new HBox(15);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        ImageView greenTick2 = new ImageView(new Image(thisClass.getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTick2.setFitHeight(20);
        greenTick2.setFitWidth(20);
        Label text2 = new Label("Unlock all Past Questions Years");
        text2.setFont(Font.font(16));
        hBox2.getChildren().addAll(greenTick2, text2);

        HBox hBox3 = new HBox(15);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        ImageView greenTick3 = new ImageView(new Image(thisClass.getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTick3.setFitHeight(20);
        greenTick3.setFitWidth(20);
        Label text3 = new Label("Unlock all Novel Chapters");
        text3.setFont(Font.font(16));
        hBox3.getChildren().addAll(greenTick3, text3);

        HBox hBox4 = new HBox(15);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        ImageView greenTick4 = new ImageView(new Image(thisClass.getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTick4.setFitHeight(20);
        greenTick4.setFitWidth(20);
        Label text4 = new Label("Use App without internet");
        text4.setFont(Font.font(16));
        hBox4.getChildren().addAll(greenTick4, text4);

        HBox hBox5 = new HBox(15);
        hBox5.setAlignment(Pos.CENTER_LEFT);
        ImageView greenTick5 = new ImageView(new Image(thisClass.getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTick5.setFitHeight(20);
        greenTick5.setFitWidth(20);
        Label text5 = new Label("Activation lasts for One Academic Year");
        text5.setFont(Font.font(16));
        hBox5.getChildren().addAll(greenTick5, text5);

        VBox innerVBox = new VBox(15);
        innerVBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, hBox5);

        Button activateNowButton = new Button("Activate Now (N2000)");
        activateNowButton.setPrefSize(200, 45);
        activateNowButton.setTextFill(Paint.valueOf("#FFFFFF"));
        activateNowButton.setStyle("-fx-background-color: #12AF20; -fx-background-radius: 8;");
        activateNowButton.setFont(new Font(16));
        activateNowButton.setOnAction(event -> {
            dialog.close();
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACTIVATE_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

        activateDialog.getChildren().addAll(activateHeaderText, innerVBox, activateNowButton);
        activateDialog.setPadding(new Insets(25, 25, 0, 25));

        ButtonType buttonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);;

        DialogPane dialogPane = new DialogPane();
        dialogPane.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        dialogPane.setMinSize(370, 420);
        dialogPane.getButtonTypes().add(buttonType);
        dialogPane.setContent(activateDialog);
        dialog.setDialogPane(dialogPane);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(thisClass.getResource("/drawable/app_logo.png").toString()));
        return dialog;
    }

}
