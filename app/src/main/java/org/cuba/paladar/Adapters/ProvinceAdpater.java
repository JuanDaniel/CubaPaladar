package org.cuba.paladar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cuba.paladar.Model.Entities.Province;
import org.cuba.paladar.R;

import java.util.List;

public class ProvinceAdpater extends ArrayAdapter<Province> {

    private final Context context;
    private final List<Province> provinces;

    public ProvinceAdpater(Context context, List<Province> provinces) {
        super(context, R.layout.item_restaurant, provinces);

        this.context = context;
        this.provinces = provinces;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_spinner, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(provinces.get(position).getName());

        return rowView;
    }

}
