/*
 * (C) Copyright 2022 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.geocoding;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String INTRO = "\r\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
            String myAddress = "UC3M Leganés";
            List<Address> addresses = gc.getFromLocationName(myAddress, 10);
            String addressesStr = myAddress + ":" + INTRO;
            for (Address address : addresses) {
                addressesStr += address.getAddressLine(0) + INTRO;
                addressesStr +=
                        address.getLatitude() + ", " + address.getLongitude() + INTRO;
            }
            TextView textView = findViewById(R.id.msg);
            textView.setText(addressesStr);

        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "Exception invoking geocoder", e);
        }
    }

}
