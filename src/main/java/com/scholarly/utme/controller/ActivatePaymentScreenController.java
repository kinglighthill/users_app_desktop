package com.scholarly.utme.controller;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.ActivatePaymentScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/ActivatePaymentScreen.fxml")
public class ActivatePaymentScreenController implements FxmlView<ActivatePaymentScreenVM>, Initializable {

    @FXML
    private ScrollPane centerScrollPane;

    @FXML
    private ImageView customerSupportImage, bankImage, zenithPaymentImage, paystackImage, flutterwaveImage, monnifyImage, noAccountImage;

    @FXML
    private VBox bankTransferVBox, bankTransferDetailsPane, paystackVBox, paystackDetailsPane, flutterwaveVBox, flutterwaveDetailsPane, monnifyVBox, monnifyDetailsPane, noAccountVBox, noAccountDetailsPane;

    @FXML
    private Label customerSupportLabel, paymentMethodText;

    @FXML
    private Button backButton;

    @FXML
    private ToggleButton bankTransferDropdown, paystackDropdown, flutterwaveDropdown, monnifyDropdown, noAccountDropdown;


    private ImageView bankTransferOpenDropdownImage, paystackOpenDropdownImage, flutterwaveOpenDropdownImage, monnifyOpenDropdownImage, noAccountOpenDropdownImage;

    private ImageView bankTransferCloseDropdownImage, paystackCloseDropdownImage, flutterwaveCloseDropdownImage, monnifyCloseDropdownImage, noAccountCloseDropdownImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        bankTransferVBox.getChildren().remove(bankTransferDetailsPane);
        paystackVBox.getChildren().remove(paystackDetailsPane);
        flutterwaveVBox.getChildren().remove(flutterwaveDetailsPane);
        monnifyVBox.getChildren().remove(monnifyDetailsPane);
        noAccountVBox.getChildren().remove(noAccountDetailsPane);

        bankTransferDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue){
                bankTransferVBox.getChildren().add(bankTransferDetailsPane);
                bankTransferDropdown.setGraphic(bankTransferOpenDropdownImage);
            }else {
                bankTransferVBox.getChildren().remove(bankTransferDetailsPane);
                bankTransferDropdown.setGraphic(bankTransferCloseDropdownImage);
            }
        }));

        paystackDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue){
                paystackVBox.getChildren().add(paystackDetailsPane);
                paystackDropdown.setGraphic(paystackOpenDropdownImage);
            }else {
                paystackVBox.getChildren().remove(paystackDetailsPane);
                paystackDropdown.setGraphic(paystackCloseDropdownImage);
            }
        }));

        flutterwaveDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue){
                flutterwaveVBox.getChildren().add(flutterwaveDetailsPane);
                flutterwaveDropdown.setGraphic(flutterwaveOpenDropdownImage);
            }else {
                flutterwaveVBox.getChildren().remove(flutterwaveDetailsPane);
                flutterwaveDropdown.setGraphic(flutterwaveCloseDropdownImage);
            }
        }));

        monnifyDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue){
                monnifyVBox.getChildren().add(monnifyDetailsPane);
                monnifyDropdown.setGraphic(monnifyOpenDropdownImage);
            }else {
                monnifyVBox.getChildren().remove(monnifyDetailsPane);
                monnifyDropdown.setGraphic(monnifyCloseDropdownImage);
            }
        }));

        noAccountDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue){
                noAccountVBox.getChildren().add(noAccountDetailsPane);
                noAccountDropdown.setGraphic(noAccountOpenDropdownImage);
            } else {
                noAccountVBox.getChildren().remove(noAccountDetailsPane);
                noAccountDropdown.setGraphic(noAccountCloseDropdownImage);
            }
        }));

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACTIVATE_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        customerSupportImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/customer_support_image.png").toString()));
        bankImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/bank_logo.png").toString()));
        zenithPaymentImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/zenith_option_image.png").toString()));
        paystackImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/paystack_logo.png").toString()));
        flutterwaveImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/flutterwave_logo.png").toString()));
        monnifyImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/monnify_logo.png").toString()));
        noAccountImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/no_account_logo.png").toString()));

        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        bankTransferOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        paystackOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        flutterwaveOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        monnifyOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        noAccountOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));

        bankTransferCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        paystackCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        flutterwaveCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        monnifyCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        noAccountCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));

        bankTransferDropdown.setGraphic(bankTransferCloseDropdownImage);
        paystackDropdown.setGraphic(paystackCloseDropdownImage);
        flutterwaveDropdown.setGraphic(flutterwaveCloseDropdownImage);
        monnifyDropdown.setGraphic(monnifyCloseDropdownImage);
        noAccountDropdown.setGraphic(noAccountCloseDropdownImage);

        backButton.setBackground(Background.EMPTY);
        centerScrollPane.setBackground(Background.EMPTY);
        bankTransferDropdown.setBackground(Background.EMPTY);
        paystackDropdown.setBackground(Background.EMPTY);
        flutterwaveDropdown.setBackground(Background.EMPTY);
        monnifyDropdown.setBackground(Background.EMPTY);
        noAccountDropdown.setBackground(Background.EMPTY);

    }

    private void initializeFonts() {
        customerSupportLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        paymentMethodText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
    }
}
