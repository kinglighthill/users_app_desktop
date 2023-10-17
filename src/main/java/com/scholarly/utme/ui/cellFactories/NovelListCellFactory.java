package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelModel;
import com.scholarly.utme.ui.listcells.NovelListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NovelListCellFactory implements Callback<ListView<NovelModel>, ListCell<NovelModel>> {

    @Override
    public ListCell<NovelModel> call(ListView<NovelModel> param) {
        return new NovelListItemCell();
    }
}

