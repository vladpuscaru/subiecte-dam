package com.example.dam_bilet_2019;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dam_bilet_2019.db.AppDatabase;
import com.example.dam_bilet_2019.db.model.Bilet;
import com.example.dam_bilet_2019.util.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_BILET = "com.example.dam_bilet_2019.bilet";
    public static final String KEY_LISTA_BILETE = "com.example.dam_bilet_2019.lista_bilete";
    public static final int REQUEST_ADD_BILET = 23;
    public static final int REQUEST_SHOW_BILETE = 66;

    private static final String API_URL = "https://api.jsonbin.io/b/5e2dbe2d3d75894195df50ec";

    private List<Bilet> bilete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList();
    }

    @SuppressLint("StaticFieldLeak")
    private void initList() {
        new AsyncTask<Void, Void, List<Bilet>>() {
            @Override
            protected List<Bilet> doInBackground(Void... voids) {
                return AppDatabase.getInstance(getApplicationContext()).biletDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Bilet> bilets) {
                bilete = bilets;
                Toast.makeText(getApplicationContext(), "Query done", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_item_eliberare:
                intent = new Intent(this, AdaugaActivity.class);
                startActivityForResult(intent, REQUEST_ADD_BILET);
                break;
            case R.id.menu_item_lista:
                intent = new Intent(this, ListaActivity.class);
                intent.putParcelableArrayListExtra(KEY_LISTA_BILETE, (ArrayList<? extends Parcelable>) bilete);
                startActivityForResult(intent, REQUEST_SHOW_BILETE);
                break;
            case R.id.menu_item_json:
                new NetworkManager() {
                    @Override
                    protected void onPostExecute(List<Bilet> bilets) {
                        bilete.addAll(bilets);
                        Toast.makeText(getApplicationContext(), "Synced", Toast.LENGTH_SHORT).show();
                    }
                }.execute(API_URL);
                break;
            case R.id.menu_item_raport:
                intent = new Intent(this, RaportActivity.class);
                intent.putParcelableArrayListExtra(KEY_LISTA_BILETE, (ArrayList<? extends Parcelable>) bilete);
                startActivity(intent);
                break;
            case R.id.menu_item_despre:
                Toast.makeText(getApplicationContext(), getString(R.string.dev_name), Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_BILET) {
            if (resultCode == RESULT_OK && data != null) {
                Bilet bilet = data.getParcelableExtra(KEY_BILET);
                bilete.add(bilet);
            }
        } else if (requestCode == REQUEST_SHOW_BILETE) {
            if (resultCode == RESULT_OK && data != null) {
                bilete = data.getParcelableArrayListExtra(KEY_LISTA_BILETE);
            }
        }
    }
}
