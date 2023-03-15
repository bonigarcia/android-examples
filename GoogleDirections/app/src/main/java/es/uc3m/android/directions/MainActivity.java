/*
 * (C) Copyright 2023 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package es.uc3m.android.directions;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        try {
            Geocoder gc = new Geocoder(this, Locale.getDefault());

            // Origin
            String origin = "Avenida de la Universidad 30 Leganés";
            List<Address> originAddresses = gc.getFromLocationName(origin, 1);
            LatLng originLatLng =
                    new LatLng(originAddresses.get(0).getLatitude(),
                            originAddresses.get(0).getLongitude());
            map.addMarker(new MarkerOptions().position(originLatLng).title(origin));

            // Destination
            String destination = "Plaza España Leganés";
            List<Address> destAddresses = gc.getFromLocationName(destination, 1);
            LatLng destLatLng =
                    new LatLng(destAddresses.get(0).getLatitude(),
                            destAddresses.get(0).getLongitude());
            map.addMarker(new MarkerOptions().position(destLatLng).title(destination));

            // Move camera
            float zoomLevel = map.getMaxZoomLevel() - 5;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, zoomLevel));

            // Directions
            // The value of the API key should be in the local.properties file:
            // MAPS_API_KEY=<your-api-key>
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(BuildConfig.MAPS_API_KEY)
                    .build();
            DirectionsResult result = DirectionsApi.newRequest(context)
                    .origin(origin)
                    .destination(destination)
                    .mode(TravelMode.WALKING)
                    .await();
            PolylineOptions polylineOptions =
                    new PolylineOptions().width(8).color(Color.BLUE);
            for (DirectionsRoute route : result.routes) {
                EncodedPolyline encodedPolyline = route.overviewPolyline;
                for (com.google.maps.model.LatLng latLng : encodedPolyline.decodePath()) {
                    polylineOptions.add(new LatLng(latLng.lat, latLng.lng));
                }
            }
            map.addPolyline(polylineOptions);

        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Exception creating route", e);
        }
    }

}