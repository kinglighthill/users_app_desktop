package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.novelsDb.Novel;
import com.scholarly.utme.ui.listcells.NovelListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NovelListCellFactory implements Callback<ListView<Novel>, ListCell<Novel>> {


    @Override
    public ListCell<Novel> call(ListView<Novel> param) {
        return new NovelListItemCell();
    }
}

