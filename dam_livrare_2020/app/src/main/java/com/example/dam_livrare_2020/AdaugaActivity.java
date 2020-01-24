package com.example.dam_livrare_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dam_livrare_2020.database.AppDatabase;
import com.example.dam_livrare_2020.database.model.Livrare;
import com.example.dam_livrare_2020.database.model.TipLivrare;

import java.util.Date;
import java.text.ParseException;

public class AdaugaActivity extends AppCompatActivity {

    public static int RESULT_DELETE = 2;

    private boolean edit = false;

    // UI Components
    private EditText etDestinatar;
    private EditText etAdresa;
    private EditText etData;
    private EditText etValoare;
    private Spinner spnTip;
    private Button btnSalvare;
    private Button btnStergere;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga);

        initComponents();

        if (getIntent().getParcelableExtra(IstoricActivity.KEY_EDIT) != null) {
            editModeOn();
        }
    }

    private void editModeOn() {
        btnStergere.setVisibility(View.VISIBLE);
        btnStergere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_DELETE);
                finish();
            }
        });
        edit = true;

        // set fields
        Livrare livrare = getIntent().getParcelableExtra(IstoricActivity.KEY_EDIT);
        if (livrare != null) {
            etDestinatar.setText(livrare.getDestinatar());
            etAdresa.setText(livrare.getAdresa());
            etData.setText(
                    MainActivity.dateFormat.format(livrare.getData())
            );
            etValoare.setText(String.valueOf(livrare.getValoare()));

            ArrayAdapter adapter = (ArrayAdapter) spnTip.getAdapter();

            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equalsIgnoreCase(livrare.getTip().toString())) {
                    spnTip.setSelection(i);
                }
            }
        }

    }

    private void initComponents() {
        etDestinatar = findViewById(R.id.adauga_et_destinatar);
        etAdresa = findViewById(R.id.adauga_et_adresa);
        etData = findViewById(R.id.adauga_et_data);
        etValoare = findViewById(R.id.adauga_et_valoare);
        spnTip = findViewById(R.id.adauga_spn_tip);
        btnSalvare = findViewById(R.id.adauga_btn_salvare);
        btnStergere = findViewById(R.id.adauga_btn_stergere);
        tvError = findViewById(R.id.adauga_tv_error);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.adauga_spn_items, android.R.layout.simple_spinner_item);
        spnTip.setAdapter(adapter);

        btnSalvare.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                if (validare()) {
                    final Livrare livrare = createLivrareFromView();
                    final Intent intent = new Intent();

                    if (!edit) {
                        // add to db
                        new AsyncTask<Livrare, Void, Long>() {
                            @Override
                            protected Long doInBackground(Livrare... livrares) {
                                return AppDatabase.getInstance(getApplicationContext()).livrareDao().insert(livrares[0]);
                            }

                            @Override
                            protected void onPostExecute(Long aLong) {
                                // TODO: Remove this
                                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
                                // TODO: Remove this

                                livrare.setId(aLong);
                                intent.putExtra(MainActivity.KEY_LIVRARE, livrare);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }.execute(livrare);
                    } else {
                        intent.putExtra(IstoricActivity.KEY_EDIT, livrare);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Livrare createLivrareFromView() {
        String destinatar = etDestinatar.getText().toString().trim();
        String adresa = etAdresa.getText().toString().trim();
        Date data = null;
        try {
            data = MainActivity.dateFormat.parse(etData.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Float valoare = Float.parseFloat(etValoare.getText().toString().trim());
        TipLivrare tip = TipLivrare.valueOf(spnTip.getSelectedItem().toString().toUpperCase());

        return new Livrare(destinatar, adresa, data, valoare, tip);
    }

    private boolean validare() {
        if (etDestinatar.getText().length() == 0 ||
                etAdresa.getText().length() == 0 ||
                etData.getText().length() == 0 ||
                etValoare.getText().length() == 0) {
            return false;
        }

        try {
            Float v = Float.parseFloat(etValoare.getText().toString().trim());
            if (v < 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            Date d = MainActivity.dateFormat.parse(etData.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
