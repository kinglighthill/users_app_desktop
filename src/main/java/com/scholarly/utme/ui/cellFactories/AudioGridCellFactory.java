package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.AudioItem;
import com.scholarly.utme.data.model.listItems.VideoItem;
import com.scholarly.utme.ui.listcells.AudioGridItemCell;
import com.scholarly.utme.ui.listcells.VideoGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class AudioGridCellFactory implements Callback<GridView<AudioItem>, GridCell<AudioItem>> {

    @Override
    public GridCell<AudioItem> call(GridView<AudioItem> videoItemGridView) {
        return new AudioGridItemCell();
    }
}
