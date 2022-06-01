package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.novelsDb.Novel;
import com.scholarly.utme.ui.cellFactories.NovelListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.viewmodels.NovelListViewVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/NovelListView.fxml")
public class NovelListViewController implements FxmlView<NovelListViewVM>, Initializable {

    @FXML
    private Label infoText, proseText;

    @FXML
    private RadioButton dontShowButton;

    @FXML
    private Button dismissButton, viewAllButton;

    @FXML
    private VBox centerBox;

    @FXML
    private ListView<Novel> jambNovelsList;

    @InjectViewModel
    private NovelListViewVM viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFont();

        Novel novel = new Novel();
        novel.setName("Sweet sixteen");
        Novel novel1 = new Novel();
        novel1.setName("Life Changer");

        ObservableList<Novel> novelsList = FXCollections.observableArrayList();
        novelsList.add(novel);
        novelsList.add(novel1);

        jambNovelsList.setCellFactory(new NovelListCellFactory());
        jambNovelsList.setItems(viewModel.getNovels());

        dismissButton.setOnAction(event -> {
            centerBox.getChildren().get(0).setVisible(false);
        });

    }

    private void initializeViews() {
        jambNovelsList.setBackground(Background.EMPTY);
        viewAllButton.setBackground(Background.EMPTY);
    }

    private void initializeFont() {
        infoText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        proseText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));

        dontShowButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        dismissButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        viewAllButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
    }
}
