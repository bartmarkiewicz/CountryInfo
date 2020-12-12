package edu.learning.countryinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import edu.learning.fit2081.countryinfo.R;

import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class CountryDetails extends AppCompatActivity {

    private TextView name;
    private TextView capital;
    private TextView code;
    private TextView population;
    private TextView area;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_details);

        getSupportActionBar().setTitle(R.string.title_activity_country_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String selectedCountry = getIntent().getStringExtra("country");

        name = findViewById(R.id.country_name);
        capital =  findViewById(R.id.capital);
        code =  findViewById(R.id.country_code);
        population =  findViewById(R.id.population);
        area = findViewById(R.id.area);

        new GetCountryDetails().execute(selectedCountry);
    }


    private class GetCountryDetails extends AsyncTask<String, String, CountryInfo> {

        @Override
        protected CountryInfo doInBackground(String... params) {
            CountryInfo countryInfo = null;
            try {
                String selectedCountry = params[0];
                URL webServiceEndPoint = new URL("https://restcountries.eu/rest/v2/name/" + selectedCountry); //

                HttpsURLConnection myConnection = (HttpsURLConnection) webServiceEndPoint.openConnection();

                if (myConnection.getResponseCode() == 200) {
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginArray();
                    String keyName;
                    countryInfo = new CountryInfo();
                    boolean countryFound = false;
                    while (jsonReader.hasNext() && !countryFound) {
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            keyName = jsonReader.nextName();
                            if (keyName.equals("name")) {
                                countryInfo.setName(jsonReader.nextString());
                                if (countryInfo.getName().equalsIgnoreCase(selectedCountry)) {
                                    countryFound = true;
                                }
                            } else if (keyName.equals("alpha2Code")){
                                countryInfo.setAlpha2Code(jsonReader.nextString());
                            }
                            else if (keyName.equals("alpha3Code")) {
                                countryInfo.setAlpha3Code(jsonReader.nextString());

                            } else if (keyName.equals("capital")) {
                                countryInfo.setCapital(jsonReader.nextString());
                            } else if (keyName.equals("population")) {
                                countryInfo.setPopulation(jsonReader.nextInt());
                            } else if (keyName.equals("area")) {
                                countryInfo.setArea(jsonReader.nextDouble());
                            } else {
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                } else {
                    Log.i("INFO", "Error:  No response");
                }
                Button wikiBt = findViewById(R.id.wikiBt);
                wikiBt.setText("WIKI " + countryInfo.getName());
                final String wikiURL = "https://en.wikipedia.org/wiki/" + countryInfo.getName();
                wikiBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), WikiActivity.class);
                        intent.putExtra("wikiURL", wikiURL);
                        startActivity(intent);
                    }
                });
                //networking logic
                ImageView flagView = findViewById(R.id.countryFlagImage);
                URL url = new URL("https://www.countryflags.io/" + countryInfo.getAlpha2Code() + "/flat/64.png");
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream buff = new BufferedInputStream(inputStream);
                Bitmap bitmap = BitmapFactory.decodeStream(buff); // decodes the inputstream into a bitmap
                flagView.setImageBitmap(bitmap);
                buff.close();
                inputStream.close(); // closes the streams to prevent memory leaks





            } catch (Exception e) {
                Log.i("INFO", "Error " + e.toString());
            }
            return countryInfo;
        }

        @Override
        protected void onPostExecute(CountryInfo countryInfo) {
            super.onPostExecute(countryInfo);
            name.setText(countryInfo.getName());
            capital.setText(countryInfo.getCapital());
            code.setText(countryInfo.getAlpha3Code());
            population.setText(Integer.toString(countryInfo.getPopulation()));
            area.setText(Double.toString(countryInfo.getArea()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CountryInfo {
        private String name;
        private String alpha3Code;
        private String capital;
        private int population;
        private double area;
        private String alpha2Code;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getAlpha2Code() {
            return alpha2Code;
        }

        public String getAlpha3Code() {
            return alpha3Code;
        }

        public void setAlpha3Code(String alpha3Code) {
            this.alpha3Code = alpha3Code;
        }

        public void setAlpha2Code(String alpha2Code){
            this.alpha2Code = alpha2Code;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public int getPopulation() {
            return population;
        }

        public void setPopulation(int population) {
            this.population = population;
        }

        public double getArea() {
            return area;
        }

        public void setArea(double area) {
            this.area = area;
        }
    }
}
