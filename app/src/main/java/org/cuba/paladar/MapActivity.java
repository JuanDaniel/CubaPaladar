package org.cuba.paladar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.cuba.paladar.Model.DataContexts.ApplicationDataContext;
import org.cuba.paladar.Model.Entities.Image;
import org.cuba.paladar.Model.Entities.Restaurant;
import org.cuba.paladar.Utils.ApplicationHelper;
import org.cuba.paladar.Utils.CustomInfoWindow;
import org.cuba.paladar.Utils.TileSource;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MBTilesFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Activity {

    private ApplicationDataContext dataContext;
    private List<Restaurant> restaurants;
    private long restaurant_id;
    private double latitude;
    private double longitude;
    private MapView mapView;
    private boolean localization;
    private LocationManager locManager;
    private MyLocationNewOverlay myLocationoverlay;
    private boolean isFocused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        restaurant_id = getIntent().getLongExtra("restaurant_id", -1);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        localization = getIntent().getBooleanExtra("localization", false);

		/*
         * ITileSource tile = new TileSource("Cuba",
		 * ResourceProxy.string.offline_mode, 14, 17, 256, ".png");
		 */
        ITileSource tile = new TileSource("Cuba",
                ResourceProxy.string.offline_mode, 8, 16, 256, ".png");

        ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(
                getApplicationContext());
        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(this);


        try {
            dataContext = new ApplicationDataContext(MapActivity.this);

            //restaurants = dataContext.

            File file = new File(ApplicationHelper.getInstance(MapActivity.this).getMapPath());
            IArchiveFile[] files = {MBTilesFileArchive
                    .getDatabaseFileArchive(file)};

			/*
			 * IArchiveFile[] files = { DatabaseFileArchive
			 * .getDatabaseFileArchive(file) };
			 */
            MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(
                    simpleReceiver, tile, files);

            MapTileProviderArray mProvider = new MapTileProviderArray(tile,
                    null, new MapTileModuleProviderBase[]{moduleProvider});

            mapView = new MapView(this, 256, mResourceProxy, mProvider);
            mapView.setBuiltInZoomControls(true);
            mapView.setMultiTouchControls(true);
            mapView.setUseDataConnection(false);
            mapView.getController().setZoom(12);

            if (latitude != 0 && longitude != 0) {
                isFocused = false;
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                if (!isFocused) {
                                    mapView.getController().setZoom(
                                            mapView.getMaxZoomLevel());
                                    mapView.getController().setCenter(
                                            new GeoPoint(latitude, longitude));

                                    isFocused = true;
                                }
                            }
                        });
            } else {
                mapView.getController().setCenter(
                        new GeoPoint(23.1346, -82.3897));
            }

            if (localization) {
                startLocalization();
            }

            mapView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));

            restaurants = new ArrayList<Restaurant>();

            // Positioning the restaurants
            ArrayList<Marker> items = new ArrayList<Marker>();
            for (int i = 0, c = restaurants.size(); i < c; i++) {
                Restaurant restaurant = restaurants.get(i);
                Marker marker = new Marker(mapView);
                marker.setPosition(new GeoPoint(restaurant.getAddress()
                        .getLatitude(), restaurant.getAddress().getLongitude()));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle(restaurant.getName());
                marker.setIcon(getResources().getDrawable(R.drawable.marker));

                if (restaurant.getImages().size() > 0) {
                    Image image = (Image) restaurant.getImages().get(0);
                    marker.setImage(new BitmapDrawable(getResources(),
                            image.getImage()));
                }

                marker.setSnippet(restaurant.getAddress().__toString());
                marker.setInfoWindow(new CustomInfoWindow(
                        R.layout.bonuspack_bubble, mapView, restaurant.getID().intValue()));

                items.add(marker);

                if (restaurant.getID() == restaurant_id) {
                    marker.showInfoWindow();
                }
            }

            mapView.getOverlays().addAll(items);

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.mapView);
            layout.addView(mapView);

        } catch (AdaFrameworkException e) {
            configDataBasePath();
        } catch (org.cuba.paladar.Utils.ApplicationHelper.DataBaseException e) {
            configDataBasePath();
        }
    }

    private void startLocalization() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(MapActivity.this,
                    getString(R.string.msg_disable_gps), Toast.LENGTH_LONG)
                    .show();
        }

        myLocationoverlay = new MyLocationNewOverlay(this, mapView);
        myLocationoverlay.enableMyLocation();
        // myLocationoverlay.enableCompas();
        myLocationoverlay.enableFollowLocation();
        myLocationoverlay.setDrawAccuracyEnabled(true);
        myLocationoverlay.runOnFirstFix(new Runnable() {
            public void run() {
                if (myLocationoverlay.getMyLocation() != null) {
                    mapView.getController().animateTo(
                            myLocationoverlay.getMyLocation());
                }
            }
        });

        mapView.getOverlays().add(myLocationoverlay);
    }

    protected void configDataBasePath() {
        Toast.makeText(MapActivity.this,
                getString(R.string.msg_database_exception_data),
                Toast.LENGTH_LONG).show();

        Intent update = new Intent(MapActivity.this, UpdateActivity.class);

        startActivity(update);
        finish();
    }

}