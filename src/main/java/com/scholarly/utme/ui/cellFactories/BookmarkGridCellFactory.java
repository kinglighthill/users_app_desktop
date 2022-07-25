package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.BookmarkItem;
import com.scholarly.utme.ui.listcells.BookmarkGridItemCell;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class BookmarkGridCellFactory implements Callback<GridView<BookmarkItem>, GridCell<BookmarkItem>> {

    @Override
    public GridCell<BookmarkItem> call(GridView<BookmarkItem> bookmarkItemGridView) {
        return new BookmarkGridItemCell();
    }
}
