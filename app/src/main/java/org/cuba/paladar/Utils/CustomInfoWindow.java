package org.cuba.paladar.Utils;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import org.cuba.paladar.R;
import org.cuba.paladar.RestaurantDetailActivity;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.views.MapView;

public class CustomInfoWindow extends MarkerInfoWindow {

    private final int idRestaurant;

    public CustomInfoWindow(int layoutResId, MapView mapView, int id) {
        super(layoutResId, mapView);

        idRestaurant = id;

        Button btn = (Button) (mView.findViewById(R.id.bubble_moreinfo));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent restaurantDetail = new Intent(view.getContext(),
                        RestaurantDetailActivity.class);
                restaurantDetail.putExtra("restaurant_id", idRestaurant);

                view.getContext().startActivity(restaurantDetail);
            }
        });
    }

    @Override
    public void onOpen(Object item) {
        closeAllInfoWindowsOn(mMapView);
        super.onOpen(item);
        mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
    }
}
