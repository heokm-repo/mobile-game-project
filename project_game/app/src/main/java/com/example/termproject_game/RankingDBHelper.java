package com.example.termproject_game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RankingDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ranking.db";
    private static final int DATABASE_VERSION = 2;

    public RankingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ranking (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "gameid INTEGER," +
                "score INTEGER," +
                "time TEXT," +
                "date TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ranking");
        onCreate(db);
    }

    public void insertRankingInfo(int gameid, int score, String time) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("SELECT COUNT(*)" +
                " FROM Ranking;", null);
        cursor.moveToFirst();
        int newId = cursor.getInt(0) + 1;
        cursor.close();

        values.put("_id", newId);
        values.put("gameid", gameid);
        values.put("score", score);
        values.put("time", time);

        String currentDate = new SimpleDateFormat("yyyy/MM/dd",
                Locale.getDefault()).format(new Date());

        values.put("date", currentDate);

        long newRowId = db.insert("ranking", null, values);
    }
}
