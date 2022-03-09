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
package io.github.bonigarcia.android.restclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.github.bonigarcia.android.places.PlacesService;
import io.github.bonigarcia.android.places.Result;
import io.github.bonigarcia.android.places.Root;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNearby();
    }

    private void getNearby() {
        List<String> nameList = new ArrayList<>();
        List<String> emailList = new ArrayList<>();
        ArrayAdapter listAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, nameList);
        ListView list = findViewById(R.id.list);
        list.setAdapter(listAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int pos, long id) {
                Toast.makeText(view.getContext(), emailList.get(pos),
                        Toast.LENGTH_SHORT).show();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PlacesService usersService = retrofit.create(PlacesService.class);

        float latitude = 40.341442F;
        float longitude = -3.762217F;
        String location = latitude + "," + longitude;
        int radius = 1000;
        String type = "atm";
        boolean sensor = true;

        // The value of the the API key should be in the local.properties file:
        // MAPS_API_KEY=<your-api-key>
        String key = BuildConfig.MAPS_API_KEY;

        Call<Root> call = usersService.nearbySearch(location, radius, type,
                sensor, key);

        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call,
                                   Response<Root> response) {
                for (Result result : response.body().results) {
                    nameList.add(result.name);
                    emailList.add(result.vicinity);
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "Exception calling endpoint", t);
            }
        });
    }
}