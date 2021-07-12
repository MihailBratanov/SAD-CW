package com.example.app16.valueObjects;

public class DailyQuoteVO {
    private String shareSymbol;
    private long date;
    private double value;
    private String serialised = "";

    //secondary constructor just in case
    public DailyQuoteVO(){
    }

    //constructor for class Quote, object to be inserted into the database
    public DailyQuoteVO(String shareSymbol, long date, double value) {
        this.shareSymbol = shareSymbol;
        this.date = date;
        this.value = value;
        serialised = shareSymbol + " " + date;
    }

//    //toString() just in case
//    @Override
//    public String toString() {
//        return "Quote{" +
//                "quoteName='" + quoteName + '\'' +
//                ", quoteDates='" + quoteDates + '\'' +
//                ", quoteValue=" + quoteValue +
//                '}';
//    }

    public String getShareSymbol() {
        return shareSymbol;
    }

    public long getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    public String getSerialised() {
        return serialised;
    }



}
