package com.example.cosmin.dont;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.TimeZone;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String TITLE = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        Calendar rightNow = Calendar.getInstance();

        rightNow.setTimeZone(TimeZone.getTimeZone("EET"));

        /*int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        if (hour<6 || hour >21) {
            mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        }*/
        mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        //System.out.print("aaaaaa"+hour);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.755040, 23.578011), 14));

        LatLng latLng = new LatLng(46.753251, 23.581890);
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Auchan"));

        LatLng carr = new LatLng(46.755043, 23.578013);
        mMap.addMarker(new MarkerOptions()
                .position(carr)
                .title("Carrefour"));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent (MapsActivity.this, ProductsList.class);
                String mss = marker.getTitle();
                intent.putExtra(TITLE ,mss);
                startActivity(intent);
                return false;
            }
        });

    }
}