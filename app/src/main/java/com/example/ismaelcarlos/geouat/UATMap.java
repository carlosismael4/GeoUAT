package com.example.ismaelcarlos.geouat;

import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class UATMap extends AppCompatActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener{

    private GoogleMap mMap;
    private boolean flagFacultary = false;
    private boolean flagGym = false;
    private List<Marker> markerFacultary;
    private List<Marker> markerOffices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uatmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        markerFacultary = new ArrayList<>();
        markerOffices = new ArrayList<>();
        //add window information
        mMap.setInfoWindowAdapter(new CustomWindowInformation(getLayoutInflater()));
        // Add a marker in UAT tamaulipas and move the camera
        LatLng UAT = new LatLng(23.7157506,-99.1519597);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UAT,17));
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
       // addFacultary();
        position();

        mMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener(){
                    public void onInfoWindowClick(Marker marker){
                        Toast.makeText(UATMap.this, "Testt", Toast.LENGTH_SHORT).show();
                    }
                }
        );
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

    public void addMarkersFacultary(){

        Marker comercio =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71640728,-99.1511106))
                .title("Comercio")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.comercio)));
        Marker cellap =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7172535726,-99.1512420))
                .title("CELLAP"));
        Marker ciencias =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7171293570,-99.14989806))
                .title("Ciencias"));
        Marker fic =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71552169,-99.15311230))
                .title("Facultad de Ingenieria y Ciencias")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.fic)));
        Marker trabajoSocial =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7172572571,-99.1521712))
                .title("Trabajo Social")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.trabajosocial)));

        markerFacultary.add(comercio);
        markerFacultary.add(cellap);
        markerFacultary.add(ciencias);
        markerFacultary.add(fic);
        markerFacultary.add(trabajoSocial);
        flagFacultary = true;
    }
    public void removeMarkers(List<Marker> markers){
        for(int i = 0; i < markers.size(); i++){
            markers.get(i).remove();
        }
    }
    public void addMarkersOffices(){

        Marker centroExcelencia =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7162351,-99.1522213))
                .title("Centro de Excelencia"));
               // .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.customwindow));
        Marker multi =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71822404,-99.15269393))
                .title("Gimnacio Multi"));
        Marker fourFloor =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7157506,-99.1519597))
                .title("Cuarto Piso"));
        markerOffices.add(centroExcelencia);
        markerOffices.add(multi);
        markerOffices.add(fourFloor);
        flagGym = true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.facultary) {
            if(flagFacultary){
                flagFacultary = false;
                removeMarkers(markerFacultary);
            }else
                addMarkersFacultary();
        } else if (id == R.id.gym) {
            if(flagGym){
                flagGym = false;
                removeMarkers(markerOffices);
            }else
                addMarkersOffices();
        } else if (id == R.id.offices) {
          //  Toast.makeText(this,"slides",Toast.LENGTH_LONG).show();
        } else if (id == R.id.localdining) {
           // Toast.makeText(this,"administrador",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {
           // Toast.makeText(this,"Compartir",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
          //  Toast.makeText(this,"Enviar",Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
