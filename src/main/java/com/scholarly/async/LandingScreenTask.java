package com.scholarly.async;

import com.scholarly.MainApplication;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import javafx.concurrent.Task;
import javafx.scene.Parent;

public class LandingScreenTask extends Task<Parent> {
    private final Class<? extends FxmlView<? extends ViewModel>> controller;

    public LandingScreenTask(Class<? extends FxmlView<? extends ViewModel>> controller) {
        this.controller = controller;
    }

    @Override
    protected Parent call() {
        Parent view = FluentViewLoader.fxmlView(controller).load().getView();
        MainApplication.timeTakenTo("load " + controller.getSimpleName());
        return view;
    }

    @Override
    protected void updateValue(Parent value) {
        super.updateValue(value);
    }
}