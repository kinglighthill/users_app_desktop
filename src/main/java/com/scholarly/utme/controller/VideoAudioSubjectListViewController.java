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
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/VideoAudioSubjectListView.fxml")
public class VideoAudioSubjectListViewController implements FxmlView<VideoAudioSubjectListViewVM>, Initializable {

    @FXML
    private ListView<VideoAudioSubjectListItemVM> subjectList;

    @FXML
    private Button continueButton;


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
            ViewSwitcher.showScreen(View.VIDEOS_GRID_SCREEN);
        });
    }
}
