package org.cuba.paladar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cuba.paladar.Model.ItemMenu;
import org.cuba.paladar.R;

import java.util.ArrayList;

public class ItemMenuAdapter extends ArrayAdapter<ItemMenu> {

    private final Context context;
    private final ArrayList<ItemMenu> modelsArrayList;

    public ItemMenuAdapter(Context context, ArrayList<ItemMenu> modelsArrayList) {

        super(context, R.layout.item_menu, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = null;
        rowView = inflater.inflate(R.layout.item_menu, parent, false);

        // 3. Get icon & title views from the rowView
        ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
        TextView titleView = (TextView) rowView.findViewById(R.id.item_title);

        // 4. Set the text for textView
        imgView.setImageResource(modelsArrayList.get(position).getIcon());
        titleView.setText(modelsArrayList.get(position).getTitle());

        return rowView;
    }
}
