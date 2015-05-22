package org.cuba.paladar.Utils;

import android.content.Context;
import android.util.Pair;

import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.modules.ArchiveFileFactory;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GEMFFile;
import org.osmdroid.views.util.constants.MapViewConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class MapTileProviderFactory implements MapViewConstants {

    /**
     * This is a utility class with only static members.
     */
    private MapTileProviderFactory() {
    }

    /**
     * Get a tile provider by scanning the pre-defined data path directory for
     * stored files.
     *
     * @param aContext the context
     * @param baseName the base name of the layer to provide tiles for
     */
    public static MapTileProviderBase getInstance(final Context aContext,
                                                  final String baseName) {

        // list the archive files available
        final File oamPath = new File("/sdcard/CubaPaladar/map/");

        final ArrayList<IArchiveFile> archiveFiles = new ArrayList<IArchiveFile>();

        final File[] files = oamPath.listFiles();
        if (files != null) {
            for (final File file : files) {
                // if (file.getName().startsWith(baseName)) {
                final IArchiveFile archiveFile = ArchiveFileFactory
                        .getArchiveFile(file);
                if (archiveFile != null) {
                    archiveFiles.add(archiveFile);
                }
                // }
            }
        }

        IArchiveFile[] aFiles = new IArchiveFile[archiveFiles.size()];
        aFiles = archiveFiles.toArray(aFiles);

        MapTileFileArchiveProvider mtfap = new MapTileFileArchiveProvider(
                new SimpleRegisterReceiver(aContext.getApplicationContext()),
                null, aFiles);

        MapTileModuleProviderBase[] tileProviders = new MapTileModuleProviderBase[1];
        tileProviders[0] = mtfap;

        MapTileProviderArray provider = new MapTileProviderArray(null,
                new SimpleRegisterReceiver(aContext.getApplicationContext()),
                tileProviders);

        return provider;
    }

    public static Pair<Integer, Integer> getMinMaxZoomLevels(
            final Context aContext, final String baseName) {
        // list the archive files available
        final File oamPath = new File("/sdcard/CubaPaladar/map/");

        final File[] files = oamPath.listFiles();
        int minZoom = Integer.MAX_VALUE;
        int maxZoom = Integer.MIN_VALUE;
        if (files != null) {
            for (final File file : files) {
                // if (file.getName().startsWith(baseName)) {
                try {
                    GEMFFile gf = new GEMFFile(file);
                    Set<Integer> zoomLevels = gf.getZoomLevels();
                    for (int i : zoomLevels) {
                        minZoom = Math.min(minZoom, i);
                        maxZoom = Math.max(maxZoom, i);
                    }
                } catch (IOException e) {
                }
                // }
            }
        }

        return new Pair<Integer, Integer>(minZoom, maxZoom);
    }
}
