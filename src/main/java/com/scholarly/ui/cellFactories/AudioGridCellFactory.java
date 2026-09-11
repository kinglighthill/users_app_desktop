package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.AudioItem;
import com.scholarly.ui.listcells.AudioGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class AudioGridCellFactory implements Callback<GridView<AudioItem>, GridCell<AudioItem>> {

    @Override
    public GridCell<AudioItem> call(GridView<AudioItem> videoItemGridView) {
        return new AudioGridItemCell();
    }
}
