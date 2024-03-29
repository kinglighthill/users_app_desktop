package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.novels.NovelModel;
import com.scholarly.ui.listcells.NovelGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class NovelGridCellFactory implements Callback<GridView<NovelModel>, GridCell<NovelModel>> {

    @Override
    public GridCell<NovelModel> call(GridView<NovelModel> param) {
        return new NovelGridItemCell();
    }
}
