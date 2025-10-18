package com.example.termproject_game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PressCActivity extends AppCompatActivity {
    private TextView textViewScore, textViewTimer;
    private Button btnTouch;
    private int count;
    private TimerThread timerThread;
    private SoundPool soundPool;
    private int soundID;
    private float SEVolume;
    private int ThreadTime;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_c);
        setTitle(R.string.game_index_name_3);

        textViewScore = findViewById(R.id.textViewScore);
        textViewTimer = findViewById(R.id.textViewTimer);
        btnTouch = findViewById(R.id.btnTouch);

        count = CGD.PressC_CLICKCOUNT;
        textViewScore.setText(String.valueOf(CGD.PressC_CLICKCOUNT));
        isGameOver = false;

        try {           // 효과음 설정 불러오기
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput("SE.txt")));
            String str = reader.readLine();
            SEVolume = (float) Integer.parseInt(str) / 100.0f;
        } catch (IOException e) {
            SEVolume = 0.5f;
        }
        soundPool = new SoundPool.Builder().build();        // 터치음
        soundID = soundPool.load(this, R.raw.game_1_touch_sound, 1);

        btnTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGameOver) return;
                gamePlayProcessor();
                soundPool.play(soundID, SEVolume, SEVolume, 1, 0, 1.0f);
            }
        });

        Handler timerHandler = new Handler(msg -> {         //타이머 헨들러
            ThreadTime = Integer.parseInt(msg.obj.toString());
            textViewTimer.setText(CGD.Time2String(ThreadTime));

            return true;
        });
        timerThread = new TimerThread(timerHandler);
        timerThread.start();
    }

    public void gamePlayProcessor() {       // 게임 플레이 처리 메소드
        textViewScore.setText(String.valueOf(--count));
        if(count <= 0){
            gameOverProcessor();
        }
    }

    public void gameOverProcessor() {       // 게임 종료시 실행 메소드
        if(!timerThread.isInterrupted()){
            timerThread.interrupt();
        }
        if (isGameOver) return; else isGameOver = true;

        View dialogView = View.inflate(PressCActivity.this, R.layout.game_over_dialog, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(PressCActivity.this);

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
        dbHelper.insertRankingInfo(3, ThreadTime, CGD.Time2String(ThreadTime));
    }
}