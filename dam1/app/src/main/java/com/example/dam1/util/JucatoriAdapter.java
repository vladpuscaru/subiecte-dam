package com.example.dam1.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dam1.MainActivity;
import com.example.dam1.R;
import com.example.dam1.model.Jucator;

import java.util.List;

public class JucatoriAdapter extends ArrayAdapter<Jucator> {

    private List<Jucator> data;

    // UI Components
    private static class ViewHolder {
        private TextView tvNume;
        private TextView tvNumar;
        private TextView tvPozitie;
        private TextView tvData;
    }

    public JucatoriAdapter(@NonNull Context context, List<Jucator> resource) {
        super(context, R.layout.adapter_jucatori, resource);
        this.data = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Jucator current = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_jucatori, parent, false);

            viewHolder.tvNume = convertView.findViewById(R.id.adapter_jucatori_tv_nume);
            viewHolder.tvNumar = convertView.findViewById(R.id.adapter_jucatori_tv_numar);
            viewHolder.tvPozitie = convertView.findViewById(R.id.adapter_jucatori_tv_pozitie);
            viewHolder.tvData = convertView.findViewById(R.id.adapter_jucatori_tv_data);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvNume.setText(current.getNume());
        viewHolder.tvNumar.setText(String.valueOf(current.getNumar()));
        viewHolder.tvPozitie.setText(current.getPozitie().toString());
        viewHolder.tvData.setText(MainActivity.dateFormat.format(current.getDataNasterii()));

        return convertView;
    }
}
