package com.example.app16.valueObjects;

import java.util.Arrays;

public class QueryVO {
    private String startDate = "";
    private String endDate = "";
    private String shareSymbol1 = "";
    private String shareSymbol2 = "";
    private String serialized = "";

    public QueryVO(String serialized) {
        this.serialized = serialized;
        deserialize();
    }

    private void deserialize() {
        String[] splitInput = serialized.split(" ");
        char[] charArr = splitInput[0].toCharArray();
        startDate = new String(Arrays.copyOfRange(charArr, 0, 10));
        endDate = new String(Arrays.copyOfRange(charArr, 10, 20));
        shareSymbol1 = new String((Arrays.copyOfRange(charArr, 20, charArr.length)));
        if (splitInput.length == 2) shareSymbol2 = splitInput[1];
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getShareSymbol1() {
        return shareSymbol1;
    }

    public String getShareSymbol2() {
        return shareSymbol2;
    }

    public String getSerialized() {
        return serialized;
    }
}


