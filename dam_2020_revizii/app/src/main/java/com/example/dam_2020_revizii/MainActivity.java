package com.example.dam_2020_revizii;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dam_2020_revizii.model.Revizie;
import com.example.dam_2020_revizii.model.TipRevizie;
import com.example.dam_2020_revizii.util.NetworkManager;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    public static String KEY_REVIZIE = "com.example.dam_2020_revizii.key_revizie";
    public static String KEY_LISTA_REVIZII = "com.example.dam_2020_revizii.key_lista_revizii";
    public static int REQUEST_ADD_REVIZIE = 101;
    public static int REQUEST_SHOW_REVIZII = 999;

    private static String API_URL = "https://api.jsonbin.io/b/5e2b502350a7fe418c532815";

    private List<Revizie> revizii = new ArrayList<>();

    // UI
    private Button btnAdauga;
    private Button btnLista;
    private Button btnSincronizare;
    private Button btnGrafic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Remove this
        try {
            revizii.add(new Revizie("DB-39-VLD", 150000, dateFormat.parse("20-8-2020"), 199.99, TipRevizie.MEDIE));
            revizii.add(new Revizie("B-11-AGK", 46789, dateFormat.parse("7-3-2020"), 959.99, TipRevizie.COMPLEXA));
            revizii.add(new Revizie("CT-67-CET", 35900, dateFormat.parse("15-2-2020"), 199.99, TipRevizie.NORMALA));
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();

        greedingMessage();
    }

    private void greedingMessage() {
        StringBuilder msg = new StringBuilder(getString(R.string.main_dialog_message));
        msg.append("\n").append(dateFormat.format(new Date(System.currentTimeMillis())));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.main_dialog_title));
        builder.setMessage(msg.toString());
        builder.setPositiveButton(getString(R.string.main_dialog_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_REVIZIE) {
            if (resultCode == RESULT_OK && data != null) {
                Revizie revizie = (Revizie) data.getParcelableExtra(KEY_REVIZIE);
                revizii.add(revizie);
            }
        } else if (requestCode == REQUEST_SHOW_REVIZII) {
            if (resultCode == RESULT_OK && data != null) {
                revizii = data.getParcelableArrayListExtra(KEY_LISTA_REVIZII);
                // TODO: Remove this
                Toast.makeText(getApplicationContext(), "Modificari salvate", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initComponents() {
        btnAdauga = findViewById(R.id.main_btn_adauga);
        btnLista = findViewById(R.id.main_btn_lista);
        btnSincronizare = findViewById(R.id.main_btn_sincronizare);
        btnGrafic = findViewById(R.id.main_btn_grafic);

        btnAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdaugaActivity.class);
                startActivityForResult(intent, REQUEST_ADD_REVIZIE);
            }
        });

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaActivity.class);
                intent.putParcelableArrayListExtra(KEY_LISTA_REVIZII, (ArrayList<? extends Parcelable>) revizii);
                startActivityForResult(intent, REQUEST_SHOW_REVIZII);
            }
        });

        btnGrafic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraficActivity.class);
                intent.putParcelableArrayListExtra(KEY_LISTA_REVIZII, (ArrayList<? extends Parcelable>) revizii);
                startActivity(intent);
            }
        });

        btnSincronizare.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new NetworkManager() {
                    @Override
                    protected void onPostExecute(List<Revizie> revizies) {
                        revizii.addAll(revizies);
                        Toast.makeText(getApplicationContext(), "Synced", Toast.LENGTH_LONG).show();
                    }
                }.execute(API_URL);
            }
        });
    }
}
