package com.example.termproject_game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private Button btnstart, btnRanking, btnSetting;
    private ImageButton projectInfo;
    private MediaPlayer mainBGM;
    private String BGMFileName = "mainBGM.txt";
    private String SEFileName = "SE.txt";
    private float BGMVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        btnstart = findViewById(R.id.btnStart);
        btnRanking = findViewById(R.id.btnRanking);
        btnSetting = findViewById(R.id.btnSetting);
        projectInfo = findViewById(R.id.projectInfo);

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IndexGameActivity.class));
            }
        });

        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RankingActivity.class));
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        projectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.main_dialog_title);
                dialog.setMessage(R.string.main_dialog_message);

                dialog.setNegativeButton(R.string.main_dialog_close_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        File file = getFileStreamPath(BGMFileName);
        if(file.exists()){
//            Toast.makeText(this, BGMFileName + " 파일이 존재함", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, BGMFileName + " 파일이 존재하지 않음", Toast.LENGTH_SHORT).show();
            try {
                FileOutputStream outFs = openFileOutput(BGMFileName, Context.MODE_PRIVATE);
                String BGMVolume = "50";
                outFs.write(BGMVolume.getBytes());
                outFs.close();
//                Toast.makeText(this, BGMFileName + " 파일 생성 완료", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
//                Toast.makeText(this, BGMFileName + " 파일 생성 중 오류 발생", Toast.LENGTH_SHORT).show();
            }
        }

        file = getFileStreamPath(SEFileName);
        if(file.exists()){
//            Toast.makeText(this, SEFileName + " 파일이 존재함", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, SEFileName + " 파일이 존재하지 않음", Toast.LENGTH_SHORT).show();
            try {
                FileOutputStream outFs = openFileOutput(SEFileName, Context.MODE_PRIVATE);
                String BGMVolume = "50";
                outFs.write(BGMVolume.getBytes());
                outFs.close();
//                Toast.makeText(this, SEFileName + " 파일 생성 완료", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
//                Toast.makeText(this, SEFileName + " 파일 생성 중 오류 발생", Toast.LENGTH_SHORT).show();
            }
        }

        startBGM();
    }

    public void startBGM() {        // 배경음악 재생
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput(BGMFileName)));
            String str = reader.readLine();
            BGMVolume = (float) Integer.parseInt(str) / 100.0f;
//            Toast.makeText(this, "BGM : " + BGMVolume, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            BGMVolume = 0.5f;
//            Toast.makeText(this, " 파일 읽기 중 오류 발생", Toast.LENGTH_SHORT).show();
        }
        mainBGM = MediaPlayer.create(this, R.raw.game_background_music);
        mainBGM.setLooping(true);
        mainBGM.setVolume(BGMVolume, BGMVolume);
        mainBGM.start();
    }

    public void setBGMVolume(int volume){
        BGMVolume = (float) volume / 100.0f;
        mainBGM.setVolume(BGMVolume, BGMVolume);
    }
}