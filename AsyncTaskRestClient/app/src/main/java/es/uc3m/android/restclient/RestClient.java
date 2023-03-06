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

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class RestClient extends AsyncTask<String, Void, String> {

    private ListView listView;

    public RestClient(ListView listView) {
        this.listView = listView;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = null;
        try {
            URL url = new URL(urls[0]);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            result = response.toString();

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Exception reading URL", e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        List<String> nameList = new ArrayList<>();
        List<String> emailList = new ArrayList<>();
        ArrayAdapter listAdapter = new ArrayAdapter(listView.getContext(),
                android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(
                (adapterView, view, pos, id) -> Toast.makeText(view.getContext(),
                        emailList.get(pos),
                        Toast.LENGTH_SHORT).show());

        Gson gson = new Gson();
        User[] users = gson.fromJson(result, User[].class);

        for (User user : users) {
            nameList.add(user.name);
            emailList.add(user.email);
        }
        listAdapter.notifyDataSetChanged();
    }

}