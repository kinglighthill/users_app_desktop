package com.scholarly.controller.settings_screens;

import com.scholarly.MainApplication;
import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.ui.utils.Screens;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
import com.scholarly.viewmodels.settings_screens.SettingsAppInfoScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/settings_screens/SettingsAppInfoScreen.fxml")
public class SettingsAppInfoScreenController implements FxmlView<SettingsAppInfoScreenVM>, Initializable {

    @FXML
    private Panel privacyPanel, termsPanel, thirdPartyPanel;
    @FXML
    private VBox thirdPartyDetails, thirdPartyVBox;
    @FXML
    private Button backButton;
    @FXML
    private ToggleButton privacyDropdown, termsDropdown, thirdPartyDropdown;
    @FXML
    private ImageView privacyImage, termsImage, thirdPartyImage;
    @FXML
    private Label rxJavaText, mvvmFxText, gsonText, okHttpText, controlsFxText, bootstrapFxText;


    private ImageView privacyOpenDropdownIcon, termsOpenDropdownIcon, thirdPartyOpenDropdownIcon;
    private ImageView privacyCloseDropdownIcon, termsCloseDropdownIcon, thirdPartyCloseDropdownIcon;

    MainApplication application = new MainApplication();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();
        initializeText();

        thirdPartyPanel.setOnMouseClicked(event -> {
            thirdPartyDropdown.setSelected(!thirdPartyDropdown.isSelected());
        });

        thirdPartyVBox.getChildren().remove(thirdPartyDetails);

        thirdPartyDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                thirdPartyVBox.getChildren().add(thirdPartyDetails);
                thirdPartyDropdown.setGraphic(thirdPartyCloseDropdownIcon);
            } else {
                thirdPartyVBox.getChildren().remove(thirdPartyDetails);
                thirdPartyDropdown.setGraphic(thirdPartyOpenDropdownIcon);
            }
        }));

        privacyPanel.setOnMouseClicked(event -> {
            String privacyUrl = "https://scholarly.africa/privacy";
            application.openBrowser(privacyUrl);
        });

        termsPanel.setOnMouseClicked(event -> {
            String termsUrl = "https://scholarly.africa/terms";
            application.openBrowser(termsUrl);
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.SETTINGS_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        privacyImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/info_icon.png").toString()));
        termsImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/info_icon.png").toString()));
        thirdPartyImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/info_icon.png").toString()));

        privacyOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        termsOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        thirdPartyOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));

        privacyCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        termsCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        thirdPartyCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));

        privacyDropdown.setGraphic(privacyOpenDropdownIcon);
        termsDropdown.setGraphic(termsOpenDropdownIcon);
        thirdPartyDropdown.setGraphic(thirdPartyOpenDropdownIcon);


        backButton.setBackground(Background.EMPTY);
        privacyDropdown.setBackground(Background.EMPTY);
        termsDropdown.setBackground(Background.EMPTY);
        thirdPartyDropdown.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }

    private void initializeText() {
        bootstrapFxText.setText("Copyright (c) 2015-2016 Andres Almiray." + System.lineSeparator() + System.lineSeparator() +
                "Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights " +
                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: " + System.lineSeparator() + System.lineSeparator() +
                "The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. " + System.lineSeparator() + System.lineSeparator() +
                "THE SOFTWARE IS PROVIDED \"AS IS,\" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE " +
                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE."
        );

        controlsFxText.setText("Copyright (c) 2013, ControlsFX. All rights reserved." + System.lineSeparator() + System.lineSeparator() +
                "Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: " + System.lineSeparator() + System.lineSeparator() +
                "1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. " + System.lineSeparator() +
                "2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. " + System.lineSeparator() +
                "3. Neither the name of ControlsFX, any associated website, nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. " + System.lineSeparator() + System.lineSeparator() +
                "THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE " +
                "FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT " +
                "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE."
        );

        mvvmFxText.setText("Copyright 2015 Alexander Casall, Manuel Mauky." + System.lineSeparator() + System.lineSeparator() +
                "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0" + System.lineSeparator() + System.lineSeparator() +
                "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License."
        );

        rxJavaText.setText("Copyright (c) 2016-present, RxJava Contributors." + System.lineSeparator() + System.lineSeparator() +
                "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0" + System.lineSeparator() + System.lineSeparator() +
                "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License."
        );

        gsonText.setText("Gson is licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 " + System.lineSeparator() + System.lineSeparator() +
                "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License."
        );

        okHttpText.setText("OKHttp is licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 " + System.lineSeparator() + System.lineSeparator() +
                "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License."
        );
    }
}
