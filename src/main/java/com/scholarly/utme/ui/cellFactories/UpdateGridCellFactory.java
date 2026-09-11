package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.UpdateItem;
import com.scholarly.utme.ui.listcells.UpdateGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class UpdateGridCellFactory implements Callback<GridView<UpdateItem>, GridCell<UpdateItem>> {

    @Override
    public GridCell<UpdateItem> call(GridView<UpdateItem> updateItemGridView) {
        return new UpdateGridItemCell();
    }
}
