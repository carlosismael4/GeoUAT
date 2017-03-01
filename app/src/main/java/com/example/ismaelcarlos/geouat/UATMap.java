package com.example.ismaelcarlos.geouat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UATMap extends AppCompatActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, BeaconConsumer{

    private GoogleMap mMap;
    private boolean flagFacultary = false;
    private boolean flagGym = false;
    private List<Marker> markerFacultary;
    private List<Marker> markerOffices;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private BackgroundPowerSaver backgroundPowerSaver;
    private RequestTask requestTask;
    private String minor = "";
    private String major = "";
    private String auxMinor = "";
    private String auxMajor = "";
    private List<EventMapper> eventMappersList;
    private String snippet = "No hay Información para mostrar.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uatmap);
        backgroundPowerSaver = new BackgroundPowerSaver(this);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}
                                ,PERMISSION_REQUEST_COARSE_LOCATION);}

                });
                builder.show();}
        }

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        requestTask = new RequestTask();
        try {
            String eventos = requestTask.execute("2","1").get();
            Log.e("Cuis",eventos);
            Gson gson = new Gson();
            Type eventListType = new TypeToken<ArrayList<EventMapper>>(){}.getType();
            eventMappersList =  gson.fromJson(eventos,eventListType);
            Log.e("Mapeados",eventMappersList.get(0).toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ME ENCANTAS", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
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
        snippet = eventMappersList.get(0).getSnippet();
        mMap.setInfoWindowAdapter(new CustomWindowInformation(getLayoutInflater(),snippet));
        // Add a marker in UAT tamaulipas and move the camera
        LatLng UAT = new LatLng(23.7157506,-99.1519597);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UAT,17));
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
       // addFacultary();
        position();
        createMarkersFacultary();
        createMarkersOffices();
        mMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener(){
                    public void onInfoWindowClick(Marker marker){
                        if(marker.getSnippet().equals(snippet)){
                            Toast.makeText(UATMap.this,"Son iguales " + marker.getSnippet() + "-"+snippet,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(UATMap.this,"No iguales " + marker.getSnippet() + "-"+snippet,Toast.LENGTH_LONG).show();
                        }
                        Intent i = new Intent(UATMap.this,InformationActivity.class);
                        startActivity(i);
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

    public void createMarkersFacultary(){
        Marker comercio =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71640728,-99.1511106))
                .title("Comercio")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.comercio))
                .snippet("Fac0"));
        Marker cellap =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7172535726,-99.1512420))
                .title("CELLAP")
                .snippet("Fac1"));
        Marker ciencias =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7171293570,-99.14989806))
                .title("Ciencias")
                .snippet("Fac2"));
        Marker fic =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71552169,-99.15311230))
                .title("Facultad de Ingenieria y Ciencias")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.fic))
                .snippet("Fac3"));
        Marker trabajoSocial =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7172572571,-99.1521712))
                .title("Trabajo Social")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.trabajosocial))
                .snippet("Fac4"));

        markerFacultary.add(comercio);
        markerFacultary.add(cellap);
        markerFacultary.add(ciencias);
        markerFacultary.add(fic);
        markerFacultary.add(trabajoSocial);
        hideMarkers(markerFacultary);
    }
    public void hideMarkers(List<Marker> markers){
        for(int i = 0; i < markers.size(); i++){
            markers.get(i).setVisible(false);
        }
    }

    public void showMarkers(List<Marker> markers){
        for(int i = 0; i < markers.size(); i++){
            markers.get(i).setVisible(true);
        }
    }
    public void createMarkersOffices(){
        Marker centroExcelencia =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7162351,-99.1522213))
                .title("Centro de Excelencia")
                .snippet("Office1"));
        Marker multi =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.71822404,-99.15269393))
                .title("Gimnacio Multi")
                .snippet("Office2"));
        Marker fourFloor =
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(23.7157506,-99.1519597))
                .title("Cuarto Piso")
                .snippet("Office3"));
        markerOffices.add(centroExcelencia);
        markerOffices.add(multi);
        markerOffices.add(fourFloor);
        hideMarkers(markerOffices);
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
                hideMarkers(markerFacultary);
            }else
            {showMarkers(markerFacultary);
                flagFacultary = true;}
        } else if (id == R.id.gym) {
            if(flagGym){
                flagGym = false;
                hideMarkers(markerOffices);
            }else
            { showMarkers(markerOffices);
                flagGym = true;}
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

    ///////////BEACON
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.e(TAG, "Acabo de ver un beacon por primera vez!");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(UATMap.this).create();
                        alertDialog.setTitle("Beacon");
                        alertDialog.setMessage("Se ha detectado un beacon.");
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void didExitRegion(Region region) {
                Log.e(TAG,"Ya no veo un faro");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Your dialog code.
                        minor = "0";
                        major = "0";
                    }
                });
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.e(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {    }

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    String id1 = beacons.iterator().next().getId1().toString();
                    String id2 = beacons.iterator().next().getId2().toString();
                    String id3 = beacons.iterator().next().getId3().toString();
                    Log.e("didRangeBeaconsInRegion", "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    if(!minor.equals(id2) && !major.equals(id3)){
                        minor = id2;
                        major = id3;
                        requestTask = new RequestTask();
                        requestTask.execute();
                        Log.e("Nuevo ", "Primera Vez");
                        auxMinor = minor;
                        auxMajor = major;
                    }else if(auxMinor.equals(id2) && auxMajor.equals(id3)){
                        Log.e("Es el mismo beacon", "BOM");
                    }
                    Log.e("Id1", id1);
                    minor = id2;
                    Log.e("Minor", minor);
                    major = id3;
                    Log.e("Major", major);
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

}
