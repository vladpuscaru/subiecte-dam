package com.example.dam_examen_2020.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Map<String, Integer> data;
    private List<String> labels = new ArrayList<>();

    public ChartView(Context context, Map<String, Integer> data) {
        super(context);
        this.data = data;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int HEIGHT = getHeight();
        int WIDTH = getWidth();
        int pWIDTH = (int) (0.1 * WIDTH);
        int pHEIGHT = (int) (0.1 * HEIGHT);
        int aWidth = WIDTH - 2 * pWIDTH;
        int aHeight = HEIGHT - 2 * pHEIGHT;

        // Draw oX & oY
        paint.setStrokeWidth((int) (0.01 * WIDTH));
        paint.setColor(Color.BLACK);

        // oY
        int x1 = pWIDTH;
        int y1 = pHEIGHT;
        int x2 = x1;
        int y2 = pHEIGHT + aHeight;

        canvas.drawLine(x1, y1, x2, y2, paint);

        // oX
        x1 = pWIDTH;
        y1 = pHEIGHT + aHeight;
        x2 = pWIDTH + aWidth;
        y2 = y1;

        canvas.drawLine(x1, y1, x2, y2, paint);


        // Draw values
        if (data != null && data.size() != 0) {
            int MAX = (int) data.values().toArray()[0];
            for (int v : data.values()) {
                if (MAX < v) {
                    MAX = v;
                }
            }

            float SIZE = aWidth / data.size();

            labels.addAll(data.keySet());

            for (int i = 0; i < data.values().size(); i++) {
                paint.setColor(Color.BLUE);

                float left = pWIDTH + (i * SIZE);
                float top = pHEIGHT + (1 - data.get(labels.get(i)) / MAX) * aHeight;
                float right = left + SIZE;
                float bottom = pHEIGHT + aHeight;

                canvas.drawRect(left, top, right, bottom, paint);

                // draw value on oY
                paint.setColor(Color.BLACK);
                paint.setTextSize((float)(0.1 * SIZE));

                float x = left + SIZE / 2;
                float y = top - 5;
                canvas.drawText(data.get(labels.get(i)).toString(), x, y, paint);

                // draw label on oX
                float xLabel = left + SIZE / 2;
                float yLabel = pHEIGHT + aHeight + (float)pHEIGHT / 2;
                paint.setColor(Color.BLUE);
                canvas.drawText(labels.get(i), xLabel, yLabel, paint);
            }
        }
    }
}
