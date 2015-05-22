package org.cuba.paladar.Model.Entities;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by jdsantana on 28/03/15.
 */

@Table(name = "restaurant_address")
public class RestaurantAddress extends Entity {

    @TableField(name = "town_id", datatype = Entity.DATATYPE_ENTITY_REFERENCE)
    private Town town;

    @TableField(name = "latitude", datatype = Entity.DATATYPE_DOUBLE)
    private double latitude;

    @TableField(name = "longitude", datatype = Entity.DATATYPE_DOUBLE)
    private double longitude;

    @TableField(name = "street", datatype = Entity.DATATYPE_STRING)
    private String street;

    @TableField(name = "num_house", datatype = Entity.DATATYPE_STRING)
    private String numHouse;

    @TableField(name = "between_a", datatype = Entity.DATATYPE_STRING)
    private String betweenA;

    @TableField(name = "between_b", datatype = Entity.DATATYPE_STRING)
    private String betweenB;

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (street != null && street.length() > 0) {
            this.street = street;
        }
    }

    public String getNumHouse() {
        return numHouse;
    }

    public void setNumHouse(String numHouse) {
        if (numHouse != null && numHouse.length() > 0) {
            this.numHouse = numHouse;
        }
    }

    public String getBetweenA() {
        return betweenA;
    }

    public void setBetweenA(String betweenA) {
        if (betweenA != null && betweenA.length() > 0) {
            this.betweenA = betweenA;
        }
    }

    public String getBetweenB() {
        return betweenB;
    }

    public void setBetweenB(String betweenB) {
        if (betweenB != null && betweenB.length() > 0) {
            this.betweenB = betweenB;
        }
    }

    public String __toString() {
        String address = "";
        boolean previous[] = {
                /* street */ false,
                /* betweenA */ false,
                /* betweenB */ false,
                /* town */ false,
        };

        if (this.street != null) {
            address += this.street;
            previous[0] = true;
        }
        if (this.betweenA != null) {
            if (previous[0]) {
                address += " e/ ";
            }
            address += this.betweenA;
            previous[1] = true;

            if (this.betweenB != null) {
                address += " y " + this.betweenB;
                previous[2] = true;
            }
        } else if (this.betweenB != null) {
            if (previous[0]) {
                address += " e/ ";
            }
            address += this.betweenB;
            previous[2] = true;
        }
        if (this.town != null) {
            String townName = this.town.getName();
            if (townName != null) {
                if (previous[0] || previous[1] || previous[2]) {
                    address += ", ";
                }
                address += townName;
                previous[3] = true;
            }

            City city = this.town.getCity();
            if (city != null) {
                String cityName = city.getName();
                if (cityName != null) {
                    if (previous[3]) {
                        address += ", ";
                    }
                    address += city.getName();
                }
            }
        }

        return address;
    }
}
