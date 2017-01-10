package com.example.ismaelcarlos.geouat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Ismael Carlos on 07/01/2017.
 */

public class CustomWindowInformation  implements InfoWindowAdapter {
    LayoutInflater inflater = null;
    private TextView textViewTitle;

    public CustomWindowInformation(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View v = inflater.inflate(R.layout.customwindowinformation, null);
        if (marker != null) {
            textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
            textViewTitle.setText(marker.getTitle());
        }
        return (v);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}