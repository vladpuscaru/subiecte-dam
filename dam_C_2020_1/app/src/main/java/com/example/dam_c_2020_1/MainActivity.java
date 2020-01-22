package com.example.dam_c_2020_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dam_c_2020_1.db.Database;
import com.example.dam_c_2020_1.db.model.Citat;
import com.example.dam_c_2020_1.util.CitateAdapter;
import com.example.dam_c_2020_1.util.NetworkManager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static String API_URL = "https://api.jsonbin.io/b/5e288eb54f60034b5f22bb6b";

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    public static int REQUEST_ADD_CITAT = 5;
    public static int REQUEST_EDIT_CITAT = 7;
    public static String KEY_CITAT = "com.example.dam_c_2020_1.key.citat";


    private List<Citat> citate = new ArrayList<>();
    private int indexSelected;
    private long idSelected;

    // UI Components
    private ListView lvCitate;
    private EditText etFilter;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        
        initList();
        
        startAppMessage();
    }

    @SuppressLint("StaticFieldLeak")
    private void initList() {
        new AsyncTask<Void, Void, List<Citat>>() {
            @Override
            protected List<Citat> doInBackground(Void... voids) {
                return Database.getInstance(getApplicationContext()).citatDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Citat> citats) {
                citate.addAll(citats);
                updateList();
            }
        }.execute();
    }

    private void initComponents() {
        lvCitate = findViewById(R.id.main_lv_citate);
        etFilter = findViewById(R.id.main_et_filter);
        pb = findViewById(R.id.main_pb);

        CitateAdapter adapter = new CitateAdapter(getApplicationContext(), citate);
        lvCitate.setAdapter(adapter);

        lvCitate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AdaugaActivity.class);
                intent.putExtra(KEY_CITAT, citate.get(position));
                startActivityForResult(intent, REQUEST_EDIT_CITAT);

                indexSelected = position;
                idSelected = citate.get(position).getId();
            }
        });

        lvCitate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AsyncTask<Citat, Void, Long>() {
                    @Override
                    protected Long doInBackground(Citat... citats) {
                        long x = 0;
                        try {
                            x = Database.getInstance(getApplicationContext()).citatDao().insert(citats[0]);
                        } catch (Exception e) {
                            return -1L;
                        }
                        return x;
                    }

                    @Override
                    protected void onPostExecute(Long integer) {
                        if (integer > 0) {
                            // TODO: Test only. Remove this.
                            Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute(citate.get(position));

                return true;
            }
        });

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing to do here
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterResults(s.toString());
            }
        });
    }

    private void filterResults(String s) {
        CitateAdapter adapter = (CitateAdapter) lvCitate.getAdapter();
        if (s.length() == 0) {
            adapter.getFilter().filter("");
        } else if (s.equalsIgnoreCase(getString(R.string.main_filter_T))) {
            adapter.getFilter().filter(getString(R.string.main_cat_All));
        } else if (s.equalsIgnoreCase(getString(R.string.main_filter_Fa))) {
            adapter.getFilter().filter(getString(R.string.main_cat_Familie));
        } else if (s.equalsIgnoreCase(getString(R.string.main_filter_S))) {
            adapter.getFilter().filter(getString(R.string.main_cat_Sport));
        } else if (s.equalsIgnoreCase(getString(R.string.main_filter_Fi))) {
            adapter.getFilter().filter(getString(R.string.main_cat_Filozofie));
        } else if (s.equalsIgnoreCase(getString(R.string.main_filter_M))) {
            adapter.getFilter().filter(getString(R.string.main_cat_Motivationale));
        } else {
            adapter.getFilter().filter(getString(R.string.main_cat_None));
        }
    }

    private void startAppMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


        builder.setTitle(getString(R.string.main_dialog_title));
        builder.setMessage(getString(R.string.main_dialog_message) + "\n" + dateFormat.format(new Date(System.currentTimeMillis())));

        builder.setPositiveButton(getString(R.string.main_dialog_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ADD_CITAT) {
            if (resultCode == RESULT_OK && data != null) {
                Citat citat = (Citat) data.getSerializableExtra(KEY_CITAT);
                citate.add(citat);
                updateList();
            }
        } else if (requestCode == REQUEST_EDIT_CITAT) {
            if (resultCode == RESULT_OK && data != null) {
                etFilter.setText("");
                filterResults("");

                Citat citat = (Citat) data.getSerializableExtra(KEY_CITAT);
                citat.setId(idSelected);

                new AsyncTask<Citat, Void, Void>() {

                    @Override
                    protected Void doInBackground(Citat... citats) {
                        Database.getInstance(getApplicationContext()).citatDao().update(citats[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {

                        // TODO: Test only. Remove this.
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_LONG).show();
                    }
                }.execute(citat);

                citate.set(indexSelected, citat);
                updateList();

                Log.d("WTF", citat.toString());

            } else if (resultCode == AdaugaActivity.RESULT_DELETE && data != null) {
                new AsyncTask<Citat, Void, Void>() {
                    @Override
                    protected Void doInBackground(Citat... citats) {
                        Database.getInstance(getApplicationContext()).citatDao().delete(citats[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        // TODO: Test only. Remove this.
                        Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
                    }
                }.execute(citate.get(indexSelected));

                citate.remove(indexSelected);

                updateList();
            }
        }
    }

    private void updateList() {
        ArrayAdapter adapter = (ArrayAdapter) lvCitate.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        Intent intent;

        switch (item.getItemId()) {
            case R.id.menu_item_adauga:
                intent = new Intent(this, AdaugaActivity.class);
                startActivityForResult(intent, REQUEST_ADD_CITAT);
                break;
            case R.id.menu_item_sincronizare:
                pb.setVisibility(View.VISIBLE);
                new NetworkManager() {
                    @Override
                    protected void onPostExecute(List<Citat> citats) {
                        citate.addAll(citats);
                        updateList();
                        pb.setVisibility(View.GONE);
                    }
                }.execute(API_URL);
                break;
        }

        return true;
    }
}
