package com.example.dam_2020_revizii.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BarChartView extends View {
    private Map<String, Double> source;
    private Paint paint;
    private List<String> labels;


    public BarChartView(Context context, Map<String, Double> source) {
        super(context);
        this.source = source;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labels = new ArrayList<>(source.keySet());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (source != null && source.size() > 0) {
            paint.setStrokeWidth((float) (getHeight() * 0.01));
            paint.setColor(Color.BLACK);

            float paddingW = (float) (getWidth() * 0.1);
            float paddingH = (float) (getHeight() * 0.1);

            float availableWidth = getWidth() - 2 * paddingW;
            float availableHeight = getHeight() - 2 * paddingH;

            // Draw oY
            canvas.drawLine(paddingW, paddingH, paddingW, paddingH + availableHeight, paint);

            // Draw oX
            canvas.drawLine(paddingW, paddingH + availableHeight, paddingW + availableWidth, paddingH + availableHeight, paint);

            // Valoare maxima
            Object[] array = source.values().toArray();
            double maxVal;
            if (array.length > 0) {
                maxVal = (double) array[0];
                for (int i = 0; i < array.length; i++) {
                    if ((double) array[i] > maxVal) {
                        maxVal = (double) array[i];
                    }
                }

                // Grosimea unei bari
                float widthOfElement = availableWidth / source.size();

                for (int i = 0; i < labels.size(); i++) {
                    paint.setColor(Color.BLUE);
                    float x1 = paddingW + i * widthOfElement;
                    // TODO: think about this
                    float y1 = (float) ((1 - source.get(labels.get(i)) / maxVal) * availableHeight + paddingH);

                    float x2 = x1 + widthOfElement;
                    float y2 = paddingH + availableHeight;

                    canvas.drawRect(x1, y1, x2, y2, paint);
                    drawLabel(canvas, x1, widthOfElement, paddingH, availableHeight, labels.get(i));
                }
            }

        }
    }

    private void drawLabel(Canvas canvas, float x1, float widthOfElement, float paddingH, float availableHeight, String s) {
        paint.setColor(Color.BLACK);
        paint.setTextSize((float) (0.3 * widthOfElement));
        float x = x1 + widthOfElement / 2;
        float y = 1 / 2 * paddingH + availableHeight;
        canvas.rotate(270, x, y);
        canvas.drawText(s + " - " + source.get(s), x, y, paint);
        canvas.rotate(-270, x, y);
    }

}
