package com.example.termproject_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class IndexGameActivity extends AppCompatActivity {
    LinearLayout gameIndexLayout;
    Class[] gameLayoutIndex = {
            TapOhActivity.class, Loop16Activity.class, PressCActivity.class, SliderActivity.class
    };

    int[] gameNameIndex = {
            R.string.game_index_name_1, R.string.game_index_name_2, R.string.game_index_name_3, R.string.game_index_name_4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_game);
        setTitle(R.string.game_index_title);

        gameIndexLayout = findViewById(R.id.gameIndexLayout);

        for(int i = 0; i < gameLayoutIndex.length; i++){
            final int index = i;

            Button button = new Button(this);
            button.setText(gameNameIndex[i]);
            button.setBackgroundColor(Color.parseColor("#6C6C84"));
            button.setTextSize(20);
            button.setTypeface(null, Typeface.BOLD);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(40, 30, 40, 10);

            button.setLayoutParams(params);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(IndexGameActivity.this, gameLayoutIndex[index]));
                }
            });

            gameIndexLayout.addView(button);
        }
    }
}