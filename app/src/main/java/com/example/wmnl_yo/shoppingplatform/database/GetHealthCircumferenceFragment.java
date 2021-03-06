package com.example.wmnl_yo.shoppingplatform.database;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.wmnl_yo.shoppingplatform.Constants;
import com.example.wmnl_yo.shoppingplatform.R;
import com.example.wmnl_yo.shoppingplatform.fragment.HealthCircumferenceFragment;
import com.example.wmnl_yo.shoppingplatform.fragment.HealthManageFragment;
import com.example.wmnl_yo.shoppingplatform.fragment.HealthWeightFragment;
import com.example.wmnl_yo.shoppingplatform.object.HealthCircumferenceObject;
import com.example.wmnl_yo.shoppingplatform.object.HealthWeightObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sandy on 2017/8/30.
 */

public class GetHealthCircumferenceFragment extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        Log.d("55125", "getHealthCircumferenceFragment...");
        String url = Constants.SERVER_URL + "getHealthCircumferenceFragment.php";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String result = null;
        try {
            URL tkuUrl = new URL(url);
            connection = (HttpURLConnection) tkuUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            //傳值
            Uri.Builder builder = new Uri.Builder()
                    //傳值到php
                    .appendQueryParameter("account",Constants.ACCOUNT.trim())
                    .appendQueryParameter("SD_Id", HealthManageFragment.SDId.trim());


            String query = builder.build().getEncodedQuery();

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {/*success*/
                InputStream inputStream = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Log.d("55125", "get inputStream error");
                } else {
                    reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null)
                        buffer.append(inputLine + "\n");
                    if (buffer.length() == 0) {
                        // Stream was empty. No point in parsing.
                        Log.d("55125", "nothing");
                        HealthCircumferenceObject.ITEMS.clear();
                    } else {
                        result = buffer.toString();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("55125", e.toString());
            return null;
        } finally {
                /*close urlConnection*/
            if (connection != null) {
                connection.disconnect();
            }
                /*close inputStreamReader*/
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("55125", "Error", e);
                }
            }
            Log.d("55125",result);
            int j = 0;

            String[] healthCircumference = result.split("<QQ>");
            String[] healthCircumference_value = new String[healthCircumference.length - 1];
            String[] healthCircumference_date = new String[healthCircumference.length - 1];
            String[] healthCircumference_time = new String[healthCircumference.length - 1];

            HealthCircumferenceObject.ITEMS.clear();
            HealthCircumferenceObject dim = new HealthCircumferenceObject();

            for (int i = 0; i < healthCircumference.length - 1; i++) {
                String[] healthCircumferenceInf = new String[5];
                healthCircumferenceInf = healthCircumference[i].split("@#");
                healthCircumference_value[j] = healthCircumferenceInf[1];
                healthCircumference_date[j] = healthCircumferenceInf[2];
                healthCircumference_time[j] = healthCircumferenceInf[3];

                dim.addItem(new HealthCircumferenceObject.HealthCircumferenceObjectItem(R.drawable.ic_circumference,
                        healthCircumferenceInf[1],"Cm",healthCircumferenceInf[2]+'('+healthCircumferenceInf[3]+")"));

                j++;
                Log.d("55125", healthCircumferenceInf[1]+"Cm"+","+healthCircumferenceInf[2]+"("+healthCircumferenceInf[3]+")");
            }
            return result;

        }
    }

    protected void onPostExecute(String s) {

        Log.d("55125","notifyDataSetChanged");
        HealthCircumferenceFragment.cAdapter.notifyDataSetChanged();
    }
}
