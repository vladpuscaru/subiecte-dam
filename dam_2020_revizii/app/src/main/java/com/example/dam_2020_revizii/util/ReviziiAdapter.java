package com.example.dam_2020_revizii.util;

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

import com.example.dam_2020_revizii.MainActivity;
import com.example.dam_2020_revizii.R;
import com.example.dam_2020_revizii.model.Revizie;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ReviziiAdapter extends ArrayAdapter<Revizie> {

    private List<Revizie> revizii;
    private List<Revizie> original;
    private Context context;

    private ReviziiFilter filter;

    private class ReviziiFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                results.values = original;
                results.count = original.size();
                return results;
            }


            List<Revizie> filterR = new ArrayList<>();
            for (Revizie r : original) {
                if (r.getTip().toString().equalsIgnoreCase(constraint.toString())) {
                    filterR.add(r);
                }
            }

            results.values = filterR;
            results.count = filterR.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            revizii = (List<Revizie>) results.values;
            notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public Revizie getItem(int position) {
        return revizii.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return revizii.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ReviziiFilter();
        }
        return filter;
    }

    // UI
    private class ViewHolder {
        private TextView tvNrAuto;
        private TextView tvNrKm;
        private TextView tvData;
        private TextView tvCost;
        private TextView tvTip;
    }

    public ReviziiAdapter(@NonNull Context context, @NonNull List<Revizie> objects) {
        super(context, R.layout.adapter_revizii, objects);
        this.original = objects;
        this.revizii = original;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Revizie current = getItem(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.adapter_revizii, parent, false);

            viewHolder.tvNrAuto = convertView.findViewById(R.id.adapter_tv_nrAuto);
            viewHolder.tvNrKm = convertView.findViewById(R.id.adapter_tv_nrKm);
            viewHolder.tvData = convertView.findViewById(R.id.adapter_tv_data);
            viewHolder.tvCost = convertView.findViewById(R.id.adapter_tv_cost);
            viewHolder.tvTip = convertView.findViewById(R.id.adapter_tv_tip);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (current != null) {
            viewHolder.tvNrAuto.setText(current.getNumarAuto());
            viewHolder.tvNrKm.setText(String.valueOf(current.getNumarKm()));
            viewHolder.tvData.setText(MainActivity.dateFormat.format(current.getData()));
            viewHolder.tvCost.setText(String.valueOf(current.getCost()));
            viewHolder.tvTip.setText(current.getTip().toString());
        }

        return convertView;
    }
}
