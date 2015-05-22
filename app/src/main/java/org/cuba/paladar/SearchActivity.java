package org.cuba.paladar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.SearchAutoComplete;
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

import org.cuba.paladar.Adapters.RestaurantSearchAdpater;
import org.cuba.paladar.Model.DataContexts.ApplicationDataContext;
import org.cuba.paladar.Model.Entities.Restaurant;

import java.util.List;

public class SearchActivity extends BaseActivity {

    private static int increase = 10;
    private ApplicationDataContext dataContext;
    private SearchView search;
    private ListView listResults;
    private List<Restaurant> restaurants;
    private ProgressDialog progressBar;
    private String query;
    private int total;
    private int countShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.search);

        search = (SearchView) findViewById(R.id.search);
        search.setIconifiedByDefault(false);
        search.setQueryHint(getString(R.string.hint_search));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                if ((arg0.length() == 0 && countShow == 0)
                        || (arg0.length() > 2)) {
                    progressBar.show();
                    query = arg0;
                    new LoadDataTask().execute((Void) null);
                }

                // ((RestaurantSearchAdpater)listResults.getAdapter()).filter(arg0);

                return true;
            }
        });
        SearchAutoComplete searchAutoComplete = (SearchAutoComplete) search
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(getResources().getColor(
                R.color.font_black));

        listResults = (ListView) findViewById(R.id.list_results);
        listResults.setTextFilterEnabled(true);
        listResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {

                Intent restaurantDetail = new Intent().setClass(
                        SearchActivity.this,
                        RestaurantDetailActivity.class);

                restaurantDetail.putExtra("restaurant_id", restaurants
                        .get(arg2).getID());

                startActivity(restaurantDetail);
            }
        });

        listResults.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (countShow < total
                            && listResults.getLastVisiblePosition() >= countShow - 1) {
                        // Execute LoadMoreDataTask AsyncTask
                        new LoadMoreDataTask().execute();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2,
                                 int arg3) {
                // TODO Auto-generated method stub

            }
        });

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage(getString(R.string.msg_loading));
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        try {
            dataContext = new ApplicationDataContext(SearchActivity.this);

            new LoadDataTask().execute((Void) null);

            registerForContextMenu(listResults);
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
        if (v.getId() == R.id.list_results) {
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
                Intent restaurantDetail = new Intent().setClass(
                        SearchActivity.this, RestaurantDetailActivity.class);

                restaurantDetail.putExtra("restaurant_id", restaurant.getID());

                startActivity(restaurantDetail);

                break;

            // Go to the map
            case 1:
                if (restaurant.getAddress() != null
                        && restaurant.getAddress().getLatitude() != 0
                        && restaurant.getAddress().getLongitude() != 0) {
                    Intent map = new Intent().setClass(SearchActivity.this,
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
                    Toast.makeText(SearchActivity.this,
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
                    Toast.makeText(SearchActivity.this,
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
                if (query == null || query.length() == 0) {
                    total = dataContext.getTotalRestaurant();
                    restaurants = dataContext.restaurantsSet.search(true, null, null, "ranking_cpl ASC", null, null, 0, increase);
                } else {
                    total = dataContext.getTotalSearch(query);
                    restaurants = dataContext.restaurantsSet.search(
                            "restaurant INNER JOIN restaurant_address ON restaurant.address_id = restaurant_address.ID " +
                                    "INNER JOIN town ON restaurant_address.town_id = town.ID " +
                                    "INNER JOIN city ON town.city_id = city.ID " +
                                    "INNER JOIN province ON city.province_id = province.ID",
                            true,
                            new String[]{"restaurant.*"},
                            "restaurant.name LIKE ? OR restaurant_address.street || ', e/ ' " +
                                    "|| restaurant_address.between_a || ' y ' || restaurant_address.between_b " +
                                    "|| ', ' || town.name || ', ' || city.name || ', ' " +
                                    "|| province.name LIKE ?",
                            new String[]{"%" + query.trim() + "%", "%" + query.trim() + "%"}, "ranking_cpl ASC", null, null, 0, increase);
                }
                countShow = restaurants.size();
            } catch (AdaFrameworkException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(
                                SearchActivity.this,
                                getString(R.string.msg_database_exception_data),
                                Toast.LENGTH_LONG).show();
                    }
                });

                return false;
            } catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,
                                getString(R.string.msg_error),
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
                RestaurantSearchAdpater adapter = new RestaurantSearchAdpater(
                        SearchActivity.this, 1, restaurants);
                listResults.setAdapter(adapter);
            } else {
                Intent update = new Intent(SearchActivity.this,
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
                if (query == null || query.length() == 0) {
                    restaurants.addAll(dataContext.restaurantsSet.search(true, null, null, "ranking_cpl ASC", null, null, countShow, increase));
                } else if (query.length() > 2) {
                    restaurants.addAll(dataContext.restaurantsSet.search(
                            "restaurant INNER JOIN restaurant_address ON restaurant.address_id = restaurant_address.ID " +
                                    "INNER JOIN town ON restaurant_address.town_id = town.ID " +
                                    "INNER JOIN city ON town.city_id = city.ID " +
                                    "INNER JOIN province ON city.province_id = province.ID",
                            true,
                            new String[]{"restaurant.*"},
                            "restaurant.name LIKE ? OR restaurant_address.street || ', e/ ' " +
                                    "|| restaurant_address.between_a || ' y ' || restaurant_address.between_b " +
                                    "|| ', ' || town.name || ', ' || city.name || ', ' " +
                                    "|| province.name LIKE ?",
                            new String[]{"%" + query.trim() + "%", "%" + query.trim() + "%"}, "ranking_cpl ASC", null, null, countShow, increase));
                }
                countShow = restaurants.size();
            } catch (AdaFrameworkException e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(
                                SearchActivity.this,
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
                int position = listResults.getLastVisiblePosition();
                RestaurantSearchAdpater adapter = new RestaurantSearchAdpater(
                        SearchActivity.this, 1, restaurants);
                listResults.setAdapter(adapter);
                listResults.setSelectionFromTop(position, 0);
            }
        }

        @Override
        protected void onCancelled() {
            progressBar.dismiss();
        }
    }
}