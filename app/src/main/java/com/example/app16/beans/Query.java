package com.example.app16.beans;

import com.example.app16.valueObjects.QueryVO;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Cache containing list of past queries in current session
 */
class Query {
    static ArrayList<Query> allInstances = new ArrayList<Query>();
    static HashMap<String, Query> queriesMap = new HashMap<>();
    QueryVO queryVO;

    Query() {
        allInstances.add(this);
    }

    static Query createQuery() {
        Query result = new Query();
        return result;
    }

    static Query createQuery(QueryVO queryVO) {
        Query result = new Query();
        result.queryVO = queryVO;
        queriesMap.put(queryVO.getSerialized(), result);
        return result;
    }
}

