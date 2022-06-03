package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.ui.listcells.NovelGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class NovelGridCellFactory implements Callback<GridView<Novel>, GridCell<Novel>> {

    @Override
    public GridCell<Novel> call(GridView<Novel> param) {
        return new NovelGridItemCell();
    }
}
