package edu.learning.countryinfo;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import edu.learning.fit2081.countryinfo.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Geocoder geocoder;

    SupportMapFragment mapFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setTitle(R.string.title_activity_maps);

        geocoder = new Geocoder(this, Locale.getDefault());

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        LatLng melbourne = new LatLng(-37.814, 144.96332);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //save current location
                String msg;
                boolean actionFlag;
                String selectedCountry = "";


                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses.size() == 0) {
                    msg = "No country at this location!!";
                    actionFlag = false;
                }
                else {
                    android.location.Address address = addresses.get(0);
                    selectedCountry = address.getCountryName();
                    msg = "Do you want more details about " + address.getCountryName() + "?";
                    actionFlag = true;
                }

                Snackbar.make(mapFrag.getView(), msg, Snackbar.LENGTH_LONG)
                        .setAction("Details", (actionFlag) ? (new MyOnClickListener(MapsActivity.this, selectedCountry)) : null)
                        .show();
            }
        });
    }

}