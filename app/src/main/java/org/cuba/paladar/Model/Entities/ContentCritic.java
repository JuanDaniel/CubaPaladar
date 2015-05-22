package org.cuba.paladar.Model.Entities;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by jdsantana on 28/03/15.
 */

@Table(name = "content_critic")
public class ContentCritic extends Entity {

    @TableField(name = "content", datatype = Entity.DATATYPE_STRING)
    private String content;

    @TableField(name = "lang_id", datatype = Entity.DATATYPE_ENTITY_REFERENCE)
    private Language lang;

    @TableField(name = "restaurant_critic_id", datatype = Entity.DATATYPE_ENTITY_REFERENCE)
    private RestaurantCritic restaurantCritic;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content != null && content.length() > 0) {
            this.content = content;
        }
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public RestaurantCritic getRestaurantCritic() {
        return restaurantCritic;
    }

    public void setRestaurantCritic(RestaurantCritic restaurantCritic) {
        this.restaurantCritic = restaurantCritic;
    }
}
