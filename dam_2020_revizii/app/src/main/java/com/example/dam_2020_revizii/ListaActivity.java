package com.example.dam_2020_revizii;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dam_2020_revizii.model.Revizie;
import com.example.dam_2020_revizii.util.ReviziiAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    public static int REQUEST_EDIT_REVIZIE = 22;
    public static String KEY_EDIT_REVIZIE = "fuck";

    private List<Revizie> revizii;
    private int indexSelected;

    private List<Integer> indicesSelected = new ArrayList<>();

    // UI
    private EditText etFilter;
    private ListView lvRevizii;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        initComponents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_REVIZIE) {
            if (resultCode == RESULT_OK && data != null) {
                Revizie revizie = data.getParcelableExtra(KEY_EDIT_REVIZIE);
                revizii.set(indexSelected, revizie);
                updateList();
            } else if (resultCode == AdaugaActivity.RESULT_DELETE) {
                revizii.remove(indexSelected);
                updateList();
            }
        }
    }

    private void updateList() {
        ReviziiAdapter adapter = (ReviziiAdapter) lvRevizii.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void initComponents() {
        if (getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_REVIZII) != null) {
            revizii = getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_REVIZII);
        } else {
            finish();
        }

        etFilter = findViewById(R.id.lista_et_filter);
        lvRevizii = findViewById(R.id.lista_lv_revizii);
        btnSave = findViewById(R.id.lista_btn_save);

        ReviziiAdapter adapter = new ReviziiAdapter(this, revizii);
        lvRevizii.setAdapter(adapter);

        lvRevizii.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indexSelected = position;

                Intent intent = new Intent(ListaActivity.this, AdaugaActivity.class);
                intent.putExtra(KEY_EDIT_REVIZIE, revizii.get(position));
                startActivityForResult(intent, REQUEST_EDIT_REVIZIE);
            }
        });

        lvRevizii.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean is = false;
                for (int i = 0; i < indicesSelected.size(); i++) {
                    if (indicesSelected.get(i) == position) {
                        is = true;
                        break;
                    }
                }
                if (is) {
                    view.setBackgroundColor(getResources().getColor(android.R.color.white));
                    for (int i = 0; i < indicesSelected.size(); i++) {
                        if (indicesSelected.get(i) == position) {
                            indicesSelected.remove(i);
                            break;
                        }
                    }
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    indicesSelected.add(position);
                }
                return true;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(MainActivity.KEY_LISTA_REVIZII, (ArrayList<? extends Parcelable>) revizii);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterRevizii(s);
            }
        });
    }

    private void filterRevizii(Editable s) {
        String c = s.toString();

        ReviziiAdapter adapter = (ReviziiAdapter) lvRevizii.getAdapter();

        if (c.length() == 0 || c.equalsIgnoreCase(getString(R.string.tip_short_toate))) {
            adapter.getFilter().filter("");
        } else if (c.equalsIgnoreCase(getString(R.string.tip_short_normala))) {
            adapter.getFilter().filter(getString(R.string.tip_normala));
        } else if (c.equalsIgnoreCase(getString(R.string.tip_short_complexa))) {
            adapter.getFilter().filter(getString(R.string.tip_complexa));
        } else if (c.equalsIgnoreCase(getString(R.string.tip_short_medie))) {
            adapter.getFilter().filter(getString(R.string.tip_medie));
        }
    }
}
