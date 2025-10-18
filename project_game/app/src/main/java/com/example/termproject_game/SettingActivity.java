package com.example.termproject_game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SettingActivity extends AppCompatActivity {
    Button btnBGM, btnSE;
    SeekBar sbBGM, sbSE;
    int preBGMProgress, preSEProgress;
    private int[] gameName = {
            R.string.setting_language_korean, R.string.setting_language_english
    };
    private String BGMFileName = "mainBGM.txt";
    private String SEFileName = "SE.txt";
    int BGMVolume, SEVolume;
//    private MediaPlayer mainBGM;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnBGM = findViewById(R.id.btnBackgroundSound);
        sbBGM = findViewById(R.id.sbBackgroundSound);
        btnSE = findViewById(R.id.btnEffectsSound);
        sbSE = findViewById(R.id.sbEffectsSound);

        try {           // 저장된 설정 불러오기
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput(BGMFileName)));
            String str = reader.readLine();
            BGMVolume = Integer.parseInt(str);
            sbBGM.setProgress(BGMVolume);

            reader = new BufferedReader(new InputStreamReader(openFileInput(SEFileName)));
            str = reader.readLine();
            SEVolume = Integer.parseInt(str);
            sbSE.setProgress(SEVolume);
        } catch (IOException e) {
            sbBGM.setProgress(50);
            sbSE.setProgress(50);
        }

        preBGMProgress = sbBGM.getProgress();
        preSEProgress = sbSE.getProgress();

        btnBGM.setText(getString(R.string.setting_background_music) +
                sbBGM.getProgress());
        btnSE.setText(getString(R.string.setting_sound_effect) +
                sbSE.getProgress());

        if(sbBGM.getProgress() == 0){
            btnBGM.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, android.R.drawable.ic_lock_silent_mode, 0);
        }
        if(sbSE.getProgress() == 0){
            btnSE.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, android.R.drawable.ic_lock_silent_mode, 0);
        }

        btnBGM.setOnClickListener(new View.OnClickListener() {      // 배경음 버튼
            @Override
            public void onClick(View v) {
                if(sbBGM.getProgress() != 0){
                    btnBGM.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, android.R.drawable.ic_lock_silent_mode, 0);
                    sbBGM.setProgress(0);
                } else {
                    btnBGM.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, android.R.drawable.ic_lock_silent_mode_off, 0);
                    sbBGM.setProgress(preBGMProgress);
                }
            }
        });

        sbBGM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {    // 배경음 시크바
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress != 0){
                    btnBGM.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, android.R.drawable.ic_lock_silent_mode_off, 0);
                } else {
                    btnBGM.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, android.R.drawable.ic_lock_silent_mode, 0);
                }
                btnBGM.setText(getString(R.string.setting_background_music) + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                preBGMProgress = sbBGM.getProgress();
                try {
                    FileOutputStream outFs = openFileOutput(BGMFileName, Context.MODE_PRIVATE);
                    String BGMVolume = String.valueOf(preBGMProgress);
                    outFs.write(BGMVolume.getBytes());
                    outFs.close();
                } catch (IOException e) { }

                //////   MainActivity의 메소드인 setBGMVolume를 실행
            }
        });

        btnSE.setOnClickListener(new View.OnClickListener() {       // 효과음 버튼
            @Override
            public void onClick(View v) {
                BackgroundMusic.mainBGM.setVolume(sbSE.getProgress(), sbSE.getProgress());
                if(sbSE.getProgress() != 0){
                    btnSE.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, android.R.drawable.ic_lock_silent_mode, 0);
                    sbSE.setProgress(0);
                } else {
                    btnSE.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, android.R.drawable.ic_lock_silent_mode_off, 0);
                    sbSE.setProgress(preBGMProgress);
                }
            }
        });

        sbSE.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {     // 효과음 시크바
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress != 0){
                    btnSE.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, android.R.drawable.ic_lock_silent_mode_off, 0);
                } else {
                    btnSE.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, android.R.drawable.ic_lock_silent_mode, 0);
                }
                btnSE.setText(getString(R.string.setting_sound_effect) + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                preSEProgress = sbSE.getProgress();
                try {
                    FileOutputStream outFs = openFileOutput(SEFileName, Context.MODE_PRIVATE);
                    String BGMVolume = String.valueOf(preSEProgress);
                    outFs.write(BGMVolume.getBytes());
                    outFs.close();
                } catch (IOException e) { }
            }
        });
    }
}