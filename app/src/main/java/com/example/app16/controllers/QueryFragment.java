package com.example.app16.controllers;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.app16.R;
import com.example.app16.beans.QueryBean;
import com.example.app16.dbUtils.DatabaseAccessor;
import com.example.app16.valueObjects.DailyQuoteVO;

import android.content.Context;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Class instantiating findquote_layout.xml. Includes event handlers for this UI
 */

public class QueryFragment extends Fragment implements OnClickListener {
    View root;
    Context myContext;
    QueryBean bean;

    String symbolMessage = "Only letters, numbers, or one of '.-^=' allowed";

    EditText date1TextField;
    EditText date2TextField;
    EditText share1TextField;
    EditText share2TextField;
    TextView date1ErrorField;
    TextView date2ErrorField;
    TextView rangeErrorField;
    TextView share1ErrorField;
    TextView share2ErrorField;
    TextView queryResultField;
    Button queryButton;
    Button cancelButton;

    public QueryFragment() {
    }

    public static QueryFragment newInstance(Context c) {
        QueryFragment fragment = new QueryFragment();
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
        root = inflater.inflate(R.layout.query_layout, container, false);
        Bundle data = getArguments();
        getXmlFields();
        bean = new QueryBean(myContext);    // Create bean for this fragment
        setListeners();
        return root;
    }

    private void getXmlFields() {
        date1TextField = (EditText) root.findViewById(R.id.date1Field);
        date1ErrorField = (TextView) root.findViewById(R.id.date1Error);
        date2TextField = (EditText) root.findViewById(R.id.date2Field);
        date2ErrorField = (TextView) root.findViewById(R.id.date2Error);
        rangeErrorField = (TextView) root.findViewById(R.id.rangeError);
        share1TextField = (EditText) root.findViewById(R.id.share1Field);
        share1ErrorField = (TextView) root.findViewById(R.id.share1Error);
        share2TextField = (EditText) root.findViewById(R.id.share2Field);
        share2ErrorField = (TextView) root.findViewById(R.id.share2Error);
        queryResultField = (TextView) root.findViewById(R.id.queryResult);
    }

    private void setListeners() {
        // Set listeners for buttons
        queryButton = root.findViewById(R.id.queryBtn);
        queryButton.setOnClickListener(this);
        cancelButton = root.findViewById(R.id.cancelBtn);
        cancelButton.setOnClickListener(this);
    }

    /**
     * Main event handler
     *
     * @param _v: View
     */
    public void onClick(View _v) {
        InputMethodManager _imm = (InputMethodManager) myContext.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        try {
            _imm.hideSoftInputFromWindow(_v.getWindowToken(), 0);
        } catch (Exception _e) {
        }
        if (_v.getId() == R.id.queryBtn) {
            query(_v);
        } else if (_v.getId() == R.id.cancelBtn) {
            cancel(_v);
        }
    }

    /**
     * Validates inputs and tells bean to make query.
     * Displays result of query.
     *
     * @param _v
     */
    public void query(View _v) {
        bean.resetData();
        resetErrorFields();
        validateInput();
        // execute query and display query result
        String result = bean.query();
        queryResultField.setText(result);
    }

    private void validateInput() {
        validateDate1();
        validateDate2();
        validateRange();
        validateShare1();
        validateShare2();
    }

    private void resetErrorFields() {
        date1ErrorField.setText("");
        date2ErrorField.setText("");
        rangeErrorField.setText("");
        share1ErrorField.setText("");
        share2ErrorField.setText("");
    }

    private void validateDate1() {
        String startDate = date1TextField.getText() + "";
        boolean success = bean.setDate1(startDate);
        if (!bean.ensureNonEmpty(startDate)) {
            date1ErrorField.setText("Please enter a date");
        } else if (!success) {
            date1ErrorField.setText("Please use the format 'yyyy-mm-dd'");
        }
    }

    private void validateDate2() {
        String endDate = date2TextField.getText() + "";
        boolean success = bean.setDate2(endDate);
        if (!bean.ensureNonEmpty(endDate)) {
            date2ErrorField.setText("Please enter a date");
        } else if (!success) {
            date2ErrorField.setText("Please use the format 'yyyy-mm-dd'");
        }
    }

    private void validateRange() {
        boolean date2Greater = bean.validateOrder();
        boolean rangeValid = bean.validateRange();
        if (!date2Greater) {
            rangeErrorField.setText("End date should be later than start date");
        } else if (!rangeValid) {
            rangeErrorField.setText("Invalid date range, please enter up to 2 years");
        }
    }

    private void validateShare1() {
        String shareSymbol1 = share1TextField.getText() + "";
        boolean success = bean.setShareSymbol1(shareSymbol1);
        if (!bean.ensureNonEmpty(shareSymbol1)) {
            share1ErrorField.setText("Please enter a ticker symbol");
        } else if (!success) {
            share1ErrorField.setText(symbolMessage);
        }
    }

    private void validateShare2() {
        String shareSymbol2 = share2TextField.getText() + "";
        boolean success = bean.setShareSymbol2(shareSymbol2);
        if (!success) {
            share2ErrorField.setText(symbolMessage);
        }
    }

    /**
     * Resets all data in bean and view elements.
     *
     * @param _v
     */
    public void cancel(View _v) {
        bean.resetData();
        resetErrorFields();
        date1TextField.setText("");
        date2TextField.setText("");
        share1TextField.setText("");
        share2TextField.setText("");
        queryResultField.setText("");
    }
}
