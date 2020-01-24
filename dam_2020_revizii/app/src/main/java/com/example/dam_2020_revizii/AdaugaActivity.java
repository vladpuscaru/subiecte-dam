package com.example.dam_2020_revizii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dam_2020_revizii.model.Revizie;
import com.example.dam_2020_revizii.model.TipRevizie;

import java.text.ParseException;
import java.util.Date;

public class AdaugaActivity extends AppCompatActivity {

    public static int RESULT_DELETE = 2;

    private boolean edit;

    // UI
    private EditText etNrAuto;
    private EditText etNrKm;
    private EditText etData;
    private EditText etCost;
    private Spinner spnTip;

    private TextView tvError;

    private Button btnSalvare;
    private Button btnStergere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga);

        initComponents();

        if (getIntent().getParcelableExtra(ListaActivity.KEY_EDIT_REVIZIE) != null) {
            editModeOn();
        }
    }

    private void editModeOn() {
        edit  = true;
        Revizie revizie = getIntent().getParcelableExtra(ListaActivity.KEY_EDIT_REVIZIE);
        if (revizie != null) {
            etNrAuto.setText(revizie.getNumarAuto());
            etNrKm.setText(String.valueOf(revizie.getNumarKm()));
            etData.setText(MainActivity.dateFormat.format(revizie.getData()));
            etCost.setText(String.valueOf(revizie.getCost()));

            ArrayAdapter adapter = (ArrayAdapter) spnTip.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equalsIgnoreCase(revizie.getTip().toString())) {
                    spnTip.setSelection(i);
                    break;
                }
            }
        }

        btnStergere.setVisibility(View.VISIBLE);

        btnStergere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_DELETE);
                finish();
            }
        });
    }

    private void initComponents() {
        etNrAuto = findViewById(R.id.adauga_et_nrAuto);
        etNrKm = findViewById(R.id.adauga_et_nrKm);
        etData = findViewById(R.id.adauga_et_data);
        etCost = findViewById(R.id.adauga_et_cost);
        spnTip = findViewById(R.id.adauga_spn_tip);
        tvError = findViewById(R.id.adauga_tv_error);
        btnSalvare = findViewById(R.id.adauga_btn_salvare);
        btnStergere = findViewById(R.id.adauga_btn_stergere);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.adauga_spn_items, android.R.layout.simple_spinner_item);
        spnTip.setAdapter(adapter);

        btnSalvare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validare()) {
                    Revizie revizie = createRevizieFromView();
                    Intent intent = new Intent();
                    if (edit) {
                        intent.putExtra(ListaActivity.KEY_EDIT_REVIZIE, revizie);
                    } else {
                        intent.putExtra(MainActivity.KEY_REVIZIE, revizie);
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Revizie createRevizieFromView() {
        String nrAuto = etNrAuto.getText().toString().trim();
        int nrKm = Integer.parseInt(etNrKm.getText().toString().trim());
        Date date = null;
        try {
            date = MainActivity.dateFormat.parse(etData.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double cost = Double.parseDouble(etCost.getText().toString().trim());

        TipRevizie tip = TipRevizie.valueOf(spnTip.getSelectedItem().toString().toUpperCase());

        return new Revizie(nrAuto, nrKm, date, cost, tip);
    }

    private boolean validare() {
        if (etNrAuto.getText().length() == 0 ||
                etNrKm.getText().length() == 0 ||
                etData.getText().length() == 0 ||
                etCost.getText().length() == 0) {
            return false;
        }

        try {
            double cost = Double.parseDouble(etCost.getText().toString().trim());
            if (cost < 0 ) {
                return false;
            }

            int km = Integer.parseInt(etNrKm.getText().toString().trim());
            if (km < 0) {
                return false;
            }

            Date date = MainActivity.dateFormat.parse(etData.getText().toString().trim());
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
