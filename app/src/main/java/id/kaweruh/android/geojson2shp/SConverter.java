/*
 *
 *  geojson2shp - library to convert GeoJSON to SHP and vice versa using GDAL
 *
 *  Last Update:
 *  - 20190402 - Bambang Setiadi <bambang@gmail.com>
 *
 * */

package id.kaweruh.android.geojson2shp;
import android.os.Environment;
import android.util.Log;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.ogr;

import java.io.File;
import java.lang.String;

public final class SConverter {


    private static final String TAG = "SpatialConv";

    /**
     * should exist demo.tab, demo.map, demo.dat, demo.id
     *
     * Ref: https://gdal.org/gdal.pdf
     * Ref: https://trac.osgeo.org/gdal/wiki/BuildingForAndroid
     * Ref: https://github.com/wshunli/gdal-android
     * Ref: https://github.com/OSGeo/gdal
     *
     * @return ...
     */
    public static String convertTab2Shp(String tabFilePath) {
        Log.i(TAG, "convertTab2Shp()");

        String strInputFile = tabFilePath;
        String strOutputFile = "";

        Log.i(TAG, "input file: " + strInputFile);

        ogr.RegisterAll();

        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");

        // chinese support
        gdal.SetConfigOption("SHAPE_ENCODING", "");

        int lastIndexOf = strInputFile.lastIndexOf('.');
        if (lastIndexOf == -1) {
            strOutputFile = strInputFile + ".shp";
        } else {
            strOutputFile = strInputFile.substring(0, lastIndexOf) + ".shp";
        }
        //open data
        Log.d(TAG, "input file: " + strInputFile);
        Log.d(TAG, "output file: " + strOutputFile);

        DataSource ds = ogr.Open(strInputFile, 0);
        if (ds == null) {
            Log.e(TAG, "Failed opening TAB file.");
            return "";
        }
        Log.d(TAG, "TAB file opened succesfully.");
        Driver dv = ogr.GetDriverByName("ESRI Shapefile");
        if (dv == null) {
            Log.e(TAG, "ESRI Shapefile driver loading error.");
            return "";
        }
        Log.d(TAG, "Shapefile driver loaded succefully.");
        dv.CopyDataSource(ds, strOutputFile);
        Log.d(TAG, "TAB file converted to SHP succefully.");


        return strOutputFile;
    }


    /**
     * should exist demo.tab, demo.map, demo.dat, demo.id
     *
     * Ref: https://gdal.org/gdal.pdf
     * Ref: https://trac.osgeo.org/gdal/wiki/BuildingForAndroid
     * Ref: https://github.com/wshunli/gdal-android
     * Ref: https://github.com/OSGeo/gdal
     *
     * @return ...
     */
    public static String convertTab2Kml(String tabFilePath) {
        Log.i(TAG, "convertTab2Kml()");

        String strInputFile = tabFilePath;
        String strOutputFile = "";

        Log.i(TAG, "input file: " + strInputFile);

        ogr.RegisterAll();

        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");

        // chinese support
        gdal.SetConfigOption("SHAPE_ENCODING", "");

        int lastIndexOf = strInputFile.lastIndexOf('.');
        if (lastIndexOf == -1) {
            strOutputFile = strInputFile + ".kml";
        } else {
            strOutputFile = strInputFile.substring(0, lastIndexOf) + ".kml";
        }
        //open data
        Log.d(TAG, "input file: " + strInputFile);
        Log.d(TAG, "output file: " + strOutputFile);

        DataSource ds = ogr.Open(strInputFile, 0);
        if (ds == null) {
            Log.e(TAG, "Failed opening TAB file.");
            return "";
        }
        Log.d(TAG, "TAB file opened succesfully.");
        Driver dv = ogr.GetDriverByName("KML");
        if (dv == null) {
            Log.e(TAG, "KML driver loading error.");
            return "";
        }
        Log.d(TAG, "KML driver loaded succefully.");
        dv.CopyDataSource(ds, strOutputFile);
        Log.d(TAG, "TAB file converted to KML succefully.");


        return strOutputFile;
    }



    // convert GeoJSON file to SHP
    public static String GeoJSON2SHP (String strInputFile) {
        String strOutputFile = "";
        if (strInputFile.toLowerCase().endsWith(".geojson")) {
            ogr.RegisterAll();

            gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");

            // chinese support
            gdal.SetConfigOption("SHAPE_ENCODING", "");

            int lastIndexOf = strInputFile.lastIndexOf('.');
            if (lastIndexOf == -1) {
                strOutputFile = strInputFile + ".shp";
            } else {
                strOutputFile = strInputFile.substring(0, lastIndexOf) + ".shp";
            }
            //open data
            Log.d( "SpatialConv", strInputFile);
            Log.d( "SpatialConv", strOutputFile);
            DataSource ds = ogr.Open(strInputFile, 0);
            if (ds == null) {
                Log.e("SpatialConv", "Failed opening GeoJSON file.");
                return "";
            }
            Log.d("SpatialConv", "GeoJSON file opened succesfully.");
            Driver dv = ogr.GetDriverByName("ESRI Shapefile");
            if (dv == null) {
                Log.e("SpatialConv", "GeoJSON driver loading error.");
                return "";
            }
            Log.d("SpatialConv", "Shapefile driver loaded succefully.");
            dv.CopyDataSource(ds,  strOutputFile );
            Log.d("SpatialConv", "GeoJSON file converted to SHP succefully.");
        } else if (strInputFile.toLowerCase().endsWith(".shp")) {
            ogr.RegisterAll();

            gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");

            // chinese support
            gdal.SetConfigOption("SHAPE_ENCODING", "");

            //open data
            Log.d( "SpatialConv", strInputFile);
            DataSource ds = ogr.Open(strInputFile, 0);
            if (ds == null) {
                Log.e("SpatialConv", "Failed opening Shapefile.");
                return "";
            }
            Log.d("SpatialConv", "SHP file opened succesfully.");
            Driver dv = ogr.GetDriverByName("GeoJSON");
            if (dv == null) {
                Log.e("SpatialConv", "OGR driver loading error.");
                return "";
            }
            Log.d("SpatialConv", "OGR driver loaded succefully.");

            int lastIndexOf = strInputFile.lastIndexOf('.');
            if (lastIndexOf == -1) {
                strOutputFile = (new StringBuilder()).append(strInputFile).append(".geojson").toString();
            } else {
                strOutputFile = (new StringBuilder()).append(strInputFile.substring(0, lastIndexOf)).append(".geojson").toString();
            }

            dv.CopyDataSource(ds,  strOutputFile );
            Log.d("SpatialConv", "SHP file converted to GeoJSON file succefully.");
        } else {
            Log.d("SpatialConv","Error file name.");
        }
        return strOutputFile;
    }

    // todo: ubah shp menjadi geojson
    public static String SHP2GeoJSON (String srcName) {
        return "";
    }
}
