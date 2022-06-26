package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.FontUtil;
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
    private ImageView customerSupportImage, bankImage, zenithPaymentImage, paystackImage;

    @FXML
    private VBox bankTransferVBox, bankTransferDetailsPane;

    @FXML
    private Label customerSupportLabel, paymentMethodText;

    @FXML
    private Button backButton;

    @FXML
    private ToggleButton dropdownButton, dropdownButton2;

    private ImageView openDropdownImage, closeDropdownImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        dropdownButton.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue){
                bankTransferVBox.getChildren().remove(bankTransferDetailsPane);
                dropdownButton.setGraphic(closeDropdownImage);
            }else {
                bankTransferVBox.getChildren().add(bankTransferDetailsPane);
                dropdownButton.setGraphic(openDropdownImage);
            }
        }));

    }

    private void initializeViews() {
        customerSupportImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/customer_support_image.png").toString()));
        bankImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/bank_image.png").toString()));
        zenithPaymentImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/zenith_option_image.png").toString()));
        paystackImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/paystack_logo.png").toString()));

        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));

        openDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        closeDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        dropdownButton.setGraphic(openDropdownImage);
        dropdownButton2.setGraphic(openDropdownImage);

        backButton.setBackground(Background.EMPTY);
        dropdownButton.setBackground(Background.EMPTY);
        dropdownButton2.setBackground(Background.EMPTY);
        centerScrollPane.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        customerSupportLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        paymentMethodText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
    }
}
