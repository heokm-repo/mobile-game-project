package com.example.termproject_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RankingActivity extends AppCompatActivity {
    private Spinner rankSpin;
    private TableLayout rankTable;
    private final int MAXVIEW = 20;
    private int[] gameName = {
            R.string.game_index_name_1, R.string.game_index_name_2,
            R.string.game_index_name_3, R.string.game_index_name_4
    };

    private RankingDBHelper rankingDBHelper;
    private SQLiteDatabase db;

    private TVD[] TVDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_screen);
        setTitle(R.string.ranking_title);

        rankSpin = findViewById(R.id.rankSpinner);
        rankTable = findViewById(R.id.rankTable);

        TextView[] ATV = new TextView[gameName.length];
        for (int i = 0; i < gameName.length; i++) {
            ATV[i] = new TextView(getApplicationContext());
            ATV[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ATV[i].setText(getString(gameName[i]));
            ATV[i].setTextSize(30);
            ATV[i].setBackgroundColor(Color.parseColor("#6C6C84"));
            ATV[i].setTextColor(Color.WHITE);
            ATV[i].setGravity(Gravity.CENTER);
        }

        TextViewAdapter adapter = new TextViewAdapter(
                this, android.R.layout.simple_spinner_item, ATV);
        rankSpin.setAdapter(adapter);

        rankingDBHelper = new RankingDBHelper(this);
//        rankingDBHelper.insertRankingInfo(1, 300, "00:07");

        rankSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTable(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택이 해제 처리
            }
        });
    }

    public static class TextViewAdapter extends ArrayAdapter<TextView> {
        private TextView[] textViewArray;

        public TextViewAdapter(Context context, int textViewResourceId, TextView[] textViewArray) {
            super(context, textViewResourceId, textViewArray);
            this.textViewArray = textViewArray;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return textViewArray[position];
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return textViewArray[position];
        }
    }

    public void updateTable(int pos) {
        int selectId = pos + 1;

        db = rankingDBHelper.getReadableDatabase();
        Cursor cursor;

        switch (selectId){
            case 1:
            case 2:
            case 4:
                cursor = db.rawQuery("SELECT score, time, date" +
                        " FROM Ranking" +
                        " WHERE gameid = " + selectId +
                        " ORDER BY score DESC", null);
                break;
            case 3:
                cursor = db.rawQuery("SELECT score, time, date" +
                        " FROM Ranking" +
                        " WHERE gameid = " + selectId +
                        " ORDER BY time", null);
                break;
            default:
                cursor = null;
                break;
        }

        // rankTable Reset
        rankTable.removeAllViews();

        // rankTable Change
        if (cursor.moveToFirst()) {
            int index = 1;

            TableRow tr = new TableRow(RankingActivity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            // Set TextView Data Array
            TVDs = new TVD[]{
                    new TVD(getResources().getString(R.string.ranking_rank), 2),
                    new TVD(getResources().getString(R.string.ranking_point), 6),
                    new TVD(getResources().getString(R.string.ranking_time), 4),
                    new TVD(getResources().getString(R.string.ranking_date), 5) };

            // Add TextViews Top head in [rankTable, tr]
            for (int i = 0; i < TVDs.length; i++) {
                TextView tv = new TextView(RankingActivity.this);
                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, TVDs[i].initWeigth));
                tv.setText(TVDs[i].Text);
                textViewStyle(tv);
                tr.addView(tv);
            }
            rankTable.addView(tr);

            // Add Selected Ranking
            do {
                tr = new TableRow(RankingActivity.this);
                tr.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                // Set TextView Data Array
                TVDs = new TVD[]{
                        new TVD(index+++"", 2),
                        new TVD(cursor.getString(0), 6),
                        new TVD(cursor.getString(1), 4),
                        new TVD(cursor.getString(2), 5) };

                // Add TextViews Selected Ranking[] in [rankTable, tr]
                for (int i = 0; i < TVDs.length; i++) {
                    TextView tv = new TextView(RankingActivity.this);
                    tv.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, TVDs[i].initWeigth));
                    tv.setText(TVDs[i].Text);
                    textViewStyle(tv);
                    tr.addView(tv);
                }
                rankTable.addView(tr);
            } while(cursor.moveToNext());
        } else {
            TableRow tr = new TableRow(RankingActivity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView textView = new TextView(RankingActivity.this);
            textView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 2));
            textView.setText(R.string.ranking_noranking);
            textViewStyle(textView);

            tr.addView(textView);
            rankTable.addView(tr);
        }
        cursor.close();
        db.close();
    }

    public void textViewStyle(TextView textView){
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(24);
    }


    class TVD {
        String Text;
        int initWeigth;
        TVD(String t, int iW) { Text = t; initWeigth = iW; }
    }
}