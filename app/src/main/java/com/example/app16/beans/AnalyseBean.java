package com.example.app16.beans;

import java.util.ArrayList;

import java.util.List;

import android.content.Context;

import com.example.app16.utils.GraphDisplay;
import com.example.app16.ui.main.ModelFacade;

/**
 * Class which validates analyseFragment inputs.
 * If there are no errors, it allows communication with ModelFacade. Otherwise returns errors.
 */
public class AnalyseBean {
    ModelFacade model = null;

    private List errors = new ArrayList();

    public AnalyseBean(Context _c) {
        model = ModelFacade.getInstance(_c);
    }

    public void resetData() {
    }

    public boolean isanalyseerror() {
        errors.clear();
        return errors.size() > 0;
    }

    public String errors() {
        return errors.toString();
    }

    public GraphDisplay analyse() {
        return model.analyse();
    }

}

