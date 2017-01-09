package com.projects.kevinbarassa.emergencyresponder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.arsy.maps_library.MapRipple;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.support.v4.app.Fragment;

/**
 * Created by Kevin Barassa on 09-Jan-17.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GPSTracker gps;
    GoogleMap nMap;
    MapRipple mapRipple;
    private LocationTracker locationTrackObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // create class object
        gps = new GPSTracker(MapsActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            LatLng latLng = new LatLng(gps.getLatitude(), gps.getLongitude());

            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + gps.getLatitude() + "\nLong: " + gps.getLongitude(), Toast.LENGTH_LONG).show();
            //Create Map
            mapRipple = new MapRipple(nMap, latLng, this);
            mapRipple.startRippleMapAnimation();      //in onMapReadyCallBack

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);



        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        locationTrackObj = new LocationTracker(this);
        if (!locationTrackObj.canGetLocation()) {
            gps.showSettingsAlert();
        }

    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


    public void onLocationChanged(Location location) {
        mapRipple.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private class LocationTracker implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        private boolean isGPSEnabled = false;

        // flag for network status
        private boolean isNetworkEnabled = false;

        // flag for GPS status
        private boolean canGetLocation = false;

        private Location location; // location
        private double latitude; // latitude
        private double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000; // 1 sec

        private final String TAG = "LocationTracker";
        // Declaring a Location Manager
        protected LocationManager locationManager;

        public LocationTracker(Context context) {
            this.mContext = context;
            getLocation();
        }
        /**
         * Function to check GPS/wifi enabled
         *
         * @return boolean
         */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                    this.canGetLocation = false;
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            return location;
        }

        @Override
        public void onLocationChanged(Location location) {
            //            mapRipple.withNumberOfRipples(3);
            this.location = location;
//            Toast.makeText(context, "  " + location.getLatitude() + ",  " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            mapRipple.withLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            location = getLocation();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        /**
         * Function to get latitude
         */
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         */
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

    }
}