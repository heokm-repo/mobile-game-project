package com.example.termproject_game;

// Const Game Data
public class CGD {

    // ---------------------------------------------------------------------------------

    // Base Value in Thread.sleep()
    static public final int THREAD_SLEEP = 100;

    // Base Time Value in Thread
    static public final int BASE_TIME = 0;

    // ---------------------------------------------------------------------------------

    // Max Life in [ TapOh ]
    static public final int TAPOH_MAX_LIFE = 10000;
    // Life Down in [ TapOh ]
    static public final int TAPOH_LIFE_DOWN = 50;
    // Life Up in [ TapOh ]
    static public final int TAPOH_LIFE_UP = 250;
    // Score Up in [ TapOh ]
    static public final int TAPOH_SCORE_UP = 100;

    // ---------------------------------------------------------------------------------

    // Number of blocks in the table in [ Loop 16 ]
    static public final int LOOP16_X = 4;
    static public final int LOOP16_Y = 4;

    // Number of Margin Size in [ Loop 16 ]
    static public final int LOOP16_MARGINSIZE = 10;

    // Score Up in [ Loop 16 ]
    static public final int LOOP16_SCORE_UP = 100;

    // ---------------------------------------------------------------------------------

    // Game End Click Count in [ PressC ]
    static public final int PressC_CLICKCOUNT = 10;

    // ---------------------------------------------------------------------------------

    // Min Line Length in [ Slider ]
    static public final int Slider_LineMinLength = 128;

    // Max Line Length in [ Slider ]
    static public final int Slider_LineMaxLength = 512;

    // Line Width in [ Slider ]
    static public final int Slider_LineWidth = 128;

    // ---------------------------------------------------------------------------------

    // Interger Time to String
    static String Time2String(int t) {
        int m = (int) (t / 1000) / 60;
        int s = (int) (t / 1000) % 60;
        int ms = (int) (t % 1000 / 10);

        return String.format("%02d : %02d.%02d", m, s, ms);
    }
}
