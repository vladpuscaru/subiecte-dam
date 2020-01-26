package com.example.dam_examen_2020.util;

import android.os.AsyncTask;

import com.example.dam_examen_2020.db.model.Examen;

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

public class NetworkManager extends AsyncTask<String, Void, List<Examen>> {

    private URL url;
    private HttpURLConnection connection;
    private BufferedReader reader;

    @Override
    protected List<Examen> doInBackground(String... strings) {
        List<Examen> result = new ArrayList<>();
        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            JSONArray array = new JSONObject(json.toString()).getJSONArray("examene");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                long id = Long.parseLong(object.getString("id"));
                String materie = object.getString("denumireMaterie");
                int studenti = Integer.parseInt(object.getString("numarStudenti"));
                String sala = object.getString("sala");
                String supraveghetor = object.getString("supraveghetor");

                result.add(new Examen(id, materie, studenti, sala, supraveghetor));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
