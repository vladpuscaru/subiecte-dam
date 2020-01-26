package com.example.dam_bilet_2019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dam_bilet_2019.db.AppDatabase;
import com.example.dam_bilet_2019.db.model.Bilet;

public class AdaugaActivity extends AppCompatActivity {

    private boolean edit = false;

    // UI
    private EditText etNumar;
    private EditText etPlecare;
    private EditText etDestinatie;
    private EditText etPret;
    private RadioGroup rgTarifRedus;

    private TextView tvError;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga);

        initComponents();

        if (getIntent().getParcelableExtra(ListaActivity.KEY_EDIT_BILET) != null) {
            editModeOn();
        }
    }

    private void editModeOn() {
        edit = true;
        Bilet bilet = getIntent().getParcelableExtra(ListaActivity.KEY_EDIT_BILET);
        assert bilet != null;
        etNumar.setText(String.valueOf(bilet.getNumar()));
        etPlecare.setText(bilet.getPlecare());
        etDestinatie.setText(bilet.getDestinatie());
        etPret.setText(String.valueOf(bilet.getPret()));

        if (bilet.isTarifRedus()) {
            ((RadioButton) findViewById(R.id.adauga_rb_tarif_true)).setChecked(true);
            ((RadioButton) findViewById(R.id.adauga_rb_tarif_false)).setChecked(false);
        } else {
            ((RadioButton) findViewById(R.id.adauga_rb_tarif_true)).setChecked(false);
            ((RadioButton) findViewById(R.id.adauga_rb_tarif_false)).setChecked(true);
        }
    }

    private void initComponents() {
        etNumar = findViewById(R.id.adauga_et_numar);
        etPlecare = findViewById(R.id.adauga_et_plecare);
        etDestinatie = findViewById(R.id.adauga_et_destinatie);
        etPret = findViewById(R.id.adauga_et_pret);
        rgTarifRedus = findViewById(R.id.adauga_rg_tarif);
        tvError = findViewById(R.id.adauga_tv_error);
        btnSave = findViewById(R.id.adauga_btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validare()) {
                    final Bilet bilet = createBiletFromView();
                    final Intent intent = new Intent();
                    if (edit) {
                        intent.putExtra(ListaActivity.KEY_EDIT_BILET, bilet);
                    } else {
                        intent.putExtra(MainActivity.KEY_BILET, bilet);
                    }

                    // Add to db
                    new AsyncTask<Bilet, Void, Long>() {
                        @Override
                        protected Long doInBackground(Bilet... bilets) {
                            return AppDatabase.getInstance(getApplicationContext()).biletDao().insert(bilets[0]);
                        }

                        @Override
                        protected void onPostExecute(Long aLong) {
                            // TODO: Remove this
                            Toast.makeText(getApplicationContext(), "Added to DB", Toast.LENGTH_SHORT).show();
                            bilet.setId(aLong);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }.execute(bilet);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean validare() {
        if (etNumar.getText().length() == 0 ||
                etPlecare.getText().length() == 0 ||
                etDestinatie.getText().length() == 0 ||
                etPret.getText().length() == 0) {
            return false;
        }

        try {
            int nr = Integer.parseInt(etNumar.getText().toString().trim());
            if (nr < 0) {
                return false;
            }

            double pr = Double.parseDouble(etPret.getText().toString().trim());
            if (pr < 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private Bilet createBiletFromView() {
        int numar = Integer.parseInt(etNumar.getText().toString().trim());
        String plecare = etPlecare.getText().toString().trim();
        String destinatie = etDestinatie.getText().toString().trim();
        double pret = Double.parseDouble(etPret.getText().toString().trim());

        boolean tarif = false;
        switch (rgTarifRedus.getCheckedRadioButtonId()) {
            case R.id.adauga_rb_tarif_true:
                tarif = true;
                break;
            case R.id.adauga_rb_tarif_false:
                tarif = false;
                break;
        }

        return new Bilet(numar, plecare, destinatie, tarif, pret);
    }
}
