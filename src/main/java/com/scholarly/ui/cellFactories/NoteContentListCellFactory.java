package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.newDb.NoteSection;
import com.scholarly.ui.listcells.NoteContentListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NoteContentListCellFactory implements Callback<ListView<NoteSection>, ListCell<NoteSection>> {
    @Override
    public ListCell<NoteSection> call(ListView<NoteSection> param) {
        return new NoteContentListItemCell();
    }
}
