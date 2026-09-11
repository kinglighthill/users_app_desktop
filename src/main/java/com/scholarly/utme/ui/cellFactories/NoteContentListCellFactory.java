package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.newDb.NoteSection;
import com.scholarly.utme.ui.listcells.NoteContentListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NoteContentListCellFactory implements Callback<ListView<NoteSection>, ListCell<NoteSection>> {
    @Override
    public ListCell<NoteSection> call(ListView<NoteSection> param) {
        return new NoteContentListItemCell();
    }
}
