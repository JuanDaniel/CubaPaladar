package org.cuba.paladar.Utils;

import org.osmdroid.ResourceProxy.string;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;

public class TileSource extends BitmapTileSourceBase {

    public TileSource(String aName, string aResourceId, int aZoomMinLevel,
                      int aZoomMaxLevel, int aTileSizePixels, String aImageFilenameEnding) {
        super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel,
                aTileSizePixels, aImageFilenameEnding);
    }

}
