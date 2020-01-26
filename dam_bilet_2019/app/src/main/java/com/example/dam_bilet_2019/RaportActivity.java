package com.example.dam_bilet_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.dam_bilet_2019.db.model.Bilet;
import com.example.dam_bilet_2019.util.ChartView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaportActivity extends AppCompatActivity {

    private ChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_BILETE) != null) {
            List<Bilet> bilete = getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_BILETE);
            Map<String, Integer> data = new HashMap<>();
            assert bilete != null;
            for (Bilet b : bilete) {
                if (data.containsKey(b.getDestinatie())) {
                    data.put(b.getDestinatie(), data.get(b.getDestinatie()) + 1);
                } else {
                    data.put(b.getDestinatie(), 1);
                }
            }
            Log.d("WTF", data.toString());
            chartView = new ChartView(this, data);
            setContentView(chartView);
        } else {
            setContentView(R.layout.activity_raport);
        }
    }
}
