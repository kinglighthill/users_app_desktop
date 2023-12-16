package com.scholarly.utme.util;

import com.scholarly.utme.MainApplication;
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
}
