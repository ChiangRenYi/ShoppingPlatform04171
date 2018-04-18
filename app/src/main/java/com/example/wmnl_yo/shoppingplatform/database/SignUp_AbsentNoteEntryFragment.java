package com.example.wmnl_yo.shoppingplatform.database;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.wmnl_yo.shoppingplatform.Constants;
import com.example.wmnl_yo.shoppingplatform.fragment.AbsentNoteEntryFragment;

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
 * Created by Sandy on 2017/10/19.
 */

public class SignUp_AbsentNoteEntryFragment extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        Log.d("55125", "signUp_AbsentNoteEntryFragment...");
        String url = Constants.SERVER_URL + "signUp_AbsentNoteEntryFragment.php";
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
                    .appendQueryParameter("account", Constants.ACCOUNT)
                    .appendQueryParameter("absentEntry_student", AbsentNoteEntryFragment.db_absent_entry_student)
                    .appendQueryParameter("absentEntry_building", AbsentNoteEntryFragment.db_absent_entry_building)
                    .appendQueryParameter("absentEntry_start", AbsentNoteEntryFragment.db_absent_entry_start)
                    .appendQueryParameter("absentEntry_class", AbsentNoteEntryFragment.db_absent_entry_class)
                    .appendQueryParameter("absentEntry_kind", AbsentNoteEntryFragment.db_absent_entry_kind)
                    .appendQueryParameter("absentEntry_money", AbsentNoteEntryFragment.db_absent_entry_money)
                    .appendQueryParameter("absentEntry_reason", AbsentNoteEntryFragment.db_absent_entry_reason);
            Log.d("55125",AbsentNoteEntryFragment.db_absent_entry_student.trim()
                    +Constants.ACCOUNT+AbsentNoteEntryFragment.db_absent_entry_building.trim()
                    +AbsentNoteEntryFragment.db_absent_entry_start.trim()
                    +AbsentNoteEntryFragment.db_absent_entry_class.trim()
                    +AbsentNoteEntryFragment.db_absent_entry_kind.trim()
                    +AbsentNoteEntryFragment.db_absent_entry_money.trim()
                    +AbsentNoteEntryFragment.db_absent_entry_reason.trim());
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
            AbsentNoteEntryFragment.absent_student_leave = result.trim();
            return result;

        }
    }

    protected void onPostExecute(String s) {
        if (s != null) {
            Log.d("55125", s);


        }
    }
}
