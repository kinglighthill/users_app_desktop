package com.scholarly.utme.controller;

import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.viewmodels.ActivatePaymentScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static com.scholarly.utme.util.Constants.PAYSTACK_URL;
import static com.scholarly.utme.util.Constants.WHATSAPP_URL;

@FxmlPath("/layouts/ActivatePaymentScreen.fxml")
public class ActivatePaymentScreenController implements FxmlView<ActivatePaymentScreenVM>, Initializable {
    public static final String TAG = "ActivatePaymentScreenController: ";

    @InjectViewModel
    private ActivatePaymentScreenVM viewModel;

    @FXML
    private ScrollPane centerScrollPane;

    @FXML
    private StackPane paymentImagePane;

    @FXML
    private Panel bankTransferPanel, paystackPanel, noAccountPanel;

    @FXML
    private ImageView customerSupportImage, bankImage, zenithPaymentImage, accessPaymentImage, ubaPaymentImage, paystackImage, flutterwaveImage, monnifyImage, noAccountImage;

    @FXML
    private VBox bankTransferVBox, bankTransferDetailsPane, paystackVBox, paystackDetailsPane, flutterwaveVBox, flutterwaveDetailsPane, monnifyVBox, monnifyDetailsPane, noAccountVBox, noAccountDetailsPane;

    @FXML
    private HBox currentPositionHBox;

    @FXML
    private Label customerSupportLabel, paymentMethodText, chatUsLabel;

    @FXML
    private Button backButton, whatsAppButton, paystackButton;

    @FXML
    private ToggleButton bankTransferDropdown, paystackDropdown, flutterwaveDropdown, monnifyDropdown, noAccountDropdown;


    private ImageView bankTransferOpenDropdownImage, paystackOpenDropdownImage, flutterwaveOpenDropdownImage, monnifyOpenDropdownImage, noAccountOpenDropdownImage;

    private ImageView bankTransferCloseDropdownImage, paystackCloseDropdownImage, flutterwaveCloseDropdownImage, monnifyCloseDropdownImage, noAccountCloseDropdownImage;


    MainApplication application = new MainApplication();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(bankTransferDropdown, paystackDropdown, noAccountDropdown);

        initializeViews();
        initializeFonts();

        AtomicInteger index = new AtomicInteger(1);

        paymentImagePane.setOnDragDetected(event -> {
            currentPositionHBox.getChildren().forEach(child -> {
                Line line = (Line) child;
                line.setStroke(Paint.valueOf("#DFDFDF"));
            });

            if (index.get() == 0) {
                Line line = (Line) currentPositionHBox.getChildren().get(index.get());
                line.setStroke(Paint.valueOf("#12AF20"));
                Animations.slideOut(zenithPaymentImage, 0f, -50f, 200);
                Animations.slideIn(ubaPaymentImage, 50f, 0f, 200);
                index.incrementAndGet();
            } else if (index.get() == 1) {
                Line line = (Line) currentPositionHBox.getChildren().get(index.get());
                line.setStroke(Paint.valueOf("#12AF20"));
                Animations.slideOut(ubaPaymentImage, 0f, -50f, 200);
                Animations.slideIn(accessPaymentImage, 50f, 0f, 200);
                index.incrementAndGet();
            } else if (index.get() == 2) {
                Line line = (Line) currentPositionHBox.getChildren().get(index.get());
                line.setStroke(Paint.valueOf("#12AF20"));
                Animations.slideOut(accessPaymentImage, 0f, -50f, 200);
                Animations.slideIn(zenithPaymentImage, 50f, 0f, 200);
                index.set(0);
            }

        });



        bankTransferVBox.getChildren().remove(bankTransferDetailsPane);
        paystackVBox.getChildren().remove(paystackDetailsPane);
//        flutterwaveVBox.getChildren().remove(flutterwaveDetailsPane);
//        monnifyVBox.getChildren().remove(monnifyDetailsPane);
        noAccountVBox.getChildren().remove(noAccountDetailsPane);

        bankTransferPanel.setOnMouseClicked(event -> {
            bankTransferDropdown.setSelected(!bankTransferDropdown.isSelected());
        });

        paystackPanel.setOnMouseClicked(event -> {
            paystackDropdown.setSelected(!paystackDropdown.isSelected());
        });

