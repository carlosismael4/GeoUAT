package com.example.ismaelcarlos.geouat;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by Ismael Carlos on 2/20/2017.
 */

public class RequestTask  extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            URL url = new URL("http://192.168.0.19/geoUAT/appweb/ServerRequests/EventRequest/getEventById.php");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("minor", params[0])
                    .appendQueryParameter("major", params[1]);
            String query = builder.build().getEncodedQuery();
            //Enviar la Infor.
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            urlConnection.connect();

            // Read data sent from server
            InputStream input = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // Pass data to onPostExecute method
            return(result.toString());

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
      //  tvWeatherJson.setText(s);
        Log.i("json", s);
    }
}

