package com.example.termproject_game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SliderActivity extends AppCompatActivity {

    CanvasView cv;
    private TimerThread timerThread;
    private TextView textViewScore, textViewTimer;
    View vc;
    private int ThreadTime;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        cv = (CanvasView) findViewById(R.id.CanvasView);
        vc = (View) findViewById(R.id.ViewCtrl);
        textViewScore = findViewById(R.id.textViewScore);
        textViewTimer = findViewById(R.id.textViewTimer);

        isGameOver = false;

        cv.post(new Runnable() {
            @Override public void run() {
                cv.CanvasXSize = cv.getWidth();
                cv.CanvasYSize = cv.getHeight();
            }
        });
        LinePosition.AllLine.clear();

        vc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cv.PlayerPosX = event.getX();
                return true;
            }
        });

        Handler timerHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                cv.invalidate();

                ThreadTime = Integer.parseInt(msg.obj.toString());
                textViewTimer.setText(CGD.Time2String(ThreadTime));
                textViewScore.setText(ThreadTime+"");
                if (cv.isEnd) gameOverProcessor();

                return true;
            }
        });
        timerThread = new TimerThread(timerHandler, CGD.BASE_TIME, 10);
        cv.timerThread = timerThread;
        timerThread.start();
    }

    @Override
    public void onBackPressed() {
        timerThread.interrupt();
        LinePosition.AllLine.clear();
        super.onBackPressed();
    }


    public void gameOverProcessor() {       // 게임 종료시 실행 메소드
        if(!timerThread.isInterrupted()){
            timerThread.interrupt();
        }
        if (isGameOver) return; else isGameOver = true;


        View dialogView = View.inflate(SliderActivity.this, R.layout.game_over_dialog, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(SliderActivity.this);

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

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.game_over_sound);
        mediaPlayer.start();

        RankingDBHelper dbHelper = new RankingDBHelper(this);
        dbHelper.insertRankingInfo(4, ThreadTime, CGD.Time2String(ThreadTime));
    }
}