        noAccountPanel.setOnMouseClicked(event -> {
            noAccountDropdown.setSelected(!noAccountDropdown.isSelected());
        });

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

//        flutterwaveDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
//            if (newValue){
//                flutterwaveVBox.getChildren().add(flutterwaveDetailsPane);
//                flutterwaveDropdown.setGraphic(flutterwaveOpenDropdownImage);
//            }else {
//                flutterwaveVBox.getChildren().remove(flutterwaveDetailsPane);
//                flutterwaveDropdown.setGraphic(flutterwaveCloseDropdownImage);
//            }
//        }));
//
//        monnifyDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
//            if (newValue){
//                monnifyVBox.getChildren().add(monnifyDetailsPane);
//                monnifyDropdown.setGraphic(monnifyOpenDropdownImage);
//            }else {
//                monnifyVBox.getChildren().remove(monnifyDetailsPane);
//                monnifyDropdown.setGraphic(monnifyCloseDropdownImage);
//            }
//        }));

        noAccountDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue){
                noAccountVBox.getChildren().add(noAccountDetailsPane);
                noAccountDropdown.setGraphic(noAccountOpenDropdownImage);
            } else {
                noAccountVBox.getChildren().remove(noAccountDetailsPane);
                noAccountDropdown.setGraphic(noAccountCloseDropdownImage);
            }
        }));

        whatsAppButton.setOnMouseClicked(event -> {
            application.openBrowser(WHATSAPP_URL);
        });

        paystackButton.setOnAction(event -> {
            try {
                UserData user = viewModel.getUser();
                String[] names = user.getFullName().split(" ");
                String firstName = names[0];
                String lastName = "";
                if (names.length > 1) {
                    lastName = names[names.length - 1];
                }
                String paystackUrl = PAYSTACK_URL + "?first_name=" + firstName + "&last_name=" + lastName + "&email=" + user.getEmail() + "&phone=" + user.getPhoneNumber();
                application.openBrowser(paystackUrl);
            } catch (Exception exception) {
                application.openBrowser(PAYSTACK_URL);
            }
        });

        chatUsLabel.setOnMouseClicked(event -> {
            application.openBrowser(WHATSAPP_URL);
        });

        chatUsLabel.setOnMouseEntered(event -> chatUsLabel.setUnderline(true));
        chatUsLabel.setOnMouseExited(event -> chatUsLabel.setUnderline(false));

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACTIVATE_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        customerSupportImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/customer_support_image.png").toString()));
        bankImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/bank_logo.png").toString()));
        paystackImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/paystack_logo.png").toString()));
//        flutterwaveImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/flutterwave_logo.png").toString()));
//        monnifyImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/monnify_logo.png").toString()));
        noAccountImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/no_account_logo.png").toString()));

        zenithPaymentImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/zenith_account_image.png").toString()));
        accessPaymentImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/access_account_image.png").toString()));
        ubaPaymentImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/uba_account_image.png").toString()));

        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        bankTransferOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        paystackOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
//        flutterwaveOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
//        monnifyOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        noAccountOpenDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));

        bankTransferCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        paystackCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
//        flutterwaveCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
//        monnifyCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        noAccountCloseDropdownImage = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));

        bankTransferDropdown.setGraphic(bankTransferCloseDropdownImage);
        paystackDropdown.setGraphic(paystackCloseDropdownImage);
//        flutterwaveDropdown.setGraphic(flutterwaveCloseDropdownImage);
//        monnifyDropdown.setGraphic(monnifyCloseDropdownImage);
        noAccountDropdown.setGraphic(noAccountCloseDropdownImage);

        backButton.setBackground(Background.EMPTY);
        centerScrollPane.setBackground(Background.EMPTY);
        bankTransferDropdown.setBackground(Background.EMPTY);
        paystackDropdown.setBackground(Background.EMPTY);
//        flutterwaveDropdown.setBackground(Background.EMPTY);
//        monnifyDropdown.setBackground(Background.EMPTY);
        noAccountDropdown.setBackground(Background.EMPTY);

    }

    private void initializeFonts() {
        customerSupportLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        paymentMethodText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
    }
}
