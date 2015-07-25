package com.example.keane.hackathonsg;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsTestActivity extends FragmentActivity implements OnMapReadyCallback {

    double latitude;
    double longtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_test);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String addressStr = "Sainta Augustine,FL,4405 Avenue A";
        Geocoder geoCoder = new Geocoder(MapsTestActivity.this, Locale.getDefault());

        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(addressStr, 1);
            if (addresses.size() >  0) {
                latitude = addresses.get(0).getLatitude(); longtitude =
                        addresses.get(0).getLongitude(); }

        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace(); }



    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng pos = new LatLng(latitude, longtitude);
        // Add a marker in Singapore, and move the camera.
        map.addMarker(new MarkerOptions().position(pos).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(pos));//, 10);
    }
}
