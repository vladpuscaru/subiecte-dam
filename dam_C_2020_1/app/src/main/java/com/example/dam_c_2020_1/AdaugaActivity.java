package com.example.dam_c_2020_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dam_c_2020_1.db.model.Categorie;
import com.example.dam_c_2020_1.db.model.Citat;

public class AdaugaActivity extends AppCompatActivity {
    public static int RESULT_DELETE = 2;

    // UI Components
    private EditText etAutor;
    private EditText etText;
    private EditText etAprecieri;
    private Spinner spnCategorie;
    private TextView tvError;
    private Button btnSave;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga);

        initComponents();

        if (getIntent().getSerializableExtra(MainActivity.KEY_CITAT) != null) {
            editModeOn((Citat) getIntent().getSerializableExtra(MainActivity.KEY_CITAT));
        }
    }

    private void editModeOn(Citat citatEdit) {
        etAutor.setText(citatEdit.getAutor());
        etText.setText(citatEdit.getText());
        etAprecieri.setText(String.valueOf(citatEdit.getNumarAprecieri()));

        for (int i = 0; i < spnCategorie.getAdapter().getCount(); i++) {
            if (spnCategorie.getAdapter().getItem(i).toString()
                    .equalsIgnoreCase(
                            citatEdit.getCategorie().toString()
                    ))
            {
                spnCategorie.setSelection(i);
                break;
            }
        }

        btnDelete.setVisibility(View.VISIBLE);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_DELETE, intent);
                finish();
            }
        });
    }

    private void initComponents() {
        etAutor = findViewById(R.id.adauga_et_autor);
        etText = findViewById(R.id.adauga_et_text);
        etAprecieri = findViewById(R.id.adauga_et_aprecieri);
        spnCategorie = findViewById(R.id.adauga_spn_categorie);
        tvError = findViewById(R.id.adauga_tv_error);
        btnSave = findViewById(R.id.adauga_btn_save);
        btnDelete = findViewById(R.id.adauga_btn_delete);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.categorii_citate,
                android.R.layout.simple_spinner_dropdown_item
        );
        spnCategorie.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Intent intent = new Intent();
                    Citat citat = createCitatFromView();
                    intent.putExtra(MainActivity.KEY_CITAT, citat);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Citat createCitatFromView() {
        String autor = etAutor.getText().toString().trim();
        String text = etText.getText().toString().trim();
        int aprecieri = Integer.parseInt(etAprecieri.getText().toString().trim());
        Categorie categorie = Categorie.valueOf(spnCategorie.getSelectedItem().toString().toUpperCase());

        return new Citat(autor, text, aprecieri, categorie);
    }

    private boolean validate() {
        if (etAutor.getText().length() == 0 || etText.getText().length() == 0 || etAprecieri.getText().length() == 0) {
            return false;
        }

        try {
            int n = Integer.parseInt(etAprecieri.getText().toString().trim());
            if (n < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
