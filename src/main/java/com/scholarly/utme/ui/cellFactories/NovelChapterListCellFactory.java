package com.scholarly.utme.ui.cellFactories;

import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.ui.listcells.NovelChapterListItemCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class NovelChapterListCellFactory implements Callback<ListView<NovelChapter>, ListCell<NovelChapter>> {

    @Override
    public ListCell<NovelChapter> call(ListView<NovelChapter> novelChapterListView) {
        return new NovelChapterListItemCell();
    }
}
