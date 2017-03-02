package com.example.ismaelcarlos.geouat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.ismaelcarlos.geouat.R.id.tvTitle;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView listView = (ListView) findViewById(R.id.lv);
        TextView textView = (TextView) findViewById(tvTitle);
        Bundle bundle = getIntent().getExtras();
        String option = (String) bundle.getSerializable("option");
        ArrayList<EventMapper> arraylist = null;
        if(option.equals("2")) {
            textView.setVisibility(View.GONE);
            arraylist = (ArrayList<EventMapper>) bundle.getSerializable("eventMappersList");
            ArrayAdapter<EventMapper> adapter = new InformationArrayAdapter(this, arraylist);
            listView.setAdapter(adapter);
        }else{
            listView.setVisibility(View.INVISIBLE);
            listView.setAdapter((ListAdapter) arraylist);
            textView.setVisibility(View.VISIBLE);
            textView.setText("No hay información que mostrar");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
