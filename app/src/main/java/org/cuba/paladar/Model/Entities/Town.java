package org.cuba.paladar.Model.Entities;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by jdsantana on 28/03/15.
 */

@Table(name = "town")
public class Town extends Entity {

    @TableField(name = "name", datatype = Entity.DATATYPE_STRING)
    private String name;

    @TableField(name = "city_id", datatype = Entity.DATATYPE_ENTITY_REFERENCE)
    private City city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
