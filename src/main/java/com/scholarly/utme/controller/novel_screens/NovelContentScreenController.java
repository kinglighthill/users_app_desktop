package com.scholarly.utme.controller.novel_screens;

import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.ui.cellFactories.NovelChapterListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.novel_screens.NovelChapterListVM.NovelState;
import com.scholarly.utme.viewmodels.novel_screens.NovelContentScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/novel_screens/NovelContentScreen.fxml")
public class NovelContentScreenController implements FxmlView<NovelContentScreenVM>, Initializable {

    @FXML
    private ScrollPane contentPane;

    @FXML
    private Button backButton, prevButton, nextButton;

    @FXML
    private Label pageTitle, chapterIndex, chapterTitle, chapterContent, chapterCount;

    @FXML
    private HBox chapterHeader;

    @FXML
    private ListView<NovelChapter> chaptersList;

    @InjectViewModel
    private NovelContentScreenVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFont();

        viewModel.processInitialData(getInitialData());

        chaptersList.setCellFactory(new NovelChapterListCellFactory());
        chaptersList.setItems(viewModel.getChapters());
        chaptersList.getSelectionModel().select(viewModel.getSelectedChapter());

        chaptersList.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            viewModel.setSelectedChapter(newValue);
        }));

        viewModel.selectedChapterProperty().addListener(((observableValue, oldValue, newValue) -> {
            chapterContent.setText(newValue.getDetails());
            chapterTitle.setText(newValue.getTitle());
            if (!chapterHeader.getChildren().contains(chapterIndex)) {
                chapterHeader.getChildren().add(0, chapterIndex);
            }
            if (newValue.getPosition() != -1) {
                chapterCount.setText(newValue.getPosition() + " of " + chaptersList.getItems().size());
                chapterIndex.setText("Chapter " + newValue.getPosition() + ":");
            }else {
                chapterCount.setText(chaptersList.getSelectionModel().getSelectedIndex() + 1 + " of " + chaptersList.getItems().size());
                chapterHeader.getChildren().remove(chapterIndex);
            }
        }));


        pageTitle.setText(viewModel.getNovel().getName());
        if (viewModel.getSelectedChapter().getPosition() == -1) {
            chapterHeader.getChildren().remove(chapterIndex);
            chapterCount.setText(chaptersList.getSelectionModel().getSelectedIndex() + 1 + " of " + chaptersList.getItems().size());
        }else {
            chapterCount.setText(viewModel.getSelectedChapter().getPosition() + " of " + chaptersList.getItems().size());
        }
        chapterIndex.setText("Chapter " + viewModel.getSelectedChapter().getPosition() + ":");
        chapterTitle.setText(viewModel.getSelectedChapter().getTitle());
        chapterContent.setText(viewModel.getSelectedChapter().getDetails());


        prevButton.disableProperty().bind(Bindings.equal(0, chaptersList.getSelectionModel().selectedIndexProperty()));
        nextButton.disableProperty().bind(Bindings.equal(chaptersList.getSelectionModel().selectedIndexProperty(), chaptersList.getItems().size()-1));

        nextButton.setOnAction(event -> {
            int selectedIndex = chaptersList.getSelectionModel().getSelectedIndex();
            chaptersList.getSelectionModel().select(selectedIndex + 1);
            viewModel.setSelectedChapter(chaptersList.getSelectionModel().getSelectedItem());
        });

        prevButton.setOnAction(event -> {
            int selectedIndex = chaptersList.getSelectionModel().getSelectedIndex();
            chaptersList.getSelectionModel().select(selectedIndex - 1);
            viewModel.setSelectedChapter(chaptersList.getSelectionModel().getSelectedItem());
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(viewModel.getNovel());
            ViewSwitcher.showScreen(View.NOVEL_CHAPTER_LIST_SCREEN);
        });

    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);
        ImageView prevIcon = new ImageView(new Image(getClass().getResource("/drawable/novel_images/prev_icon.png").toString()));
        prevButton.setGraphic(prevIcon);
        ImageView nextIcon = new ImageView(new Image(getClass().getResource("/drawable/novel_images/next_icon.png").toString()));
        nextButton.setGraphic(nextIcon);

        contentPane.setBackground(Background.EMPTY);
        chapterContent.setBackground(Background.EMPTY);
        chaptersList.setBackground(Background.EMPTY);
    }

    private void initializeFont() {
        chapterIndex.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));
        chapterTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));
        chapterContent.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
        chapterCount.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
    }

    private NovelState getInitialData() {
        NovelState novelState = (NovelState) ViewSwitcher.retrieveData();
        return novelState;
    }


}
