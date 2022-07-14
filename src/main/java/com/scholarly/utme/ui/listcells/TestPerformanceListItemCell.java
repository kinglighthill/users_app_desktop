package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.TestPerformanceItem;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class TestPerformanceListItemCell extends ListCell<TestPerformanceItem> {

    public Label testTitle;
    public Label testDate;
    public Label testResult;
    public Label testSubjects;


    public TestPerformanceListItemCell() {
        loadFxml();
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/test_performance_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(TestPerformanceItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            initializeFonts();
//            testTitle.setText(item.getTitle());
//            testDate.setText(item.getDate());
            testResult.setText(item.getResult() + "%");
//            testSubjects.setText(item.getSubjects());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private void initializeFonts() {
        testTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.FOURTEEN.size));
        testDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        testResult.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));
        testSubjects.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }
}
