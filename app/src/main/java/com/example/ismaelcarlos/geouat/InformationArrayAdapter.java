package com.example.ismaelcarlos.geouat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ismael Carlos on 3/1/2017.
 */

public class InformationArrayAdapter extends ArrayAdapter<EventMapper> {

    private Context context;
    private List<EventMapper> events;

    public InformationArrayAdapter(Context context, ArrayList<EventMapper> events){
        super(context, 0,events);
        this.context = context;
        this.events = events;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        EventMapper event = events.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_row, null);

        TextView titulo = (TextView) view.findViewById(R.id.tvTitulo);
        TextView description = (TextView) view.findViewById(R.id.tvDescripcion);

        //display trimmed excerpt for description
        //set price and rental attributes
        titulo.setText("Titulo: " + String.valueOf(event.getTitulo()));
        description.setText("Descripción: " + String.valueOf(event.getDescripcion()));

        return view;
    }
}
