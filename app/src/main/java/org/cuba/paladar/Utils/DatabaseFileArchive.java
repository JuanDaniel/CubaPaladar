package org.cuba.paladar.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class DatabaseFileArchive implements IArchiveFile {

    private static final Logger logger = LoggerFactory
            .getLogger(DatabaseFileArchive.class);

    private final SQLiteDatabase mDatabase;

    private final int zoomMax = 17;
    private final int zoomMin = 14;

    private DatabaseFileArchive(final SQLiteDatabase pDatabase) {
        mDatabase = pDatabase;
    }

    public static DatabaseFileArchive getDatabaseFileArchive(final File pFile)
            throws SQLiteException {
        return new DatabaseFileArchive(SQLiteDatabase.openOrCreateDatabase(
                pFile, null));
    }

    @Override
    public InputStream getInputStream(final ITileSource pTileSource,
                                      final MapTile pTile) {
        try {
            InputStream ret = null;
            int x = pTile.getX();
            int y = pTile.getY();
            int z = zoomMax - pTile.getZoomLevel();
            Cursor cur = mDatabase.rawQuery(
                    "SELECT image FROM tiles WHERE x=? AND y=? AND z=?",
                    new String[]{String.valueOf(x), String.valueOf(y),
                            String.valueOf(z),});
            if (cur.getCount() != 0) {
                cur.moveToFirst();
                ret = new ByteArrayInputStream(cur.getBlob(0));
            }
            cur.close();
            if (ret != null) {
                return ret;
            }
            /*
			 * else{ System.out.println("SELECT image FROM tiles WHERE x=" +
			 * String.valueOf(x) + " AND y=" + String.valueOf(y) + " AND z=" +
			 * String.valueOf(z)); }
			 */
        } catch (final Throwable e) {
            logger.warn("Error getting db stream: " + pTile, e);
        }

        return null;
    }

    @Override
    public String toString() {
        return "DatabaseFileArchive [mDatabase=" + mDatabase.getPath() + "]";
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

}
