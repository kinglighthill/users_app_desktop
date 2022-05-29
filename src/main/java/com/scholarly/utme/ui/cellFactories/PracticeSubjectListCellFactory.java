package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.ui.listcells.PracticeSubjectListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PracticeSubjectListCellFactory implements Callback<ListView<Subject>, ListCell<Subject>> {

    @Override
    public ListCell<Subject> call(ListView<Subject> param) {
        return new PracticeSubjectListItemCell();
    }
}
