package com.example.dam1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dam1.model.Jucator;
import com.example.dam1.model.PozitieFotbal;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AdaugaJucatorActivity extends AppCompatActivity {

    // UI components
    private EditText etNume;
    private EditText etNumar;
    private EditText etData;
    private Spinner spnPozitie;
    private Button btnSave;

    private TextView tvAlert;

    private PozitieFotbal pozitieSelectata;

    // Edit jucator
    private Jucator editJucator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_jucator);

        initComponents();

        if (getIntent().hasExtra(MainActivity.EDIT_JUCATOR_KEY)) {
            editJucator = getIntent().getParcelableExtra(MainActivity.EDIT_JUCATOR_KEY);
            setInputs();
        }
    }

    private void setInputs() {
        etNume.setText(editJucator.getNume());
        etNumar.setText(String.valueOf(editJucator.getNumar()));
        etData.setText(MainActivity.dateFormat.format(editJucator.getDataNasterii()));

        String pozitie = editJucator.getPozitie().toString();
        int poz = 0;

        for (int i = 0; i < spnPozitie.getAdapter().getCount(); i++) {
            String x = spnPozitie.getAdapter().getItem(i).toString().toUpperCase();
            if (pozitie.toUpperCase().equals(x)) {
                poz = i;
                break;
            }
        }

        spnPozitie.setSelection(poz);
    }

    private void initComponents() {
        etNume = findViewById(R.id.adauga_et_nume);
        etNumar = findViewById(R.id.adauga_et_numar);
        etData = findViewById(R.id.adauga_et_data);
        btnSave = findViewById(R.id.adauga_btn_save);
        tvAlert = findViewById(R.id.adauga_tv_alert);
        spnPozitie = findViewById(R.id.adauga_spn_pozitie);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.adauga_spn_values,
                R.layout.support_simple_spinner_dropdown_item);

        spnPozitie.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validare()) {
                    Jucator jucator = createJucatorFromView();
                    Intent intent = new Intent();

                    if (editJucator == null) {
                        intent.putExtra(MainActivity.ADAUGA_JUCATOR_KEY, jucator);
                    } else {
                        intent.putExtra(MainActivity.EDIT_JUCATOR_KEY, jucator);
                    }

                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    tvAlert.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Jucator createJucatorFromView() {
        String nume = etNume.getText().toString().trim();
        int nr = Integer.parseInt(etNumar.getText().toString().trim());
        Date data = null;
        try {
            data = MainActivity.dateFormat.parse(etData.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PozitieFotbal pozitie = PozitieFotbal.valueOf(spnPozitie.getSelectedItem().toString().toUpperCase());

        return new Jucator(nr, nume, data, pozitie);
    }

    private boolean validare() {
        if (etNume.getText().length() < 0 || etNumar.getText().length() < 0 || etData.getText().length() < 0) {
            return false;
        }

        try {

            int nr = Integer.parseInt(etNumar.getText().toString().trim());
            if (nr < 0) {
                return false;
            }

            Date date = MainActivity.dateFormat.parse(etData.getText().toString().trim());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
