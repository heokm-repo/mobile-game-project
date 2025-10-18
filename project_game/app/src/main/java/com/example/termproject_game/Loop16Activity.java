package com.example.termproject_game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Loop16Activity extends AppCompatActivity {
    private TableLayout tableLayout;
    private TextView textViewScore, textViewTimer;
    private List<Integer> textViewID = new ArrayList<>();
    private List<TextView> textViews = new ArrayList<>();
    private List<Integer> numbers = new ArrayList<>();
    private TimerThread timerThread;
    private int count;
    private SoundPool soundPool;
    private int soundID;
    private int score;
    private final long SET_TIME = 90;
    private float SEVolume;
    private int ThreadTime;
    private boolean isGameOver = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_16);
        setTitle(R.string.game_index_name_2);

        tableLayout = findViewById(R.id.tableLayout);
        textViewScore = findViewById(R.id.textViewScore);
        textViewTimer = findViewById(R.id.textViewTimer);

        count = 1;                  // 눌러야 할 숫자
        score = 0;                  // 점수 초기화
        isGameOver = false;

        try {           // 효과음 설정 불러오기
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput("SE.txt")));
            String str = reader.readLine();
            SEVolume = (float) Integer.parseInt(str) / 100.0f;
        } catch (IOException e) {
            SEVolume = 0.5f;
        }

        soundPool = new SoundPool.Builder().build();        // 터치음 설정
        soundID = soundPool.load(this, R.raw.game_1_touch_sound, 1);

        for(int i = 1; i <= CGD.LOOP16_X * CGD.LOOP16_Y; i++){       // 리스트에 숫자 추가
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        // Draw Table
        for (int i = 0; i < CGD.LOOP16_Y; i++) {
            TableRow tr = new TableRow(Loop16Activity.this);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < CGD.LOOP16_X; j++) {
                int k = CGD.LOOP16_X * i + j;
                TextView ftv = new TextView(Loop16Activity.this);
                textViewID.add(100100 + k);
                ftv.setId(textViewID.get(k));
                textViews.add(ftv);

                textViews.get(k).setBackgroundColor(Color.parseColor("#6C6C84"));
                textViews.get(k).setText(numbers.get(k).toString());
                textViews.get(k).setGravity(Gravity.CENTER);
                textViews.get(k).setTextSize(50);

                textViews.get(k).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        soundPool.play(soundID, SEVolume, SEVolume, 1, 0, 1.0f);
                        int getNum = Integer.parseInt(textViews.get(k).getText().toString());
                        textViews.get(k).setText(null);
                        textViews.get(k).setEnabled(false);
                        textViews.get(k).setVisibility(View.INVISIBLE);
                        gamePlayProcessor(getNum);
                    }
                });
                tr.addView(ftv);
            }
            tableLayout.addView(tr);
        }

        tableLayout.post(new Runnable() {       // 텍스트 뷰 크기 조정
            @Override
            public void run() {
                int tvSize = (tableLayout.getWidth() / 4) - (CGD.LOOP16_MARGINSIZE * 2);     // 텍스트 뷰 크기

                for (int i = 0; i < CGD.LOOP16_X * CGD.LOOP16_Y; i++) {
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(tvSize, tvSize);
                    layoutParams.setMargins(CGD.LOOP16_MARGINSIZE, CGD.LOOP16_MARGINSIZE, CGD.LOOP16_MARGINSIZE, CGD.LOOP16_MARGINSIZE);
                    textViews.get(i).setLayoutParams(layoutParams);
                }
            }
        });

        Handler timerHandler = new Handler(msg -> {         //타이머 헨들러( + 체력 감소)
            ThreadTime = Integer.parseInt(msg.obj.toString());
            textViewTimer.setText(CGD.Time2String(ThreadTime));
            textViewScore.setText("" + score);

            if (ThreadTime <= 0) gameOverProcessor();

            return true;
        });
        timerThread = new TimerThread(timerHandler, SET_TIME);
        timerThread.start();
    }

    @Override
    public void onBackPressed() {       // 뒤로가기로 게임을 종료할 경우 스레드 종료 메소드
        timerThread.interrupt();
        super.onBackPressed();
    }

    public void gamePlayProcessor(int num) {     // 게임 플레이 처리 메소드
        if(count == num){
            count++;
            score += CGD.LOOP16_SCORE_UP;
            textViewScore.setText(String.valueOf(score));
        } else {
            timerThread.interrupt();
            gameOverProcessor();
        }

        if(16 < count){
            Collections.shuffle(numbers);
            count = 1;

            for(int i = 0; i < CGD.LOOP16_X * CGD.LOOP16_Y; i++){
                textViews.get(i).setText(numbers.get(i).toString());
                textViews.get(i).setEnabled(true);
                textViews.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    public void gameOverProcessor() {       // 게임 종료시 실행 메소드
        if(!timerThread.isInterrupted()){
            timerThread.interrupt();
        }
        if (isGameOver) return; else isGameOver = true;

        View dialogView = View.inflate(Loop16Activity.this, R.layout.game_over_dialog, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(Loop16Activity.this);

        TextView textViewFinalScore = dialogView.findViewById(R.id.textViewFinalScore);
        TextView textViewFinalTime = dialogView.findViewById(R.id.textViewFinalTime);
        textViewFinalScore.setText(textViewScore.getText());
        textViewFinalTime.setText(textViewTimer.getText());

        Button btnEnd = dialogView.findViewById(R.id.btnReturnMain);
        Button btnReset = dialogView.findViewById(R.id.btnRetry);

        btnEnd.setOnClickListener(v -> finish());

        btnReset.setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });

        dlg.setView(dialogView);
        dlg.setCancelable(false);
        dlg.show();

        soundPool.play(R.raw.game_over_sound, SEVolume, SEVolume, 1, 0, 1.0f);

        RankingDBHelper dbHelper = new RankingDBHelper(this);
        dbHelper.insertRankingInfo(2, score, CGD.Time2String(ThreadTime));
    }
}

