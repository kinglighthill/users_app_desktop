package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.SyllabusTopic;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.SyllabusScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/SyllabusScreen.fxml")
public class SyllabusScreenController implements FxmlView<SyllabusScreenVM>, Initializable {

    @InjectViewModel
    private SyllabusScreenVM viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InitialData data = getInitialData();
        System.out.println("Got data with subject -> " + data.subject.getSubjectName() + " and topic -> " + data.selectedSyllabusTopic.getTitle());
        //viewModel.processInitialData(getInitialData());
    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        return data;
    }


    public static class InitialData {
        private Subject subject;
        private SyllabusTopic selectedSyllabusTopic;

        public InitialData(Subject subject, SyllabusTopic selectedSyllabusTopic) {
            this.subject = subject;
            this.selectedSyllabusTopic = selectedSyllabusTopic;
        }

    }
}
