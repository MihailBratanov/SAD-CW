package com.example.app16.dbUtils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.app16.valueObjects.DailyQuoteVO;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessor extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "QUOTES";
    public static final String COL_QUOTE_DATA = "QUOTE_DATA";
    public static final String COLUMN_QUOTE_VALUE = "QUOTE_VALUE";
    public static final String COLUMN_ID = "ID";
    public static final String DATABASE_NAME = "quotes.db";

    private static SQLiteDatabase db;
    @SuppressLint("StaticFieldLeak")
    private static DatabaseAccessor instance;
    @SuppressLint("StaticFieldLeak")
    private static Context context;


    public DatabaseAccessor(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        DatabaseAccessor.context = context;
    }

    //this method is called when the database is first accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDatabaseStatement = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_QUOTE_DATA + " TEXT, " + COLUMN_QUOTE_VALUE + " REAL );";

        db.execSQL(createDatabaseStatement);
    }

    //call this method when we make changes to the schema after deployment
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public static synchronized DatabaseAccessor getInstance() {
        if (instance == null) {
            instance = new DatabaseAccessor(context);
            db = instance.getWritableDatabase();
        }
        return instance;
    }

    //method to add a DailyQuote object to the database
    public boolean saveQuote(DailyQuoteVO dailyQuoteVO) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_QUOTE_DATA, dailyQuoteVO.getSerialised());
        cv.put(COLUMN_QUOTE_VALUE, dailyQuoteVO.getValue());

        long insert = db.insert(TABLE_NAME, null, cv);
        return insert != -1;
    }

    @Override
    public synchronized void close() {
        if (instance != null) {
            db.close();
        }
    }

    // Given a share symbol and a date, find corresponding data in table and return DailyQuoteVO
    public DailyQuoteVO getQuote(String shareSymbol, long date) {
        String col_quote_data = shareSymbol + " " + date;
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE QUOTE_DATA='" + col_quote_data + "'";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            try {
                int ID = cursor.getInt(0);
                String data = cursor.getString(1);
                double quoteValue = cursor.getDouble(2);
                String[] separated = data.split("\\s+");
                String quoteName = separated[0];
                long quoteDates = Long.parseLong(separated[1]);
                return new DailyQuoteVO(quoteName, quoteDates, quoteValue);

            } catch (SQLiteException e) {
                return null;
            }

        }
        return null;
    }


}
