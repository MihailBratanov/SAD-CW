package com.example.app16.ui.main;

import com.example.app16.dbUtils.DatabaseAccessor;
import com.example.app16.utils.DateComponent;
import com.example.app16.valueObjects.DailyQuoteVO;

import java.util.ArrayList;

/**
 * Class which searches for relevant requested data. Accesses DailyQuoteCache, internet and database
 */
public class DailyQuote_DAO implements InternetCallback {
    private long t1;
    private long t2;
    private String shareSymbol = "";
    InternetAccessor x;
    DatabaseAccessor database = DatabaseAccessor.getInstance();

    // This gets called by InternetAccessor after it is complete
    public synchronized void completeInternetAccess(String response, String shareSymbol) {
        makeFromCSV(response, shareSymbol);
    }

    //  function which finds data given companySymbol (1 or 2?) and date range
    //       1. Empties cache
    //       2. Searches internet or database depending on internet access for required data and
    //          fills cache
    //       3. Returns number of items in cache
    public synchronized boolean getQuotesInRange(String startDate, String endDate, String shareSymbol) {
        try {
            t1 = DateComponent.getEpochSeconds(startDate);
            t2 = DateComponent.getEpochSeconds(endDate);
            this.shareSymbol = shareSymbol;

            x = new InternetAccessor();
            x.setDelegate(this);

            if (hasInternetAccess()) searchInternet();
            else searchDatabase();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void refreshCache() {
        DailyQuote.empty();
    }

    // Forms a url and executes it using the internet accessor
    private synchronized void searchInternet() {
        ArrayList<String> sq1 = Ocl.copySequence(Ocl.initialiseSequence("period1", "period2", "interval", "events"));
        ArrayList<String> sq2 = Ocl.copySequence(Ocl.initialiseSequence((t1 + ""), (t2 + ""), "1d", "history"));
        String url = getURL(shareSymbol, sq1, sq2);
        x.execute(url, shareSymbol);     // the csv parsing happens upon this statement
    }

    private void searchDatabase() {
        long date = t1;
        while (date < t2) {
            DailyQuoteVO dailyQuoteVO = database.getQuote(shareSymbol, date);
            if(dailyQuoteVO!=null) {
                DailyQuote.createDailyQuote(dailyQuoteVO);
            }
            date += 86400;
        }
    }

    private String getURL(String command, ArrayList<String> pars, ArrayList<String> values) {
        String res = "https://query1.finance.yahoo.com/v7/finance/download/";
        if (command != null) {
            res = res + command;
        }
        if (pars.size() == 0) {
            return res;
        }
        res = res + "?";
        for (int i = 0; i < pars.size(); i++) {
            String par = pars.get(i);
            String val = values.get(i);
            res = res + par + "=" + val;
            if (i < pars.size() - 1) {
                res = res + "&";
            }
        }
        return res;
    }

    // creates DailyQuote from given line and adds to DailyQuote instances and DailyQuote.quotesMap
    private DailyQuote parseCSV(String _line, String shareSymbol) {
        if (_line == null) return null;
        ArrayList<String> lineVals = Ocl.tokeniseCSV(_line);
        long date = DateComponent.getEpochSeconds((String) lineVals.get(0));
        double value = Double.parseDouble((String) lineVals.get(4));
        DailyQuote dailyquote = DailyQuote.quotesMap.get(shareSymbol + " " + date);

        if (dailyquote == null) {
            DailyQuoteVO dailyQuoteVO = new DailyQuoteVO(shareSymbol, date, value);
            dailyquote = DailyQuote.createDailyQuote(dailyQuoteVO);
        }
        return dailyquote;
    }

    // Take a csv in string format and extract DailyQuotes
    private void makeFromCSV(String lines, String shareSymbol) {
        if (lines == null) return;

        ArrayList<String> rows = Ocl.parseCSVtable(lines);
        for (int i = 1; i < rows.size(); i++) {
            String row = rows.get(i);
            if (row != null && row.trim().length() != 0) {
                DailyQuote _x = parseCSV(row, shareSymbol);  // adds to DailyQuote cache
                // add value object to database
                boolean write= database.saveQuote(_x.dailyQuoteVO);
            }
        }
    }

    // Check if there is internet access
    public boolean hasInternetAccess() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    // Check if a certain quote is cached
    private boolean isCached(String id) {
        if (DailyQuote.quotesMap.get(id) == null) return false;
        return true;
    }

    // Get a DailyQuote from cache
    private DailyQuote getCachedInstance(String id) {
        return DailyQuote.quotesMap.get(id);
    }
}
