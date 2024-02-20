/*
 * (C) Copyright 2024 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<MyItem> myItems;

    public MyAdapter(Context context, List<MyItem> items) {
        this.context = context;
        this.myItems = items;
    }

    @Override
    public int getCount() {
        return myItems.size();
    }

    @Override
    public Object getItem(int position) {
        return myItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View gridViewItem = inflater.inflate(R.layout.grid_item_layout, parent, false);
        Button buttonView = gridViewItem.findViewById(R.id.button);
        buttonView.setText(myItems.get(position).getText());
        buttonView.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Hello " + position, Toast.LENGTH_SHORT).show();
        });
        return buttonView;
    }
}