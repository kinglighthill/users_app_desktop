package com.scholarly.utme.controller.audio_video_screens;

import com.scholarly.utme.ui.utils.NoSelectionModel;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.audio_video_screens.AudioVideoSubjectListItemVM;
import com.scholarly.utme.viewmodels.audio_video_screens.AudioVideoSubjectListViewVM;
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

@FxmlPath("/layouts/audio_video_screens/AudioVideoSubjectListView.fxml")
public class AudioVideoSubjectListViewController implements FxmlView<AudioVideoSubjectListViewVM>, Initializable {

    @FXML
    private ListView<AudioVideoSubjectListItemVM> subjectList;

    @FXML
    private Button continueButton;

    @FXML
    private Label headerText;


    @InjectViewModel
    private AudioVideoSubjectListViewVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        subjectList.setSelectionModel(new NoSelectionModel<>());

        subjectList.setItems(viewModel.getSubjects());

        ViewListCellFactory<AudioVideoSubjectListItemVM> videoAudioListCellFactory = CachedViewModelCellFactory.create(vm -> {
            return FluentViewLoader.fxmlView(AudioVideoSubjectListItemController.class).viewModel(vm).load();
        });

        subjectList.setCellFactory(videoAudioListCellFactory);
        subjectList.setFocusTraversable(false);


        continueButton.setOnAction(event -> {
            if (viewModel.getType() == AudioVideoSubjectListViewVM.Type.VIDEO) {
                ViewSwitcher.showScreen(View.VIDEOS_GRID_SCREEN);
            }
            if (viewModel.getType() == AudioVideoSubjectListViewVM.Type.AUDIO) {
                ViewSwitcher.showScreen(View.AUDIOS_GRID_SCREEN);
            }

        });
    }

    public void setHeaderText(String text) {
        headerText.setText(text);
    }

    public void setType(AudioVideoSubjectListViewVM.Type type) {
        viewModel.setType(type);

        if (type == AudioVideoSubjectListViewVM.Type.VIDEO) {
            headerText.setText("Choose videos by subject");
        }else if (type == AudioVideoSubjectListViewVM.Type.AUDIO) {
            headerText.setText("Choose videos by subject");
        }
    }
}
