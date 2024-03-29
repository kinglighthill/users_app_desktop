package com.scholarly.ui.cellFactories;

import com.scholarly.data.model.listItems.NewsItem;
import com.scholarly.ui.listcells.NewsListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


public class NewsListCellFactory implements Callback<ListView<NewsItem>, ListCell<NewsItem>> {

    @Override
    public ListCell<NewsItem> call(ListView<NewsItem> param) {
        return new NewsListItemCell();
    }
}
