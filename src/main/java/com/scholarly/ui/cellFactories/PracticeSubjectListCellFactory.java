package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.newDb.PQSubject;
import com.scholarly.ui.listcells.PracticeSubjectListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PracticeSubjectListCellFactory implements Callback<ListView<PQSubject>, ListCell<PQSubject>> {

    @Override
    public ListCell<PQSubject> call(ListView<PQSubject> param) {
        return new PracticeSubjectListItemCell();
    }
}
