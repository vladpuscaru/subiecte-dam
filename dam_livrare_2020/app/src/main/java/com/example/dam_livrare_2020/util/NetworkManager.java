package com.example.dam_livrare_2020.util;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.dam_livrare_2020.MainActivity;
import com.example.dam_livrare_2020.database.model.Livrare;
import com.example.dam_livrare_2020.database.model.TipLivrare;

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

import javax.net.ssl.HttpsURLConnection;

public class NetworkManager extends AsyncTask<String, Void, List<Livrare>> {
    private URL url;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    @Override
    protected List<Livrare> doInBackground(String... strings) {
        List<Livrare> livrari = new ArrayList<>();

        try {
            StringBuilder result = new StringBuilder();
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            // parse JSON
            JSONArray array = new JSONArray(result.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String destinatar = object.getString("destinatar");
                String adresa = object.getString("adresa");
                Date data = MainActivity.dateFormat.parse(object.getString("data"));
                float valoare = Float.parseFloat(object.getString("valoare"));
                TipLivrare tip = TipLivrare.valueOf(object.getString("tip").toUpperCase());

                livrari.add(
                        new Livrare(destinatar, adresa, data, valoare, tip)
                );
            }

        } catch (Exception e) {
//            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return livrari;
    }
}
