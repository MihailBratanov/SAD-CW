package com.example.app16.controllers;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app16.R;
import com.example.app16.dbUtils.DatabaseAccessor;
import com.example.app16.utils.GraphDisplay;
import com.example.app16.beans.AnalyseBean;
import com.example.app16.valueObjects.DailyQuoteVO;

import android.content.Context;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Class instantiating analyse_layout.xml. Includes event handlers for this UI
 */
public class AnalyseFragment extends Fragment implements OnClickListener {
    View root;
    Context myContext;
    AnalyseBean analysebean;

    ImageView analyseResult;
    Button analyseOkButton;
    Button analysecancelButton;
    TextView errorTextField;

    public AnalyseFragment() {
    }

    public static AnalyseFragment newInstance(Context c) {
        AnalyseFragment fragment = new AnalyseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.myContext = c;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.analyse_layout, container, false);
        Bundle data = getArguments();
        analyseResult = (ImageView) root.findViewById(R.id.analyseResult);
        analysebean = new AnalyseBean(myContext);
        analyseOkButton = root.findViewById(R.id.analyseOK);
        analyseOkButton.setOnClickListener(this);
        analysecancelButton = root.findViewById(R.id.analyseCancel);
        analysecancelButton.setOnClickListener(this);
        errorTextField = (TextView) root.findViewById(R.id.errorTextField);
        return root;
    }


    public void onClick(View _v) {
        InputMethodManager _imm = (InputMethodManager) myContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        try {
            _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0);
        } catch (Exception _e) {
        }
        if (_v.getId() == R.id.analyseOK) {
            analyseOK(_v);
        } else if (_v.getId() == R.id.analyseCancel) {
            analyseCancel(_v);

        }
    }

    // function to retrieve graph display for last query
    public void analyseOK(View _v) {
        if (analysebean.isanalyseerror()) {
            Log.w(getClass().getName(), analysebean.errors());
            Toast.makeText(myContext, "Errors: " + analysebean.errors(), Toast.LENGTH_LONG).show();
        } else {
            GraphDisplay _result = analysebean.analyse();
            // TODO if GraphDisplay is null instead (meaning there is no data to plot), set errorTextField to an error message
            analyseResult.invalidate();
            analyseResult.refreshDrawableState();
            analyseResult.setImageDrawable(_result);
        }
    }


    public void analyseCancel(View _v) {
        analysebean.resetData();
    }
}
