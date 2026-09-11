package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.listItems.UnorderedListItem;
import com.scholarly.utme.ui.listcells.UnorderedListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class UnorderedListCellFactory implements Callback<ListView<UnorderedListItem>, ListCell<UnorderedListItem>> {

    private final String subjectColor;
    public UnorderedListCellFactory(String subjectColor) {
        this.subjectColor = subjectColor;
    }

    @Override
    public ListCell<UnorderedListItem> call(ListView<UnorderedListItem> param) {
        return new UnorderedListItemCell(subjectColor);
    }
}
