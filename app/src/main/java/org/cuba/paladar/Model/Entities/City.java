package org.cuba.paladar.Model.Entities;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by jdsantana on 28/03/15.
 */

@Table(name = "city")
public class City extends Entity {

    @TableField(name = "name", datatype = Entity.DATATYPE_STRING)
    private String name;

    @TableField(name = "province_id", datatype = Entity.DATATYPE_ENTITY_REFERENCE)
    private Province province;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
