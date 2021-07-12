package com.example.app16.beans;

import com.example.app16.utils.GraphDisplay;

import java.util.ArrayList;

class AnalyseCache { static ArrayList<AnalyseCache> Analyse_allInstances = new ArrayList<AnalyseCache>();

  AnalyseCache() { Analyse_allInstances.add(this); }

  static AnalyseCache createAnalyse() { AnalyseCache result = new AnalyseCache();
    return result; }


  public GraphDisplay analyse()
  {
    GraphDisplay result = null;
    return result;
  }

}

