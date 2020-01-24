package com.example.dam_livrare_2020;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dam_livrare_2020.database.AppDatabase;
import com.example.dam_livrare_2020.database.model.Livrare;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IstoricActivity extends AppCompatActivity {

    public static String KEY_EDIT = "com.example.dam.edit";
    public static int REQUEST_EDIT = 100;

    private List<Livrare> livrari;
    private long idSelected;
    private int indexSelected;

    //UI Components
    private ListView lvLivrari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istoric);

        initComponents();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_OK && data != null) {
                Livrare livrare = data.getParcelableExtra(KEY_EDIT);
                livrare.setId(idSelected);
                livrari.set(indexSelected, livrare);
                updateList();

                // update in db
                new AsyncTask<Livrare, Void, Void>() {
                    @Override
                    protected Void doInBackground(Livrare... livrares) {
                        AppDatabase.getInstance(getApplicationContext()).livrareDao().update(livrares[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        // TODO: Remove this
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                    }
                }.execute(livrare);
            } else if (resultCode == AdaugaActivity.RESULT_DELETE) {
                // remove from db
                new AsyncTask<Livrare, Void, Void>() {
                    @Override
                    protected Void doInBackground(Livrare... livrares) {
                        AppDatabase.getInstance(getApplicationContext()).livrareDao().delete(livrares[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        // TODO: Remove this
                        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                    }
                }.execute(livrari.get(indexSelected));

                livrari.remove(indexSelected);
                updateList();
            }
        }
    }

    private void updateList() {
        ArrayAdapter adapter = (ArrayAdapter) lvLivrari.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void initComponents() {
        if (getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_LIVRARI) == null) {
            finish();
        } else {
            lvLivrari = findViewById(R.id.istoric_lv_livrari);

            livrari = getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_LIVRARI);
            // sortare dupa data
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (livrari != null) {
                    livrari.sort(new Comparator<Livrare>() {
                        @Override
                        public int compare(Livrare o1, Livrare o2) {
                            return o1.getData().compareTo(o2.getData());
                        }
                    });
                }
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, livrari);
            lvLivrari.setAdapter(adapter);

            lvLivrari.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    indexSelected = position;
                    idSelected = livrari.get(position).getId();

                    Intent intent = new Intent(IstoricActivity.this, AdaugaActivity.class);
                    intent.putExtra(KEY_EDIT, livrari.get(position));
                    startActivityForResult(intent, REQUEST_EDIT);
                }
            });
        }
    }
}
