package org.cuba.paladar.Model.DataContexts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.ObjectSet;
import com.mobandme.ada.exceptions.AdaFrameworkException;

import org.cuba.paladar.Model.Entities.City;
import org.cuba.paladar.Model.Entities.ContentCritic;
import org.cuba.paladar.Model.Entities.Image;
import org.cuba.paladar.Model.Entities.Language;
import org.cuba.paladar.Model.Entities.Province;
import org.cuba.paladar.Model.Entities.Restaurant;
import org.cuba.paladar.Model.Entities.RestaurantAddress;
import org.cuba.paladar.Model.Entities.RestaurantCritic;
import org.cuba.paladar.Model.Entities.Town;
import org.cuba.paladar.Utils.ApplicationHelper;
import org.cuba.paladar.Utils.ApplicationHelper.DataBaseException;

public class ApplicationDataContext extends ObjectContext {

    public ObjectSet<Restaurant> restaurantsSet;
    public ObjectSet<RestaurantAddress> restaurantAddressSet;
    public ObjectSet<Province> provinceSet;
    public ObjectSet<City> citySet;
    public ObjectSet<Town> townSet;
    public ObjectSet<RestaurantCritic> restaurantCriticSet;
    public ObjectSet<ContentCritic> contentCriticSet;
    public ObjectSet<Language> languageSet;
    public ObjectSet<Image> restaurantImageSet;

    public ApplicationDataContext(Context pContext)
            throws AdaFrameworkException, DataBaseException {
        super(pContext, ApplicationHelper.getInstance(pContext).getDataBasePath());

        restaurantsSet = new ObjectSet<Restaurant>(Restaurant.class, this);
        restaurantAddressSet = new ObjectSet<RestaurantAddress>(RestaurantAddress.class, this);
        provinceSet = new ObjectSet<Province>(Province.class, this);
        citySet = new ObjectSet<City>(City.class, this);
        townSet = new ObjectSet<Town>(Town.class, this);
        restaurantCriticSet = new ObjectSet<RestaurantCritic>(RestaurantCritic.class, this);
        contentCriticSet = new ObjectSet<ContentCritic>(ContentCritic.class, this);
        languageSet = new ObjectSet<Language>(Language.class, this);
        restaurantImageSet = new ObjectSet<Image>(Image.class, this);
    }

    // Get total of restaurants
    public int getTotalRestaurant() {
        SQLiteDatabase myDataBase = getReadableDatabase();

        int total = 0;

        String sql = "SELECT COUNT(ID) total FROM restaurant";

        Cursor result = myDataBase.rawQuery(sql, null);
        if (result.moveToFirst()) {
            do {
                total = result.getInt(result.getColumnIndex("total"));
            } while (result.moveToNext());
        }

        return total;
    }

    // Get total of search restaurants
    public int getTotalSearch(String query) {
        SQLiteDatabase myDataBase = getReadableDatabase();

        int total = 0;

        String sql = "SELECT count(restaurant.ID) total FROM restaurant "
                + "INNER JOIN restaurant_address ON restaurant.address_id = restaurant_address.ID "
                + "INNER JOIN town ON restaurant_address.town_id = town.ID "
                + "INNER JOIN city ON town.city_id = city.ID "
                + "INNER JOIN province ON city.province_id = province.ID "
                + "WHERE restaurant.name LIKE ? OR "
                + "restaurant_address.street || ', e/ ' || restaurant_address.between_a || ' y ' || restaurant_address.between_b || ', ' || town.name || ', ' || city.name || ', ' || province.name LIKE ? "
                + "ORDER BY ranking_cpl ASC";

        String[] args = {"%" + query.trim() + "%", "%" + query.trim() + "%"};

        Cursor result = myDataBase.rawQuery(sql, args);
        if (result.moveToFirst()) {
            do {
                total = result.getInt(result.getColumnIndex("total"));
            } while (result.moveToNext());
        }

        return total;
    }

    public ContentCritic getContentCritic(String lang) {
        Language language;
        for (ContentCritic content : contentCriticSet) {
            language = content.getLang();
            if (language != null) {
                if (language.getLang() != null) {
                    if (language.equals(lang)) {
                        return content;
                    }
                }
            }
        }

        return contentCriticSet.get(0);
    }
}
