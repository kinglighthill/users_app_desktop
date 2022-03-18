package com.scholarly.utme.controller.landing_screen;

import com.scholarly.utme.viewmodels.landing_screen.LandingScreenAccountVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen/landing_screen_account.fxml")
public class LandingScreenAccountController implements FxmlView<LandingScreenAccountVM>, Initializable {

    @InjectViewModel
    private LandingScreenAccountVM viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
