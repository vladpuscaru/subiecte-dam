package com.example.dam_bilet_2019;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dam_bilet_2019.db.model.Bilet;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    public static final String KEY_EDIT_BILET = "key_edit_bilet";
    public static final int REQUEST_EDIT_BILET = 123;

    private List<Bilet> bilete;
    private int indexSelected;
    private long idSelected;

    // UI
    private ListView lvBilete;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        if (getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_BILETE) != null) {
            this.bilete = getIntent().getParcelableArrayListExtra(MainActivity.KEY_LISTA_BILETE);
            initComponents();
        }

    }

    private void initComponents() {
        lvBilete = findViewById(R.id.lista_lv_bilete);
        btnSave = findViewById(R.id.lista_btn_save);

        ArrayAdapter<Bilet> adapter = new ArrayAdapter<Bilet>(this, android.R.layout.simple_list_item_1, bilete);
        lvBilete.setAdapter(adapter);

        lvBilete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indexSelected = position;
                Intent intent = new Intent(ListaActivity.this, AdaugaActivity.class);
                intent.putExtra(KEY_EDIT_BILET, bilete.get(position));
                startActivityForResult(intent, REQUEST_EDIT_BILET);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(MainActivity.KEY_LISTA_BILETE, (ArrayList<? extends Parcelable>) bilete);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_BILET) {
            if (resultCode == RESULT_OK && data != null) {
                bilete.set(indexSelected, (Bilet) data.getParcelableExtra(KEY_EDIT_BILET));
                updateList();
            }
        }
    }

    private void updateList() {
        ArrayAdapter adapter = (ArrayAdapter) lvBilete.getAdapter();
        adapter.notifyDataSetChanged();
    }
}
