package org.cuba.paladar.Model.Entities;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by jdsantana on 28/03/15.
 */

@Table(name = "restaurant_critic")
public class RestaurantCritic extends Entity {

    @TableField(name = "service", datatype = Entity.DATATYPE_DOUBLE)
    private double service;

    @TableField(name = "food", datatype = Entity.DATATYPE_DOUBLE)
    private double food;

    @TableField(name = "drinking", datatype = Entity.DATATYPE_DOUBLE)
    private double drinking;

    @TableField(name = "enviroment", datatype = Entity.DATATYPE_DOUBLE)
    private double enviroment;

    @TableField(name = "price_quality", datatype = Entity.DATATYPE_DOUBLE)
    private double priceQuality;

    @TableField(name = "price", datatype = Entity.DATATYPE_DOUBLE)
    private double price;

    @TableField(name = "count", datatype = Entity.DATATYPE_DOUBLE)
    private double count;

    @TableField(name = "author", datatype = Entity.DATATYPE_STRING)
    private String author;

    @TableField(name = "restaurant_id", datatype = Entity.DATATYPE_ENTITY_REFERENCE)
    private Restaurant restaurant;

    public double getService() {
        return service;
    }

    public void setService(double service) {
        this.service = service;
    }

    public double getFood() {
        return food;
    }

    public void setFood(double food) {
        this.food = food;
    }

    public double getDrinking() {
        return drinking;
    }

    public void setDrinking(double drinking) {
        this.drinking = drinking;
    }

    public double getEnviroment() {
        return enviroment;
    }

    public void setEnviroment(double enviroment) {
        this.enviroment = enviroment;
    }

    public double getPriceQuality() {
        return priceQuality;
    }

    public void setPriceQuality(double priceQuality) {
        this.priceQuality = priceQuality;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author != null && author.length() > 0) {
            this.author = author;
        }
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public double getValue(int i) {
        switch (i) {
            case 0:
                return this.service;
            case 1:
                return this.food;
            case 2:
                return this.drinking;
            case 3:
                return this.enviroment;
            case 4:
                return this.priceQuality;
            case 5:
                return this.count;
            default:
                return 0;
        }
    }
}
