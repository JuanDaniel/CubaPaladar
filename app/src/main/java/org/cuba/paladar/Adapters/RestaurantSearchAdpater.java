package org.cuba.paladar.Adapters;

import android.content.Context;

import org.cuba.paladar.Model.Entities.Restaurant;

import java.util.List;

public class RestaurantSearchAdpater extends RestaurantAdpater {

    private final Context context;
    //private final List<Restaurant> list;
    private final int rank;

    public RestaurantSearchAdpater(Context context, int rank, List<Restaurant> restaurants) {
        super(context, rank, restaurants);

        this.context = context;
        this.rank = rank;

		/*this.list = new ArrayList<Restaurant>();
        this.list.addAll(restaurants);*/
    }

    public void filter(String arg0) {
        /*String query = arg0.toLowerCase(Locale.getDefault());
		this.restaurants.clear();
		if (query.length() == 0) {
			this.restaurants.addAll(this.list);
		} else {
			String name, address;
			for (Restaurant item : list) {
				name = item.getName().toLowerCase(Locale.getDefault());
				address = item.getAddress().__toString()
						.toLowerCase(Locale.getDefault());
				if (name.contains(query) || address.contains(query)) {
					this.restaurants.add(item);
				}
			}
		}
		notifyDataSetChanged();*/
    }

}
