package org.cuba.paladar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.cuba.paladar.Adapters.RestaurantAdpater;
import org.cuba.paladar.Model.DataContexts.ApplicationDataContext;
import org.cuba.paladar.Model.Entities.Restaurant;

import java.util.List;

public class RankActivity extends BaseActivity {

    private static int increase = 10;
    private ActionBar actionBar;
    private ApplicationDataContext dataContext;
    private ListView list;
    private List<Restaurant> restaurants;
    private int typeRank;
    private ProgressDialog progressBar;
    private int total;
    private int countShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.rank);

        typeRank = getIntent().getIntExtra("rank_type", 1);

        if (typeRank == 1) {
            actionBar.setTitle(R.string.ranking_cubapaladar);
        } else {
            actionBar.setTitle(R.string.ranking_popularity);
        }

        list = (ListView) findViewById(R.id.list_top_ten);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {

                Intent restaurantDetail = new Intent().setClass(
                        RankActivity.this,
                        RestaurantDetailActivity.class);

                restaurantDetail.putExtra("restaurant_id", restaurants
                        .get(arg2).getID());

                startActivity(restaurantDetail);
            }
        });

        list.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (countShow < total
                            && list.getLastVisiblePosition() >= countShow - 1) {
                        // Execute LoadMoreDataTask AsyncTask
                        new LoadMoreDataTask().execute();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2,
                                 int arg3) {
            }
        });

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage(getString(R.string.msg_loading));
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        try {
            dataContext = new ApplicationDataContext(RankActivity.this);

            // Execute LoadDataTask AsyncTask
            new LoadDataTask().execute((Void) null);

            registerForContextMenu(list);
        } catch (AdaFrameworkException e) {
            configDataBasePath();
        } catch (org.cuba.paladar.Utils.ApplicationHelper.DataBaseException e) {
            configDataBasePath();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.list_top_ten) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(getString(R.string.shortcuts));

            String[] menuItems = getResources().getStringArray(
                    R.array.menu_long_tap);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int menuItemIndex = item.getItemId();

        Restaurant restaurant;
        restaurant = restaurants.get(info.position);

        switch (menuItemIndex) {
            // Show the restaurant details
            case 0:
                Intent restaurantDetail = new Intent().setClass(RankActivity.this,
                        RestaurantDetailActivity.class);

                restaurantDetail.putExtra("restaurant_id", restaurant.getID());

                startActivity(restaurantDetail);

                break;

            // Go to the map
            case 1:
                if (restaurant.getAddress() != null
                        && restaurant.getAddress().getLatitude() != 0
                        && restaurant.getAddress().getLongitude() != 0) {
                    Intent map = new Intent().setClass(RankActivity.this,
                            MapActivity.class);

                    map.putExtra("restaurant_id", restaurant.getID());
                    if (restaurant.getAddress() != null) {
                        map.putExtra("latitude", restaurant.getAddress()
                                .getLatitude());
                        map.putExtra("longitude", restaurant.getAddress()
                                .getLongitude());
                    }

                    startActivity(map);
                } else {
                    Toast.makeText(RankActivity.this,
                            getString(R.string.msg_no_shortcut_map),
                            Toast.LENGTH_LONG).show();
                }

                break;

            // Contact by call or send a email
            case 2:
                if (restaurant.getPhones() != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + restaurant.getPhones()));

                    startActivity(intent);
                } else if (restaurant.getCell() != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + restaurant.getCell()));

                    startActivity(intent);
                } else if (restaurant.getEmail() != null) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);

                    intent.setData(Uri.parse("mailto:" + restaurant.getEmail()));

                    startActivity(intent);
                } else {
                    Toast.makeText(RankActivity.this,
                            getString(R.string.msg_no_shortcut_contact),
                            Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }

        return true;
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                if (dataContext != null) {
                    total = dataContext.getTotalRestaurant();
                    if (typeRank == 1) {
                        restaurants = dataContext.restaurantsSet.search(true, null, null, "ranking_cpl ASC", null, null, 0, increase);
                    } else {
                        restaurants = dataContext.restaurantsSet.search(true, null, null, "ranking_p ASC", null, null, 0, increase);
                    }
                    countShow = restaurants.size();
                }
            } catch (AdaFrameworkException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(
                                RankActivity.this,
                                getString(R.string.msg_database_exception_data),
                                Toast.LENGTH_LONG).show();
                    }
                });

                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            progressBar.dismiss();

            if (success) {
                RestaurantAdpater adapter = new RestaurantAdpater(RankActivity.this, typeRank, restaurants);
                list.setAdapter(adapter);
            } else {
                Intent update = new Intent(RankActivity.this,
                        UpdateActivity.class);

                startActivity(update);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            progressBar.dismiss();
        }
    }

    public class LoadMoreDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                if (typeRank == 1) {
                    restaurants.addAll(dataContext.restaurantsSet.search(true, null, null, "ranking_cpl ASC", null, null, countShow, increase));
                } else {
                    restaurants.addAll(dataContext.restaurantsSet.search(true, null, null, "ranking_p ASC", null, null, countShow, increase));
                }
                countShow = restaurants.size();
            } catch (AdaFrameworkException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(
                                RankActivity.this,
                                getString(R.string.msg_database_exception_data),
                                Toast.LENGTH_LONG).show();
                    }
                });

                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            progressBar.dismiss();

            if (success) {
                int position = list.getLastVisiblePosition();
                RestaurantAdpater adapter = new RestaurantAdpater(
                        RankActivity.this, typeRank, restaurants);
                list.setAdapter(adapter);
                list.setSelectionFromTop(position, 0);
            }
        }

        @Override
        protected void onCancelled() {
            progressBar.dismiss();
        }
    }

}
