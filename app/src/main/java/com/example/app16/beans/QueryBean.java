package com.example.app16.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.example.app16.ui.main.ModelFacade;
import com.example.app16.utils.DateComponent;
import com.example.app16.valueObjects.QueryVO;

/**
 * Class which validates findQuoteFragment inputs.
 * If there are no errors, it allows communication with ModelFacade. Otherwise returns errors.
 */
public class QueryBean {
    ModelFacade model = null;

    private long MAX_SECONDS_RANGE = 63072000;
    private String SYMBOL_REGEX = "[A-Za-z0-9.-^=]*";
    private String DATE_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
    private String startDate = "";
    private String endDate = "";
    private String shareSymbol1 = "";
    private String shareSymbol2 = "";
    private long date1 = 0;
    private long date2 = 0;
    private boolean errorsFound = false;

    public QueryBean(Context _c) {
        model = ModelFacade.getInstance(_c);
    }

    /**
     * Checks that given string is not empty.
     * If it is nonempty, return true.
     * Else. set errors as true and return false.
     */
    public boolean ensureNonEmpty(String str) {
        if (!str.isEmpty()) {
            return true;
        } else {
            errorsFound = true;
            return false;
        }
    }

    /**
     * Validates that the date string is a correct format and
     * is a valid date value.
     * @param date
     * @return boolean
     */
    public boolean setDate1(String date) {
        date = date.trim();
        long seconds = DateComponent.getEpochSeconds(date);
        if (seconds == -1 || !date.matches(DATE_REGEX)) {
            errorsFound = true;
            return false;
        } else {
            startDate = date;
            date1 = seconds;
            return true;
        }
    }

    public boolean setDate2(String date) {
        date = date.trim();
        long seconds = DateComponent.getEpochSeconds(date);
        if (seconds == -1 || !date.matches(DATE_REGEX)) {
            errorsFound = true;
            return false;
        } else {
            endDate = date;
            date2 = seconds ;
            return true;
        }
    }

    public boolean validateRange() {
        long range = date2 - date1;
//        System.out.println("RANGE " + date2 + " " + date1 + " " + range);
        if (range > MAX_SECONDS_RANGE) {    // if date range is longer than the maximum allowed
            errorsFound = true;
            return false;
        } else {
            return true;
        }
    }

    public boolean validateOrder() {
        long range = date2 - date1;
        if (range <= 0) {   // if date 2 is the same or earlier than date 1
            return false;
        } else {
            return true;
        }
    }

    public boolean setShareSymbol1(String symbol) {
        symbol = symbol.trim();
        if (symbol.matches(SYMBOL_REGEX)) {
            this.shareSymbol1 = symbol;
            return true;
        } else {
            errorsFound = true;
            return false;
        }
    }

    public boolean setShareSymbol2(String symbol) {
        symbol = symbol.trim();
        if (symbol.isEmpty()) {
            return true;
        }
        if (symbol.matches(SYMBOL_REGEX)) {
            this.shareSymbol2 = symbol;
            return true;
        } else {
            errorsFound = true;
            return false;
        }
    }

    public void resetData() {
        startDate = "";
        endDate = "";
        date1 = 0;
        date2 = 0;
        shareSymbol1 = "";
        shareSymbol2 = "";
        errorsFound = false;
    }

    /**
     * If no errors present, creates value object and adds it to cache
     * Tells model to make query
     * @return result: String
     */
    public String query() {
        if (errorsFound) return "";

        // serialize data
        String serialized = serialize();

        // get this query from cache if exists. make value object and cache if it doesn't
        Query query = Query.queriesMap.get(serialized);
        if (query == null) {
            QueryVO queryVO = new QueryVO(serialized);
            query = Query.createQuery(queryVO);
        }

        return model.query(query.queryVO);
    }

    private String serialize() {
        return startDate + endDate + shareSymbol1 + " " + shareSymbol2;
    }

}

