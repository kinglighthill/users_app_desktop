package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.NoSelectionModel;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.VideoAudioSubjectListItemVM;
import com.scholarly.utme.viewmodels.VideoAudioSubjectListViewVM;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import de.saxsys.mvvmfx.utils.viewlist.ViewListCellFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/VideoAudioSubjectListView.fxml")
public class VideoAudioSubjectListViewController implements FxmlView<VideoAudioSubjectListViewVM>, Initializable {

    @FXML
    private ListView<VideoAudioSubjectListItemVM> subjectList;

    @FXML
    private Button continueButton;

    @FXML
    private Label headerText;


    @InjectViewModel
    private VideoAudioSubjectListViewVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        subjectList.setSelectionModel(new NoSelectionModel<>());

        subjectList.setItems(viewModel.getSubjects());

        ViewListCellFactory<VideoAudioSubjectListItemVM> videoAudioListCellFactory = CachedViewModelCellFactory.create(vm -> {
            return FluentViewLoader.fxmlView(VideoAudioSubjectListItemController.class).viewModel(vm).load();
        });

        subjectList.setCellFactory(videoAudioListCellFactory);
        subjectList.setFocusTraversable(false);


        continueButton.setOnAction(event -> {
            if (viewModel.getType() == VideoAudioSubjectListViewVM.Type.VIDEO) {
                ViewSwitcher.showScreen(View.VIDEOS_GRID_SCREEN);
            }
            if (viewModel.getType() == VideoAudioSubjectListViewVM.Type.AUDIO) {
                ViewSwitcher.showScreen(View.AUDIOS_GRID_SCREEN);
            }

        });
    }

    public void setHeaderText(String text) {
        headerText.setText(text);
    }

    public void setType(VideoAudioSubjectListViewVM.Type type) {
        viewModel.setType(type);

        if (type == VideoAudioSubjectListViewVM.Type.VIDEO) {
            headerText.setText("Choose videos by subject");
        }else if (type == VideoAudioSubjectListViewVM.Type.AUDIO) {
            headerText.setText("Choose videos by subject");
        }
    }
}
