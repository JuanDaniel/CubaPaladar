package org.cuba.paladar.Model.Entities;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by jdsantana on 28/03/15.
 */

@Table(name = "province")
public class Province extends Entity {

    @TableField(name = "name", datatype = Entity.DATATYPE_STRING)
    private String name;

    @TableField(name = "phone_code", datatype = Entity.DATATYPE_STRING)
    private String phoneCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        if (phoneCode != null && phoneCode.length() > 0) {
            this.phoneCode = phoneCode;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
