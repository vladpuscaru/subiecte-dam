package com.example.dam_examen_2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dam_examen_2020.db.AppDatabase;
import com.example.dam_examen_2020.db.model.Examen;
import com.example.dam_examen_2020.util.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String API_URL = "https://api.jsonbin.io/b/5e2c7253593fd741857030e6";

    public static String KEY_EXAMEN = "com.example.dam_examen_2020.examen";
    public static String KEY_LISTA_EXAMEN = "com.example.dam_examen_2020.lista_examen";
    private static int REQUEST_ADD_EXAMEN = 55;
    private static int REQUEST_EDIT_EXAMEN = 123;

    private List<Examen> examene = new ArrayList<>();
    private int indexSelected;

    private SharedPreferences sharedPreferences;

    // UI
    private ListView lvExamene;
    private Button btnSave;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: remove this
        examene.add(new Examen(0, "DAM", 189, "Sala pulii", "Al. Dita"));
        examene.add(new Examen(1, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));
        examene.add(new Examen(2, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));
        examene.add(new Examen(3, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));
        examene.add(new Examen(4, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));
        examene.add(new Examen(5, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));
        examene.add(new Examen(6, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));
        examene.add(new Examen(7, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));
        examene.add(new Examen(8, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));
        examene.add(new Examen(9, "Teh. Web", 152, "Sala pizdii", "Super Awsome Guy"));

        initComponents();
    }

    private void initComponents() {
        lvExamene = findViewById(R.id.main_lv_examene);
        btnSave = findViewById(R.id.main_btn_save);
        pb = findViewById(R.id.main_pb);

        ArrayAdapter<Examen> adapter = new ArrayAdapter<Examen>(this, android.R.layout.simple_list_item_1, examene);
        lvExamene.setAdapter(adapter);

        lvExamene.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indexSelected = position;
                Intent intent = new Intent(MainActivity.this, AdaugaActivity.class);
                intent.putExtra(KEY_EXAMEN, examene.get(position));
                startActivityForResult(intent, REQUEST_EDIT_EXAMEN);
            }
        });

        lvExamene.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.main_dialog_title));
                builder.setMessage(getString(R.string.main_dialog_message));
                builder.setPositiveButton(getString(R.string.main_dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        examene.remove(position);
                        updateList();
                    }
                });
                builder.setNegativeButton(getString(R.string.main_dialog_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                new AsyncTask<List<Examen>, Integer, Void>() {
                    @Override
                    protected Void doInBackground(List<Examen>... lists) {
                        for (int i = 0; i < examene.size(); i++) {
                            AppDatabase.getInstance(getApplicationContext()).examenDao().insert(examene.get(i));
                            int progress = (int)( ((float) (i + 1) / examene.size()) * 100 );
                            publishProgress(progress);
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        pb.setProgress(values[0]);
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        //TODO: remove Toast
                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.GONE);
                    }
                }.execute(examene);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EXAMEN) {
            if (resultCode == RESULT_OK && data != null) {
                Examen examen = data.getParcelableExtra(KEY_EXAMEN);
                examene.add(examen);
                updateList();
            }
        } else if (requestCode == REQUEST_EDIT_EXAMEN) {
            if (resultCode == RESULT_OK && data != null) {
                Examen examen = data.getParcelableExtra(KEY_EXAMEN);
                examene.set(indexSelected, examen);
                updateList();
            }
        }
    }

    private void updateList() {
        ArrayAdapter adapter = (ArrayAdapter) lvExamene.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.menu_item_adauga:
                intent = new Intent(MainActivity.this, AdaugaActivity.class);
                startActivityForResult(intent, REQUEST_ADD_EXAMEN);
                break;
            case R.id.menu_item_sincronizare:
                new NetworkManager() {
                    @Override
                    protected void onPostExecute(List<Examen> examen) {
                        examene.addAll(examen);
                        updateList();
                        Toast.makeText(getApplicationContext(), "Synced", Toast.LENGTH_SHORT).show();
                    }
                }.execute(API_URL);
                break;
            case R.id.menu_item_grafic:
                intent = new Intent(MainActivity.this, GraficActivity.class);
                intent.putParcelableArrayListExtra(KEY_LISTA_EXAMEN, (ArrayList<? extends Parcelable>) examene);
                startActivity(intent);
                break;
        }

        return true;
    }
}
