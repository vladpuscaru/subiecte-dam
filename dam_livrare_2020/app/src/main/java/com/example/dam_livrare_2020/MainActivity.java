package com.example.dam_livrare_2020;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dam_livrare_2020.database.AppDatabase;
import com.example.dam_livrare_2020.database.model.Livrare;
import com.example.dam_livrare_2020.util.NetworkManager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static String API_URL = "https://api.jsonbin.io/b/5e2af7ec3d75894195de1dee";

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    public static SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss", Locale.US);

    public static String KEY_LIVRARE = "com.example.dam.key_livrare";
    public static String KEY_LISTA_LIVRARI = "com.example.dam.key_lista_livrari";

    public static int REQUEST_LIVRARE_ADD = 55;

    private List<Livrare> livrari = new ArrayList<>();

    // UI components
    private TextView tvStatic;
    private Button btnAdauga;
    private Button btnIstoric;
    private Button btnImport;
    private Button btnDespre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initList();
    }

    @SuppressLint("StaticFieldLeak")
    private void initList() {
        new AsyncTask<Void, Void, List<Livrare>>() {
            @Override
            protected List<Livrare> doInBackground(Void... voids) {
                return AppDatabase.getInstance(getApplicationContext()).livrareDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Livrare> livrares) {
                livrari = livrares;
                // TODO: Remove this
                Toast.makeText(getApplicationContext(), "Got from DB", Toast.LENGTH_LONG).show();
            }
        }.execute();
    }


    private void initComponents() {
        tvStatic = findViewById(R.id.main_tv_static);
        btnAdauga = findViewById(R.id.main_btn_adauga);
        btnIstoric = findViewById(R.id.main_btn_istoric);
        btnImport = findViewById(R.id.main_btn_import);
        btnDespre = findViewById(R.id.main_btn_despre);

        tvStatic.setText(tvStatic.getText().toString() + " " + hourFormat.format(new Date(System.currentTimeMillis())));

        btnDespre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.main_despre), Toast.LENGTH_LONG).show();
            }
        });

        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdaugaActivity.class);
                startActivityForResult(intent, REQUEST_LIVRARE_ADD);
            }
        });

        btnIstoric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IstoricActivity.class);
                intent.putParcelableArrayListExtra(KEY_LISTA_LIVRARI, (ArrayList<? extends Parcelable>) livrari);
                startActivity(intent);
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new NetworkManager() {
                    @Override
                    protected void onPostExecute(List<Livrare> livrares) {
                        livrari.addAll(livrares);
                        Toast.makeText(getApplicationContext(), "Imported", Toast.LENGTH_SHORT).show();
                        Log.d("WTF", livrari.toString());
                    }
                }.execute(API_URL);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIVRARE_ADD) {
            if (resultCode == RESULT_OK && data != null) {
                final Livrare livrare = (Livrare) data.getParcelableExtra(KEY_LIVRARE);
                livrari.add(livrare);
            }
        }
    }

}
