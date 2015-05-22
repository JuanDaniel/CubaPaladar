package org.cuba.paladar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cuba.paladar.Model.Entities.Image;
import org.cuba.paladar.Model.Entities.Restaurant;
import org.cuba.paladar.R;

import java.util.List;

public class RestaurantAdpater extends ArrayAdapter<Restaurant> {

    private final Context context;
    private final int rank;
    private final List<Restaurant> restaurants;

    public RestaurantAdpater(Context context, int rank, List<Restaurant> restaurants) {
        super(context, R.layout.item_restaurant, restaurants);

        this.context = context;
        this.rank = rank;
        this.restaurants = restaurants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater
                .inflate(R.layout.item_restaurant, parent, false);

        Restaurant restaurant = (Restaurant) this.restaurants.get(position);

        ImageView photo = (ImageView) rowView.findViewById(R.id.image);

        List<Image> restaurantImages = restaurant.getImages();

        if (restaurantImages != null && restaurantImages.size() > 0) {
            photo.setImageBitmap(restaurantImages.get(0).getImage());
        } else {
            photo.setImageResource(R.drawable.ic_paladar_no_image);
        }

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(restaurant.getName());

        TextView badgeCp = (TextView) rowView.findViewById(R.id.badge_cpl);
        TextView badgeP = (TextView) rowView.findViewById(R.id.badge_p);
        // CubaPaladar ranking
        if (this.rank == 1) {
            badgeCp.setText(String.valueOf(restaurant.getRankingCpl()));
            badgeCp.setVisibility(View.VISIBLE);
        }
        // Popular ranking
        else {
            badgeP.setText(String.valueOf(restaurant.getRankingP()));
            badgeP.setVisibility(View.VISIBLE);
        }

        TextView address = (TextView) rowView.findViewById(R.id.address);
        address.setText(restaurant.getAddress().__toString());

        return rowView;
    }

}
