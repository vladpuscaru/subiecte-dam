package com.example.dam_c_2020_1.util;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dam_c_2020_1.db.model.Categorie;
import com.example.dam_c_2020_1.db.model.Citat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager extends AsyncTask<String, Void, List<Citat>> {

    // Connection fields
    private URL url;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;


    private List<Citat> parseJson(String json) {
        if (json == null)
            return null;

        List<Citat> citate = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                citate.add(getCitat(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return citate;
    }

    private Citat getCitat(JSONObject jsonObject) throws JSONException {
        String autor = jsonObject.getString("autor");
        String text = jsonObject.getString("text");
        int aprecieri = jsonObject.getInt("numarAprecieri");
        Categorie categorie = Categorie.valueOf(jsonObject.getString("categorie").toUpperCase());

        return new Citat(autor, text, aprecieri, categorie);
    }

    @Override
    protected List<Citat> doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();

        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            Log.d("WTF", result.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseJson(result.toString());

    }
}
