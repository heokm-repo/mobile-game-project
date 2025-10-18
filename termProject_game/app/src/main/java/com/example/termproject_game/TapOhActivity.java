package com.example.termproject_game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class TapOhActivity extends AppCompatActivity {   // 언어, 효과음, 배경음 추가
    private ImageView[] imageViews;
    private final int[] imgID = {R.id.clickImageViewLeft, R.id.clickImageViewRight, R.id.imageView4_1, R.id.imageView4_2, R.id.imageView3_1, R.id.imageView3_2,
            R.id.imageView2_1, R.id.imageView2_2, R.id.imageView1_1, R.id.imageView1_2};
    private TextView textViewScore, textViewTimer;
    private ArrayList<Boolean> boolSet1;
    private LinearLayout layoutLeft, layoutRight;
    private View viewLifeLeft, viewLifeRight, viewLeftbg, viewRightbg;
    private int score, life, OimageSource, XimageSource;
    private int lifeDown, lifeUp;
    private TimerThread timerThread;
    private SoundPool soundPool;
    private int soundID;
    private float SEVolume;
    private int ThreadTime;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_oh);
        setTitle(R.string.game_index_name_1);

        textViewScore = findViewById(R.id.textViewScore);
        textViewTimer = findViewById(R.id.textViewTimer);
        layoutLeft = findViewById(R.id.layoutLeftLife);
        layoutRight = findViewById(R.id.layoutRightLife);
        viewLifeLeft = findViewById(R.id.viewLifeLeft);
        viewLifeRight = findViewById(R.id.viewLifeRight);
        viewLeftbg = findViewById(R.id.viewLeftBackground);
        viewRightbg = findViewById(R.id.viewRightBackground);

        OimageSource = R.drawable.o;        // ox사진
        XimageSource = R.drawable.x;

        score = 0;
        life = CGD.TAPOH_MAX_LIFE;
        lifeDown = CGD.TAPOH_LIFE_DOWN;
        lifeUp = CGD.TAPOH_LIFE_UP;
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

        boolSet1 = new ArrayList<>();       // 이미지를 입히기 위한 boolean값 랜덤 생성
        Random random = new Random();
        for (int i = 0; i < imgID.length / 2; i++) {
            boolSet1.add(random.nextBoolean());
        }

        imageViews = new ImageView[imgID.length];       // 이미지 뷰 생성
        for(int i = 0; i < imgID.length; i++){
            final int index = i;
            imageViews[index] = findViewById(imgID[index]);
        }

        for (int i = 0; i < imgID.length / 2; i++) {      // 이미지뷰에 이미지입히기(첫 시작에만)
            int index = i * 2;
            imageViews[index].setImageResource(boolSet1.get(i) ? OimageSource : XimageSource);
            imageViews[index + 1].setImageResource(boolSet1.get(i) ? XimageSource : OimageSource);
        }

        //가장 밑 두개의 이미지뷰에 리스너 추가
        imageViews[0].setOnClickListener(v -> {
            if (isGameOver) return;
            if(boolSet1.get(0)){
                gamePlayProcessor();
                imageViewSettingProcessor();
//                playTouchSound();
                soundPool.play(soundID, SEVolume, SEVolume, 1, 0, 1.0f);
            } else {
                gameOverProcessor();
            }
        });
        imageViews[1].setOnClickListener(v -> {
            if (isGameOver) return;
            if(!boolSet1.get(0)){
                gamePlayProcessor();
                imageViewSettingProcessor();
//                playTouchSound();
                soundPool.play(soundID, SEVolume, SEVolume, 1, 0, 1.0f);
            } else {
                gameOverProcessor();
            }
        });


        Handler timerHandler = new Handler(msg -> {         //타이머 헨들러( + 체력 감소)
            ThreadTime = Integer.parseInt(msg.obj.toString());
            textViewTimer.setText(CGD.Time2String(ThreadTime));
            updateLifeView();

            if (life <= 0) {
                gameOverProcessor();
            }

            life -= lifeDown;
            return true;
        });
        timerThread = new TimerThread(timerHandler);
        timerThread.start();
    }

    @Override
    public void onBackPressed() {       // 뒤로가기로 게임을 종료할 경우 스레드 종료 메소드
        timerThread.interrupt();
        super.onBackPressed();
    }

    public void imageViewSettingProcessor() {     // 이미지 변경 메소드
        boolSet1.remove(0);
        boolSet1.add(new Random().nextBoolean());

        for (int i = 0; i < imgID.length / 2; i++) {
            int index = i * 2;
            imageViews[index].setImageResource(boolSet1.get(i) ? OimageSource : XimageSource);
            imageViews[index + 1].setImageResource(boolSet1.get(i) ? XimageSource : OimageSource);
        }
    }

    public void gamePlayProcessor() {       // 게임 플레이 처리 메소드
        // Score Up
        score += CGD.TAPOH_SCORE_UP;
        textViewScore.setText(String.valueOf(score));

        // Life Up, if Life > MAXLIFE Base Life Value
        if ((life += lifeUp) > CGD.TAPOH_MAX_LIFE) life = CGD.TAPOH_MAX_LIFE;

        // Difficulty Up
        if(score % 1000 == 0) {
            // Set Life Down Value
            if ((lifeDown += 5) > CGD.TAPOH_MAX_LIFE / 2) lifeDown = CGD.TAPOH_MAX_LIFE / 2;
            // Set Life Up Value
            if (--lifeUp <= 0) lifeUp = 1;
        }

        updateLifeView();
    }

    public void updateLifeView() {          // 변경된 체력 업데이트 메소드
        int vlife = (int) ((CGD.TAPOH_MAX_LIFE - life) * layoutRight.getHeight() / (float) CGD.TAPOH_MAX_LIFE);

        ViewGroup.LayoutParams layoutParams = viewRightbg.getLayoutParams();
        layoutParams.height = vlife;
        viewRightbg.setLayoutParams(layoutParams);
        viewLeftbg.setLayoutParams(layoutParams);

        layoutRight.invalidate();
        layoutLeft.invalidate();
    }

    public void gameOverProcessor() {       // 게임 종료시 실행 메소드
        if(!timerThread.isInterrupted()){
            timerThread.interrupt();
        }
        if (isGameOver) return; else isGameOver = true;

        View dialogView = View.inflate(TapOhActivity.this, R.layout.game_over_dialog, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(TapOhActivity.this);

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
        dbHelper.insertRankingInfo(1, score, CGD.Time2String(ThreadTime));
    }
}