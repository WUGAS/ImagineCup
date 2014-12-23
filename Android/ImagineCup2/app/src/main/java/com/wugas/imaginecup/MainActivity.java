package com.wugas.imaginecup;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by suhon_000 on 12/16/2014.
 */
public class MainActivity extends ActionBarActivity {
    //Framework variables
    private MyAdapter mAdapter;
    public static final int NUM_ITEMS = 3;
    private ViewPager mPager;

    //Service variables
    public Intent service;
    private boolean inService;
    private Intent connectionIntent;

    //Settings variables
    public static final String PREFS_NAME = "PrefsFile";
    SharedPreferences settings;
    private final String serviceKey = "service_pref";

    //Debugger
    public static final String TAG = "TAG";

    //Database global variable
    public static LocationOpenHelper dbHelper;

    //Status variables
    public static boolean feeling = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_main);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.swipe_main);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(1); // Map Screen

        dbHelper = new LocationOpenHelper(this);
        Button but = (Button) findViewById(R.id.website);
        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent website = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(website);
            }
        });

        // Testing Database actually just viewing the database
        Button store = (Button) findViewById(R.id.storeData);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String id = "id";
                String title =  "title";
//                String content = "content";

                //Define a projection that specifies which columns from the database you will actually use after this query.
                String[] projection = {
                        LocationContract.LocationEntry.COLUMN_NAME_LAT,
                        LocationContract.LocationEntry.COLUMN_NAME_LONG,
                };

//                String sortOrder= LocationContract.LocationEntry.

                //Read database
                Cursor c = db.query(LocationContract.LocationEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null, // group by
                        null, // having
                        null); // order by
                c.moveToFirst();
                double lat = c.getDouble(0);
                double lon = c.getDouble(1);
                Log.d(TAG, "Read data: " + Double.toString(lat) + ", " + Double.toString(lon));
                Log.d(TAG, "COUNT: " + Integer.toString(c.getCount()));
            }
        });

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        inService = settings.getBoolean(serviceKey, true);

        if (inService) {
            connectionIntent = new Intent(this, LocationService.class);
            bindService(connectionIntent, locationServiceConnection, Context.BIND_AUTO_CREATE);
            startService(connectionIntent);

        }
    }

    private ServiceConnection locationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {
            LocationService locService = ((LocationService.LocalBinder) binder).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new StatusFragment();
                case 1:
                    return new MainScreenFragment();
                case 2:
                    return new NewsFragment();
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.service);
        if (inService) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_stop));
        }
        else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_play));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.service:
                SharedPreferences.Editor edit = settings.edit();
                if (inService) {
                    unbindService(locationServiceConnection);
                    stopService(connectionIntent);
                    item.setIcon(getResources().getDrawable(R.drawable.ic_action_play));
                    edit.putBoolean(serviceKey, false);
                    inService = false;
                }
                else {
                    //if not existing, create a new connectionIntent
                    if (connectionIntent == null) {
                        connectionIntent = new Intent(this, LocationService.class);
                    }
                    bindService(connectionIntent, locationServiceConnection, Context.BIND_AUTO_CREATE);
                    startService(connectionIntent);
                    item.setIcon(getResources().getDrawable(R.drawable.ic_action_stop));
                    edit.putBoolean(serviceKey, true);
                    inService = true;
                }
                edit.commit();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    public void changePic(View view) {
        ImageView img = (ImageView) findViewById(R.id.image);
        if (feeling) {
            img.setImageResource(R.drawable.ic_bad);
            feeling = false;
        }
        else {
            img.setImageResource(R.drawable.ic_good);
            feeling = true;
        }
    }
}
