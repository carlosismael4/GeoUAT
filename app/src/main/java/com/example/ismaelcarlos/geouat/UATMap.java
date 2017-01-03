package com.example.ismaelcarlos.geouat;

import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class UATMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uatmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in UAT tamaulipas and move the camera
        LatLng UAT = new LatLng(23.7157506,-99.1519597);
        mMap.addMarker(new MarkerOptions().position(UAT).title("4toPiso"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UAT,17));
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
        addFacultary();
        position();
    }

    public void position(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Projection proj = mMap.getProjection();
                Point coord = proj.toScreenLocation(latLng);

                Toast.makeText(
                        UATMap.this,
                        "Click\n" +
                                "Lat: " + latLng.latitude + "\n" +
                                "Lng: " + latLng.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addFacultary(){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71640728,-99.1511106))
                .title("Comercio"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7172535726,-99.1512420))
                .title("CELLAP"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7172572571,-99.1521712))
                .title("Trabajo Social"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7171293570,-99.14989806))
                .title("Ciencias"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71822404,-99.15269393))
                .title("Gimnacio Multi"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71552169,-99.15311230))
                .title("FIC"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7162351,-99.1522213))
                .title("Centro de Excelencia"));
    }
}
