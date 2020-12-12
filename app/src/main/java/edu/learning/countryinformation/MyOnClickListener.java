package edu.learning.countryinfo;

import android.content.Intent;
import android.view.View;

public class MyOnClickListener implements View.OnClickListener {

    private MapsActivity mapsActivity;
    String country;

    public MyOnClickListener(MapsActivity mapsActivity, String country) {
        this.mapsActivity = mapsActivity;
        this.country = country;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mapsActivity.mapFrag.getContext(), CountryDetails.class);
        intent.putExtra("country", country);
        mapsActivity.startActivity(intent);
    }
}
