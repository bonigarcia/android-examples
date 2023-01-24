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
package io.github.bonigarcia.android.asynctaskbasicdemo;

import android.os.AsyncTask;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DownloadWebPageTask extends AsyncTask<String, Void, String> {

    private TextView textView;
    private ProgressBar progressBar;

    public DownloadWebPageTask(ProgressBar progressBar, TextView textView) {
        this.textView = textView;
        this.progressBar = progressBar;
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
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(Html.fromHtml(result));
        textView.setMovementMethod(new ScrollingMovementMethod());

        progressBar.setVisibility(View.GONE);
    }

}
