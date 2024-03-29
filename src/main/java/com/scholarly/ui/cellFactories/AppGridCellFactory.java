package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.AppItem;
import com.scholarly.ui.listcells.AppGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class AppGridCellFactory implements Callback<GridView<AppItem>, GridCell<AppItem>> {

    @Override
    public GridCell<AppItem> call(GridView<AppItem> appItemGridView) {
        return new AppGridItemCell();
    }
}
