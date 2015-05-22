package org.cuba.paladar.Model.Entities;

import android.content.Context;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.cuba.paladar.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Table(name = "restaurant")
public class Restaurant extends Entity {

    @TableField(name = "name", datatype = Entity.DATATYPE_STRING)
    private String name;

    @TableField(name = "slogan", datatype = Entity.DATATYPE_STRING, required = false)
    private String slogan;

    @TableField(name = "ranking_cpl", datatype = Entity.DATATYPE_INTEGER)
    private int rankingCpl;

    @TableField(name = "ranking_p", datatype = Entity.DATATYPE_INTEGER)
    private int rankingP;

    @TableField(name = "address_id", datatype = Entity.DATATYPE_ENTITY_REFERENCE)
    private RestaurantAddress address;

    @TableField(name = "phones", datatype = Entity.DATATYPE_STRING, required = false)
    private String phones;

    @TableField(name = "cell", datatype = Entity.DATATYPE_STRING, required = false)
    private String cell;

    @TableField(name = "email", datatype = Entity.DATATYPE_STRING, required = false)
    private String email;

    @TableField(name = "web", datatype = Entity.DATATYPE_STRING, required = false)
    private String web;

    @TableField(name = "facebook", datatype = Entity.DATATYPE_STRING, required = false)
    private String facebook;

    @TableField(name = "hours", datatype = Entity.DATATYPE_STRING, required = false)
    private String hours;

    @TableField(name = "code", datatype = Entity.DATATYPE_STRING)
    private String code;

    @TableField(name = "aval", datatype = Entity.DATATYPE_BOOLEAN)
    private boolean aval;

    @TableField(name = "images", datatype = Entity.DATATYPE_ENTITY_LINK)
    private List<Image> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            this.name = name;
        }
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        if (slogan != null && slogan.length() > 0) {
            this.slogan = slogan;
        }
    }

    public int getRankingCpl() {
        return rankingCpl;
    }

    public void setRankingCpl(int rankingCpl) {
        this.rankingCpl = rankingCpl;
    }

    public int getRankingP() {
        return rankingP;
    }

    public void setRankingP(int rankingP) {
        this.rankingP = rankingP;
    }

    public String getPhones() {
        if (phones != null) {
            return phones.replace('.', ' ');
        }

        return null;
    }

    public void setPhones(String phones) {
        if (phones != null && phones.length() > 0) {
            this.phones = phones;
        }
    }

    public String getCell() {
        if (cell != null) {
            return cell.replace('.', ' ');
        }

        return null;
    }

    public void setCell(String cell) {
        if (cell != null && cell.length() > 0) {
            this.cell = cell;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.length() > 0) {
            this.email = email;
        }
    }

    public String getWeb() {
        if (web != null) {
            return web;
        } else if (facebook != null) {
            return facebook;
        }

        return null;
    }

    public void setWeb(String web) {
        if (web != null && web.length() > 0) {
            this.web = web;
        }
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        if (facebook != null && facebook.length() > 0) {
            this.facebook = facebook;
        }
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        if (hours != null && hours.length() > 0) {
            this.hours = hours;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code != null && code.length() > 0) {
            this.code = code;
        }
    }

    public boolean isAval() {
        return aval;
    }

    public void setAval(boolean aval) {
        this.aval = aval;
    }

    public RestaurantAddress getAddress() {
        return address;
    }

    public void setAddress(RestaurantAddress address) {
        this.address = address;
    }

    public List getImages() {
        return images;
    }

    public void setImages(List images) {
        this.images = images;
    }

    public ArrayList<String> getHoursTranslate(Context context) {
        ArrayList<String> hours = new ArrayList<String>();
        try {
            JSONArray data = new JSONArray(this.hours);
            JSONObject line;
            String token;
            String row;
            for (int i = 0, c = data.length(); i < c; i++) {
                line = data.getJSONObject(i);
                row = "";

                // Token from
                token = line.getString("from");
                row += getTokenHours(token, context) + " - ";

                // Token to
                token = line.getString("to");
                row += getTokenHours(token, context) + " ";

                row += line.getString("fromh");
                row += line.getString("fromhd") + " - ";
                row += line.getString("toh");
                row += line.getString("tohd");

                hours.add(row);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return hours;
    }

    private String getTokenHours(String token, Context context) {
        if (token.equals("rest_field_hours_mon")) {
            return context.getString(R.string.rest_field_hours_mon);
        } else if (token.equals("rest_field_hours_sat")) {
            return context.getString(R.string.rest_field_hours_sat);
        } else if (token.equals("rest_field_hours_sun")) {
            return context.getString(R.string.rest_field_hours_sun);
        }

        return "";
    }
}
