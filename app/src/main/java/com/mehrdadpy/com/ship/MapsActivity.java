package com.mehrdadpy.com.ship;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mehrdadpy.com.ship.data.DatabaseHandler;
import com.mehrdadpy.com.ship.model.ItemSlideMenu;
import com.mehrdadpy.com.ship.model.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng markerPosition;
    private DatabaseHandler db;
    private Button searchB;
    private EditText searchE;
    private Button menuB;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private Vector<Double> lat = new Vector<>(100,2);
    private Vector<Double> lon = new Vector<>(100,2);
    private Vector<String> idList = new Vector<>(100,2);
    private static boolean refresh = false;

//    private List<ItemSlideMenu> listSliding;
//    private SlidingM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new DatabaseHandler(MapsActivity.this);

        searchB = (Button) findViewById(R.id.searchButton);
        searchE = (EditText) findViewById(R.id.sesrchEditText);
        menuB = (Button) findViewById(R.id.menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        navigationView = (NavigationView) findViewById(R.id.nvMenu);

        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findShip();
            }
        });

        menuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.CENTER);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                switch (id) {

                    case R.id.add:
                        register();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit:
                        finish();
                        System.exit(0);
                        break;
                }

                return true;
            }
        });
    }

    private void findShip() {

        String idSearch = searchE.getText().toString().trim();
        boolean flag = true;

        if (idSearch.equals("")) {

            Toast.makeText(getApplicationContext(), "شما باید شماره کشتی مورد نظر را وارد کنید", Toast.LENGTH_LONG).show();
        } else {

            idSearch = "Id = " + idSearch;

            for (int i = 0; i < idList.size(); i++)

                if (idSearch.equals(idList.get(i))) {

                    markerPosition = new LatLng(lat.get(i), lon.get(i));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition,10.f));
                    flag = false;

                    break;
                }

            if (flag)
                Toast.makeText(getApplicationContext(), "کشتی شما یافت نشد", Toast.LENGTH_LONG).show();
        }
    }

    private void register(){

        float zoomLevel;

        markerPosition = mMap.getCameraPosition().target;
        zoomLevel = mMap.getCameraPosition().zoom;

        mMap.addMarker(new MarkerOptions().position(markerPosition).draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition,zoomLevel));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                markerPosition = marker.getPosition();
                double curLat = markerPosition.latitude;
                double curLon = markerPosition.longitude;
                String id = "Id = " + (lat.size() + 1);
                Intent intent = new Intent(MapsActivity.this, AddShip.class);

                intent.putExtra("lat", curLat);
                intent.putExtra("lon", curLon);
                intent.putExtra("id", id);

                startActivity(intent);
                refresh = true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (refresh) {

            refresh = false;
            refreshData();
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
        mMap.getUiSettings().setCompassEnabled(true);
        markerPosition = new LatLng(32.434765, 53.292645);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition,5.f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.d("stat",marker.getTitle());
                return false;
            }
        });

        refreshData();
    }

    private void refreshData() {

        lat.clear();
        lon.clear();
        idList.clear();
        ArrayList<Ship> shipsFromDb = db.getShips();

        mMap.clear();

        for (int i = 0; i < shipsFromDb.size(); i++) {

            lat.add(shipsFromDb.get(i).getLat());
            lon.add(shipsFromDb.get(i).getLon());
            idList.add(shipsFromDb.get(i).getShipTitle());

            markerPosition = new LatLng(lat.get(i),lon.get(i));
            mMap.addMarker(new MarkerOptions().position(markerPosition).title(idList.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ship1)));
        }
    }
}
