package org.cuba.paladar.Model.Entities;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by jdsantana on 28/03/15.
 */

@Table(name = "language")
public class Language extends Entity {

    @TableField(name = "lang", datatype = Entity.DATATYPE_STRING)
    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
