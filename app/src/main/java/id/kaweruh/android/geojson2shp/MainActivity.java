/*
 *
 *  A simple Android app to convert GeoJSON to SHP and vice versa using GDAL
 *
 *  Last Update:
 *  - 20190402 - Bambang Setiadi <bambang@gmail.com>
 *
 * */
package id.kaweruh.android.geojson2shp;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.database.sqlite.SQLiteCursorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;

    String  finputname = "";
    String  foutputname = "";
    TextView t1, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            String str = "android.permission.WRITE_EXTERNAL_STORAGE";
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, str);
            ArrayList arrayList = new ArrayList();
            if (checkSelfPermission != 0) {
                arrayList.add(str);
            }
            if (arrayList.isEmpty()) {
                b();
                return;
            } else {
                requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 1);
                return;
            }
        }
        b();
    }

    public Context getContext() {
        return (Context)this;
    }

    public void b() {
        t1 = (TextView) findViewById(R.id.txtInput);
        t2 = (TextView) findViewById(R.id.txtOutput);

        //
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("GEOJSON2SHP", "onClick");

                final Intent intent = new Intent();
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1359);// Activity is started with requestCode 2

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String resultFile;
                Log.d("GEOJSON2SHP", "button2 onClick");

                if ((finputname != null ) && (finputname.length()>0)) {

                    foutputname = SConverter.GeoJSON2SHP(finputname);
                    if (foutputname.length() != 0 ) {
                        Toast.makeText(MainActivity.this, "File has been created in: " + foutputname, Toast.LENGTH_LONG).show();
                        t2.setVisibility(View.VISIBLE);
                        t2.setText("Output file: " + foutputname);
                    } else {
                        t2.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "File creation failed.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Silakan pilih file .json dulu.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if ((intent != null) && ( i == 1359 )) {
            Uri resultUri = intent.getData();
            try {
                finputname = getFilePath(this, resultUri);
                Log.d("CreateSHP", "Uri = " + resultUri.toString());
                Log.d("CreateSHP", "fname = " + finputname);
                this.t1.setText("Input file: " + this.finputname);
                this.button2.setVisibility(View.VISIBLE);
                this.t2.setVisibility(View.VISIBLE);
                this.t2.setText("");
            } catch (URISyntaxException e){
                Log.e("CreateSHP", "Input file selection error.");
            } catch(Exception e) {
                Log.e("CreateSHP", "Input file selection error.");
            }
        } else {
            super.onActivityResult(i, i2, intent);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        int i2 = 0;
        switch (i) {
            case 1:
                int i3 = 0;
                while (i2 < strArr.length) {
                    Log.e("PERMISION", "permissions" + strArr.length);
                    Log.e("PERMISION GRANTED", "granted " + iArr.length);
                    if (iArr[i2] == 0) {
                        i3++;
                    }
                    i2++;
                }
                if (strArr.length == i3) {
                    b();
                    return;
                } else {
                    Toast.makeText(this, "You need to accept permissions to get this app running properly running.\nPlease restart the app and accept required permissions...", Toast.LENGTH_LONG).show();
                    return;
                }
            default:
                return;
        }
    }

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
