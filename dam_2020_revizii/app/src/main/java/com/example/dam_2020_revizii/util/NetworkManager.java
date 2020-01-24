package com.example.dam_2020_revizii.util;

import android.os.AsyncTask;

import com.example.dam_2020_revizii.MainActivity;
import com.example.dam_2020_revizii.model.Revizie;
import com.example.dam_2020_revizii.model.TipRevizie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager extends AsyncTask<String, Void, List<Revizie>> {
    private URL url;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    @Override
    protected List<Revizie> doInBackground(String... strings) {
        List<Revizie> result = new ArrayList<>();
        try {
            StringBuilder json = new StringBuilder();

            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }

            JSONArray array = new JSONArray(json.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String nrAuto = object.getString("numarAuto");
                int nrKm = object.getInt("numarKm");
                Date data = MainActivity.dateFormat.parse(object.getString("data"));
                double cost = object.getDouble("cost");
                TipRevizie tip = TipRevizie.valueOf(object.getString("tip").toUpperCase());

                result.add(new Revizie(nrAuto, nrKm, data, cost, tip));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
