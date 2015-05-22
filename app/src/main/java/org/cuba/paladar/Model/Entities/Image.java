package org.cuba.paladar.Model.Entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.ByteArrayInputStream;

/**
 * Created by jdsantana on 28/03/15.
 */

@Table(name = "image")
public class Image extends Entity {

    @TableField(name = "image", datatype = Entity.DATATYPE_BLOB)
    private byte[] image;

    public Bitmap getImage() {
        Bitmap image = null;

        if (this.image != null) {
            ByteArrayInputStream imgStream = new ByteArrayInputStream(this.image);
            try {
                image = BitmapFactory.decodeStream(imgStream);
            } catch (Exception ex) {
                image = null;
            }
        }

        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
