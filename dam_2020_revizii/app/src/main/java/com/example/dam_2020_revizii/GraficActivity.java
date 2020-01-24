package com.example.dam_2020_revizii;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.dam_2020_revizii.model.Revizie;
import com.example.dam_2020_revizii.model.TipRevizie;
import com.example.dam_2020_revizii.util.BarChartView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraficActivity extends AppCompatActivity {

    private BarChartView barChart;
    private List<Revizie> revizii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<String, Double> source = new HashMap<>();

        if (getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_REVIZII) != null) {
            revizii = getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_REVIZII);

            // Calcul costuri pt fiecare tip de revizie
            double costN = 0.0;
            double costC = 0.0;
            double costM = 0.0;

            for (Revizie r : revizii) {
                if (r.getTip().toString().equalsIgnoreCase(TipRevizie.NORMALA.toString())) {
                    costN += r.getCost();
                } else if (r.getTip().toString().equalsIgnoreCase(TipRevizie.COMPLEXA.toString())) {
                    costC += r.getCost();
                } else if (r.getTip().toString().equalsIgnoreCase(TipRevizie.MEDIE.toString())) {
                    costM += r.getCost();
                }
            }

            source.put(TipRevizie.NORMALA.toString(), costN);
            source.put(TipRevizie.COMPLEXA.toString(), costC);
            source.put(TipRevizie.MEDIE.toString(), costM);

            barChart = new BarChartView(this, source);

            setContentView(barChart);
        }
    }


}
