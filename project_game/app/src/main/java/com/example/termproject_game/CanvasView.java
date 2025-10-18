package com.example.termproject_game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class CanvasView extends View {

    long tick = 0;
    private ArrayList<Boolean> boolSet1;
    public int CanvasXSize = -1;
    public int CanvasYSize = -1;
    public float PlayerPosX = -100;
    public float PlayerPosY = -100;

    public TimerThread timerThread;

    boolean isLoad = false;
    boolean isEnd = false;
    Random rd = new Random();

    Paint pLine, pCir;


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        pLine = new Paint();
        pLine.setStrokeWidth(CGD.Slider_LineWidth);
        pLine.setAntiAlias(true);
        pLine.setColor(0xFFFF0000);
        pLine.setStrokeCap(Paint.Cap.ROUND);

        pCir = new Paint();
        pCir.setAntiAlias(true);
        pCir.setStyle(Paint.Style.STROKE);
        pCir.setColor(0xFF00FF00);
        pCir.setStrokeWidth(CGD.Slider_LineWidth / 16);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (CanvasXSize < 0) return;

        if (!isLoad) {
            isLoad = true;
            PlayerPosY = CanvasYSize - CGD.Slider_LineWidth;
            int r = CanvasXSize / 2, x1 = CanvasXSize / 2, y1 = 0;
            while (x1 - CGD.Slider_LineMinLength < r && r < x1 + CGD.Slider_LineMinLength)
                r = rd.nextInt(CanvasXSize);
            new LinePosition(x1, y1, r, -Math.abs(x1 - r));
        }

        for (LinePosition lp : LinePosition.AllLine) {
            lp.LineDownMove(8);
            LineDraw(canvas, lp, pLine);

            float RX = (lp.s[0] + ((PlayerPosY - lp.s[1]) * (lp.s[0] - lp.e[0])) / (lp.s[1] - lp.e[1]));
            if (lp.e[1] < PlayerPosY && PlayerPosY < lp.s[1]) {
                if (RX - Math.sqrt(2.0) * CGD.Slider_LineWidth / 2 > PlayerPosX ||
                        PlayerPosX > RX + Math.sqrt(2.0) * CGD.Slider_LineWidth / 2) {
                    isEnd = true;
                }
            }
        }

        if (isEnd) return;

        canvas.drawCircle(PlayerPosX,
                CanvasYSize - CGD.Slider_LineWidth,
                CGD.Slider_LineWidth / 4,
                pCir);

        if (LinePosition.AllLine.size() > 0) {
            if (LinePosition.AllLine.get(0).e[1] > CanvasYSize) {
                LinePosition.AllLine.remove(0);
            }

            if (LinePosition.AllLine.get(LinePosition.AllLine.size() - 1).e[1] > 0) {
                float x1 = LinePosition.AllLine.get(LinePosition.AllLine.size() - 1).e[0];
                float y1 = LinePosition.AllLine.get(LinePosition.AllLine.size() - 1).e[1];
                //float r = x1;
                float r = x1 + 2 * CGD.Slider_LineMaxLength;
                //while (x1 - CGD.Game4_LineMinLength < r && r < x1 + CGD.Game4_LineMinLength)
                while (x1 - CGD.Slider_LineMaxLength > r || r > x1 + CGD.Slider_LineMaxLength)
                    r = rd.nextInt(CanvasXSize);
                new LinePosition(x1, y1, r, -Math.abs(x1 - r));
            }
        }
    }

    public void LineDraw(Canvas c, LinePosition lp, Paint p) {
        c.drawLine(lp.s[0],lp.s[1],lp.e[0],lp.e[1], p);
    }
}
