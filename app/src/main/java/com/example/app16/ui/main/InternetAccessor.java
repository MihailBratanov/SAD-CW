package com.example.app16.ui.main;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetAccessor extends AsyncTask<String, Void, String> {
    private InternetCallback delegate = null;
    private static InternetAccessor instance = null;
    private String shareSymbol = "";

    public void setDelegate(InternetCallback c) {
        delegate = c;
    }

    public static InternetAccessor getInstance() {
        if (instance == null) {
            instance = new InternetAccessor();
        }
        return instance;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        shareSymbol = params[1];
        String myData = "";
        try {
            myData = fetchUrl(url);
        } catch (Exception _e) {
            delegate.completeInternetAccess(null, "");
            return null;
        }
        return myData;
    }

    private String fetchUrl(String url) {
        String urlContent = "";
        StringBuilder myStrBuff = new StringBuilder();

        try {
            URL myUrl = new URL(url);
            HttpURLConnection myConn = (HttpURLConnection) myUrl.openConnection();
            myConn.setRequestProperty("User-Agent", "");
            myConn.setRequestMethod("GET");
            myConn.setDoInput(true);
            myConn.connect();

            InputStream myInStrm = myConn.getInputStream();
            BufferedReader myBuffRdr = new BufferedReader(new InputStreamReader(myInStrm));

            while ((urlContent = myBuffRdr.readLine()) != null) {
                myStrBuff.append(urlContent + '\n');
            }

        } catch (IOException e) {
            delegate.completeInternetAccess(null, "");
            return null;
        }

        return myStrBuff.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.completeInternetAccess(result, shareSymbol);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}

interface InternetCallback {
    public void completeInternetAccess(String response, String shareSymbol);
}


