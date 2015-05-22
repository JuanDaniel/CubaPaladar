package org.cuba.paladar.Utils;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public class OwnerProvider extends MapTileProviderArray {

    protected OwnerProvider(ITileSource pTileSource,
                            IRegisterReceiver pRegisterReceiver) {
        super(pTileSource, pRegisterReceiver);

        // mTileProviderList.add(arg0);
    }

}
