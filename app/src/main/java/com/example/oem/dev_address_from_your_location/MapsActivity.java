package com.example.oem.dev_address_from_your_location;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    IntentFilter filter;
    ConnectivityManager cm;
    static String phonecode;
    static String postalcode;
    static String Countrycode;
    static String daddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new Receiver(), filter);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 9051);
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 9051);
        } else {
            passed();
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void passed() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double longt = location.getLongitude();
                    LatLng ll = new LatLng(lat, longt);
                    mMap.addMarker(new MarkerOptions().position(ll).title("You are here !"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10.2f));
                    Geocoder gc = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses = gc.getFromLocation(lat, longt, 1);
                         Countrycode = addresses.get(0).getCountryCode();
                         phonecode = addresses.get(0).getPhone();
                         postalcode = addresses.get(0).getPostalCode();
                         daddress=addresses.get(0).getAddressLine(0);
                        DialogInterface.OnClickListener dicl =new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Intent i=new Intent(MapsActivity.this,dis.class);
                                        startActivity(i);break;
                                    case DialogInterface.BUTTON_NEGATIVE:break;
                                }
                            }
                        };
                        AlertDialog.Builder adb=new AlertDialog.Builder(MapsActivity.this);
                        adb.setMessage("Want the Details ?").setPositiveButton("YES",dicl).setNegativeButton("No",dicl).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat=location.getLatitude();
                    double longt=location.getLongitude();
                    LatLng ll=new LatLng(lat,longt);
                    mMap.addMarker(new MarkerOptions().position(ll).title("You are here !"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,10.2f));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

    }
    public class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder adb=new AlertDialog.Builder(MapsActivity.this);
            NetworkInfo ni=cm.getActiveNetworkInfo();
            if(ni==null)
                adb.setMessage("Not Connected to Internet !").show();
        }
    }

}

