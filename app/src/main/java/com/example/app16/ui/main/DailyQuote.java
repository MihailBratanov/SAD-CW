package com.example.app16.ui.main;

import com.example.app16.valueObjects.DailyQuoteVO;

import java.util.*;
import java.util.HashMap;
import java.util.ArrayList;


/**
 * Cache for daily quotes. Should contain all prices retrieved from the last search
 */
public class DailyQuote {
    static ArrayList<DailyQuote> allInstances = new ArrayList<DailyQuote>();
    static Map<String, DailyQuote> quotesMap = new HashMap<String, DailyQuote>();
    static HashMap<String, ArrayList<DailyQuote>> allInstancesByShareSymb = new HashMap<>();

    DailyQuoteVO dailyQuoteVO;

    DailyQuote() {
        allInstances.add(this);
    }

    static DailyQuote createDailyQuote() {
        DailyQuote result = new DailyQuote();
        return result;
    }

    String date = ""; /* primary */

    static DailyQuote createDailyQuote(DailyQuoteVO dailyQuoteVO) {
        if (dailyQuoteVO != null) {
            DailyQuote result = new DailyQuote();
            result.dailyQuoteVO = dailyQuoteVO;
            DailyQuote.quotesMap.put(dailyQuoteVO.getSerialised(), result);

            if(!allInstancesByShareSymb.containsKey(dailyQuoteVO.getShareSymbol())){
                allInstancesByShareSymb.put(dailyQuoteVO.getShareSymbol(), new ArrayList<DailyQuote>());
            }
            allInstancesByShareSymb.get(dailyQuoteVO.getShareSymbol()).add(result);
            return result;
        }
        return null;
    }

    static void empty() {
        allInstances.clear();
        quotesMap.clear();
        allInstancesByShareSymb.clear();
    }

}

