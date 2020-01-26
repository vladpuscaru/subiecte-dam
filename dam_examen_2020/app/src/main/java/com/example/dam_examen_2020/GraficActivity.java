package com.example.dam_examen_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.dam_examen_2020.db.model.Examen;
import com.example.dam_examen_2020.util.ChartView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraficActivity extends AppCompatActivity {

    private ChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_EXAMEN) != null) {
            List<Examen> examene = getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_EXAMEN);

            Map<String, Integer> data = new HashMap<>();
            for (Examen e : examene) {
                boolean is = false;
                for (String m : data.keySet()) {
                    if (m.equalsIgnoreCase(e.getDenumireMaterie())) {
                        is = true;
                    }
                }

                if (!is) {
                    data.put(e.getDenumireMaterie(), e.getNumarStudenti());
                } else {
                    int x = data.get(e.getDenumireMaterie()) + e.getNumarStudenti();
                    data.remove(e.getDenumireMaterie());
                    data.put(e.getDenumireMaterie(), x);
                }
            }


            chartView = new ChartView(this, data);

            setContentView(chartView);
        } else {
            setContentView(R.layout.activity_grafic);
        }
    }
}
