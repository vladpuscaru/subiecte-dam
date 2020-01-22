package com.example.dam_c_2020_1.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dam_c_2020_1.R;
import com.example.dam_c_2020_1.db.model.Citat;

import java.util.ArrayList;
import java.util.List;

public class CitateAdapter extends ArrayAdapter<Citat> {

    private List<Citat> citate;
    private List<Citat> citateCopy;

    private CitateFilter citateFilter;

    private class CitateFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                results.values = citateCopy;
                results.count = citateCopy.size();
            } else {
                List<Citat> citateFiltered = new ArrayList<>();
                for (Citat c : citateCopy) {
                    if (c.getCategorie().toString().equalsIgnoreCase(constraint.toString())) {
                        citateFiltered.add(c);
                    }
                }
                results.values = citateFiltered;
                results.count = citateFiltered.size();
            }
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            citate = (List<Citat>) results.values;
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        private TextView tvAutor;
        private TextView tvText;
        private TextView tvAprecieri;
        private TextView tvCategorie;
    }

    public CitateAdapter(@NonNull Context context, @NonNull List<Citat> objects) {
        super(context, R.layout.adapter_citate, objects);
        this.citate = objects;
        this.citateCopy = citate;
    }

    @Override
    public int getCount() {
        return citate.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public Citat getItem(int position) {
        return citate.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Citat current = getItem(position);
        ViewHolder viewHolder;


        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_citate, parent, false);

            viewHolder.tvAutor = convertView.findViewById(R.id.adapter_tv_autor);
            viewHolder.tvText = convertView.findViewById(R.id.adapter_tv_text);
            viewHolder.tvAprecieri = convertView.findViewById(R.id.adapter_tv_aprecieri);
            viewHolder.tvCategorie = convertView.findViewById(R.id.adapter_tv_categorie);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvAutor.setText(current.getAutor());
        viewHolder.tvText.setText("\"" + current.getText() + "\"");
        viewHolder.tvAprecieri.setText(String.valueOf(current.getNumarAprecieri()));
        viewHolder.tvCategorie.setText(current.getCategorie().toString());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (citateFilter == null) {
            citateFilter = new CitateFilter();
        }
        return citateFilter;
    }
}
