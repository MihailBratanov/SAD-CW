package com.example.app16.ui.main;

import android.content.Context;
import android.renderscript.ScriptIntrinsicYuvToRGB;

import com.example.app16.dbUtils.DatabaseAccessor;
import com.example.app16.utils.DateComponent;
import com.example.app16.utils.FileAccessor;
import com.example.app16.utils.GraphDisplay;
import com.example.app16.valueObjects.DailyQuoteVO;
import com.example.app16.valueObjects.QueryVO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Connection between Controllers and Model. Uses Model functions to compute
 * Controller requests.
 */
public class ModelFacade {
    FileAccessor fileSystem;
    Context myContext;
    static ModelFacade instance = null;
    DailyQuote_DAO dao;
    DatabaseAccessor db;

    public static ModelFacade getInstance(Context context) {
        if (instance == null) {
            instance = new ModelFacade(context);
        }
        return instance;
    }


    private ModelFacade(Context context) {
        myContext = context;
        fileSystem = new FileAccessor(context);
        db=new DatabaseAccessor(myContext);
        dao = new DailyQuote_DAO();
    }

    /*
    public void internetAccessCompleted(String response) {
        DailyQuote_DAO.makeFromCSV(response);
    }
     */

    // Sends quote request to DAO, receives number of results and returns this to the controller
    public String query(QueryVO vo) {
        String startDate = vo.getStartDate();
        String endDate = vo.getEndDate();
        String shareSymbol1 = vo.getShareSymbol1();
        String shareSymbol2 = vo.getShareSymbol2();
        dao.refreshCache();

        boolean result1 = dao.getQuotesInRange(startDate, endDate, shareSymbol1);
        if (shareSymbol2 != "") {
            boolean result2 = dao.getQuotesInRange(startDate, endDate, shareSymbol2);
        }
        return "Completed!";
    }

    // requests DAO for cache values and returns a graph using these values
    public GraphDisplay analyse() {
        GraphDisplay result = new GraphDisplay();
        ArrayList<DailyQuote> quotes = null;

        if ( DailyQuote.allInstances.isEmpty()){
            return null;
            //TODO [Z] Print to text fragment
        }

        //Group the instances of DailyQuotes in the cache by share symbol.
        HashMap<String, ArrayList<DailyQuote>> allInstancesByShareSymb = DailyQuote.allInstancesByShareSymb;

        //For each share symbol create a new line for the graph.
        for (String shareSymb : allInstancesByShareSymb.keySet()) {
            quotes = Ocl.copySequence(allInstancesByShareSymb.get(shareSymb));

            ArrayList<Long> xnames = null;
            xnames = Ocl.copySequence(Ocl.collectSequence(quotes, (q) -> {
                return q.dailyQuoteVO.getDate();
            }));

            ArrayList<Double> yvalues = null;
            yvalues = Ocl.copySequence(Ocl.collectSequence(quotes, (q) -> {
                return q.dailyQuoteVO.getValue();
            }));

            result.addNominalLine(shareSymb, xnames, yvalues);
        }

        result.setxname("Date");
        result.setyname("Value");

        return result;
    }

}
