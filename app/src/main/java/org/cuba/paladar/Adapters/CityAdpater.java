package org.cuba.paladar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cuba.paladar.Model.Entities.City;
import org.cuba.paladar.R;

import java.util.List;

public class CityAdpater extends ArrayAdapter<City> {

    private final Context context;
    private final List<City> cities;

    public CityAdpater(Context context, List<City> cities) {
        super(context, R.layout.item_restaurant, cities);

        this.context = context;
        this.cities = cities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_spinner, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(cities.get(position).getName());

        return rowView;
    }

}
