package com.example.dam_bilet_2019.util;

import android.os.AsyncTask;

import com.example.dam_bilet_2019.db.model.Bilet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager extends AsyncTask<String, Void, List<Bilet>> {

    private URL url;
    private HttpURLConnection connection;
    private BufferedReader reader;

    @Override
    protected List<Bilet> doInBackground(String... strings) {
        List<Bilet> bilete = new ArrayList<>();
        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            JSONArray array = new JSONArray(json.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int numar = object.getInt("numar");
                String plecare = object.getString("plecare");
                String destinatie = object.getString("destinatie");
                boolean tarif = object.getBoolean("tarifRedus");
                double pret = object.getDouble("pret");

                bilete.add(new Bilet(numar, plecare, destinatie, tarif, pret));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bilete;
    }
}
