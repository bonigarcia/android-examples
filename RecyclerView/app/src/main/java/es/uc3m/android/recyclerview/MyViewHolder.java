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
package es.uc3m.android.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public final MyRecyclerViewAdapter parent;
    public static List<String> data;
    public final TextView textView;

    public MyViewHolder(MyRecyclerViewAdapter parent, List<String> data, View view) {
        super(view);
        this.textView = view.findViewById(R.id.textview);
        this.parent = parent;
        this.data = data;

        view.setOnClickListener(view1 -> {
            int position = getLayoutPosition();
            data.remove(position);
            parent.notifyDataSetChanged();
        });

    }

}