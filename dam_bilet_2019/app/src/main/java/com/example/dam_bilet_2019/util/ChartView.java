package com.example.dam_bilet_2019.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartView extends View {

    private List<String> labels;
    private List<Integer> data;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float WIDTH;
    float HEIGHT;
    float PADDING_WIDTH;
    float PADDING_HEIGHT;
    float AVAILABLE_WIDTH;
    float AVAILABLE_HEIGHT;
    float RECT_SIZE;
    int MAX_VAL;


    @Override
    public String toString() {
        return "ChartView{" +
                "labels=" + labels +
                ", data=" + data +
                ", paint=" + paint +
                ", WIDTH=" + WIDTH +
                ", HEIGHT=" + HEIGHT +
                ", PADDING_WIDTH=" + PADDING_WIDTH +
                ", PADDING_HEIGHT=" + PADDING_HEIGHT +
                ", AVAILABLE_WIDTH=" + AVAILABLE_WIDTH +
                ", AVAILABLE_HEIGHT=" + AVAILABLE_HEIGHT +
                ", RECT_SIZE=" + RECT_SIZE +
                ", MAX_VAL=" + MAX_VAL +
                '}';
    }


    public ChartView(Context context, Map<String, Integer> data) {
        super(context);
        this.data = new ArrayList<>(data.values());
        this.labels = new ArrayList<>(data.keySet());
    }

    private void init() {
        WIDTH = getWidth();
        HEIGHT = getHeight();
        PADDING_WIDTH = (float) (0.1 * getWidth());
        PADDING_HEIGHT = (float) (0.1 * getHeight());
        AVAILABLE_WIDTH = WIDTH - 2 * PADDING_WIDTH;
        AVAILABLE_HEIGHT = HEIGHT - 2 * PADDING_HEIGHT;

        if (this.data.size() > 0) {
            RECT_SIZE = AVAILABLE_WIDTH / this.data.size();
            MAX_VAL = this.data.get(0);
            for (int i : this.data) {
                if (MAX_VAL < i) {
                    MAX_VAL = i;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        // Draw axes
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth((float) (0.01 * WIDTH));
        // oX
        float x1 = PADDING_WIDTH;
        float y1 = PADDING_HEIGHT + AVAILABLE_HEIGHT;
        float x2 = PADDING_WIDTH + AVAILABLE_WIDTH;
        float y2 = PADDING_HEIGHT + AVAILABLE_HEIGHT;
        canvas.drawLine(x1, y1, x2, y2, paint);

        // oY
        x1 = PADDING_WIDTH;
        y1 = PADDING_HEIGHT;
        x2 = PADDING_WIDTH;
        y2 = PADDING_HEIGHT + AVAILABLE_HEIGHT;
        canvas.drawLine(x1, y1, x2, y2, paint);


        for (int i = 0; i < data.size(); i++) {
            float left = PADDING_WIDTH + i * RECT_SIZE;
            float top = (float) (PADDING_HEIGHT + (0.1 * AVAILABLE_HEIGHT) + (1 - (float) data.get(i) / MAX_VAL) * AVAILABLE_HEIGHT);
            float right = left + RECT_SIZE;
            float bottom = PADDING_HEIGHT + AVAILABLE_HEIGHT;

            paint.setColor(Color.BLUE);
            canvas.drawRect(left, top, right, bottom, paint);

            paint.setColor(Color.BLACK);
            float x = left + RECT_SIZE / 2;
            float y = bottom + PADDING_HEIGHT / 2;
            paint.setTextSize((float) (0.05 * WIDTH));
            canvas.drawText(labels.get(i) + "\n" + data.get(i), x, y, paint);
        }
    }
}
