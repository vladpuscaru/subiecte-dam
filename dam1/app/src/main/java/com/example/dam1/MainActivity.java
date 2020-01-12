package com.example.dam1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.dam1.database.AppDatabase;
import com.example.dam1.model.Jucator;
import com.example.dam1.util.JucatoriAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    public static String ADAUGA_JUCATOR_KEY = "com.example.dam1.adauga.jucator";
    public static String EDIT_JUCATOR_KEY = "com.example.dam1.edit.jucator";
    public String LIST_JUCATORI_KEY = "com.example.dam1.lista.jucatori";
    public static int ADAUGA_JUCATOR_REQUEST = 22;
    public static int EDIT_JUCATOR_REQUEST = 88;

    private List<Jucator> jucatori = new ArrayList<>();
    private int pozitieSelectata = 0;

    // UI components
    private ListView lvJucatori;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            jucatori = savedInstanceState.getParcelableArrayList(LIST_JUCATORI_KEY);
        }

        initComponents();
    }

    private void initComponents() {
        lvJucatori = findViewById(R.id.main_lv_jucatori);
        btnSave = findViewById(R.id.main_btn_save);

        JucatoriAdapter adapter = new JucatoriAdapter(getApplicationContext(), jucatori);
        lvJucatori.setAdapter(adapter);

        lvJucatori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AdaugaJucatorActivity.class);
                intent.putExtra(EDIT_JUCATOR_KEY, jucatori.get(position));
                pozitieSelectata = position;
                startActivityForResult(intent, EDIT_JUCATOR_REQUEST);
            }
        });

        lvJucatori.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setTitle(getString(R.string.main_dialog_title));

                alert.setMessage(getString(R.string.main_dialog_message));

                alert.setPositiveButton(getString(R.string.main_dialog_option_1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jucatori.remove(position);
                        updateList();
                        dialog.cancel();
                    }
                });

                alert.setNegativeButton(getString(R.string.main_dialog_option_2), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alert.show();

                return true;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<List<Jucator>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<Jucator>... lists) {
                        AppDatabase.getInstance(getApplicationContext()).jucatorDao().insertAll(lists[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        new AsyncTask<Void, Void, List<Jucator>>() {
                            @Override
                            protected List<Jucator> doInBackground(Void... voids) {
                                return AppDatabase.getInstance(getApplicationContext()).jucatorDao().getAll();
                            }

                            @Override
                            protected void onPostExecute(List<Jucator> jucators) {
                                Log.d("WTF", jucators.toString());
                            }
                        }.execute();
                    }
                }.execute(jucatori);
            }
        });
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(LIST_JUCATORI_KEY, (ArrayList<? extends Parcelable>) jucatori);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_adauga:
                intent = new Intent(getApplicationContext(), AdaugaJucatorActivity.class);
                startActivityForResult(intent, ADAUGA_JUCATOR_REQUEST);
                break;
            case R.id.nav_sincronizare:
                break;
            case R.id.nav_grafic:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADAUGA_JUCATOR_REQUEST) {
                if (data != null) {
                    Jucator jucator = data.getParcelableExtra(ADAUGA_JUCATOR_KEY);
                    jucatori.add(jucator);
                    updateList();
                }
            } else if (requestCode == EDIT_JUCATOR_REQUEST) {
                if (data != null) {
                    Jucator jucator = data.getParcelableExtra(EDIT_JUCATOR_KEY);
                    jucatori.set(pozitieSelectata, jucator);
                    updateList();
                }
            }
        }
    }

    private void updateList() {
        JucatoriAdapter adapter = (JucatoriAdapter) lvJucatori.getAdapter();
        adapter.notifyDataSetChanged();
    }
}
