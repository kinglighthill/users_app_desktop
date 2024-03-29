package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.AudioItem;
import com.scholarly.ui.listcells.AudioListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class AudioListCellFactory implements Callback<ListView<AudioItem>, ListCell<AudioItem>> {

    @Override
    public ListCell<AudioItem> call(ListView<AudioItem> audioItemListView) {
        return new AudioListItemCell();
    }
}
