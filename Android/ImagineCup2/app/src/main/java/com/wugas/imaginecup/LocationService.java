package com.wugas.imaginecup;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = "LocationService";

    private TextView loc;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private boolean currentlyProcessingLocation = false;

    private final static int UPDATE_LOCATION_TIME_MILLIS = 2000; //How often to update location

    //Define request code to send to Google Play Services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

//    public LocationService() {
//    }

    @Override
    public void onCreate() {
        // Service is being created
        Toast.makeText(this, "Service Creation", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Connection start command", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.connect();
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_LOCATION_TIME_MILLIS); //update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Location save: " + location.toString(), Toast.LENGTH_SHORT).show();
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LAT, lat);
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LONG, lon);

        long newRowId;
        newRowId = db.insert(LocationContract.LocationEntry.TABLE_NAME,
                null, values);
        Log.d(TAG, Long.toString(newRowId));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        showErrorDialog(connectionResult.getErrorCode());
        //Need to implement this somehow
    }

}
