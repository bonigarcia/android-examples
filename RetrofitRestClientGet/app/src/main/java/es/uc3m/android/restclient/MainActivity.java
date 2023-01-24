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
package es.uc3m.android.restclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.uc3m.android.gorest.User;
import es.uc3m.android.gorest.UsersService;
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

        getUsers(listAdapter, nameList, emailList);
    }

    private void getUsers(ArrayAdapter listAdapter, List<String> nameList, List<String> emailList) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gorest.co.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsersService usersService = retrofit.create(UsersService.class);
        Call<List<User>> call = usersService.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                for (User user : response.body()) {
                    nameList.add(user.getName());
                    emailList.add(user.getEmail());
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "Exception calling endpoint", t);
            }
        });
    }
}