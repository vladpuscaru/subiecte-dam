package com.example.dam_examen_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dam_examen_2020.db.model.Examen;

public class AdaugaActivity extends AppCompatActivity {

    // UI
    private EditText etId;
    private EditText etMaterie;
    private EditText etStudenti;
    private EditText etSala;
    private EditText etSupraveghetor;

    private Button btnSave;

    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga);

        initComponents();

        if (getIntent().getParcelableExtra(MainActivity.KEY_EXAMEN) != null) {
            editModeOn((Examen) getIntent().getParcelableExtra(MainActivity.KEY_EXAMEN));
        }
    }

    private void editModeOn(Examen examen) {
        etId.setText(String.valueOf(examen.getId()));
        etMaterie.setText(examen.getDenumireMaterie());
        etStudenti.setText(String.valueOf(examen.getNumarStudenti()));
        etSala.setText(examen.getSala());
        etSupraveghetor.setText(examen.getSupraveghetor());
    }

    private void initComponents() {
        etId = findViewById(R.id.adauga_et_id);
        etMaterie = findViewById(R.id.adauga_et_materie);
        etStudenti = findViewById(R.id.adauga_et_studenti);
        etSala = findViewById(R.id.adauga_et_sala);
        etSupraveghetor = findViewById(R.id.adauga_et_supraveghetor);
        tvError = findViewById(R.id.adauga_tv_error);

        btnSave = findViewById(R.id.adauga_btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validare()) {
                    Intent intent = new Intent();
                    Examen examen = createExamenFromView();
                    intent.putExtra(MainActivity.KEY_EXAMEN, examen);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Examen createExamenFromView() {
        long id = Long.parseLong(etId.getText().toString().trim());
        String materie = etMaterie.getText().toString().trim();
        int studenti = Integer.parseInt(etStudenti.getText().toString().trim());
        String sala = etSala.getText().toString().trim();
        String supraveghetor = etSupraveghetor.getText().toString().trim();

        return new Examen(id, materie, studenti, sala, supraveghetor);
    }

    private boolean validare() {
        if (etId.getText().length() == 0 ||
                etMaterie.getText().length() == 0 ||
                etStudenti.getText().length() == 0 ||
                etSala.getText().length() == 0 ||
                etSupraveghetor.getText().length() == 0) {
            return false;
        }

        // TODO: Check for id unique in database

        try {
            long id = Long.parseLong(etId.getText().toString());
            if (id < 0) {
                return false;
            }

            int st = Integer.parseInt(etStudenti.getText().toString());
            if (st < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
