package com.mehrdadpy.com.ship;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private Vector<Double> lat = new Vector<>(100,2);
    private Vector<Double> lon = new Vector<>(100,2);
    private Ship ship;

//    private List<ItemSlideMenu> listSliding;
//    private SlidingM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new DatabaseHandler(MapsActivity.this);

        searchB = (Button) findViewById(R.id.searchButton);
        searchE = (EditText) findViewById(R.id.sesrchEditText);
        menuB = (Button) findViewById(R.id.menu);

        menuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                register();
            }
        });
    }

    private void register(){

        float zoomLevel;
        ship = new Ship();

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

                ship.setLat(curLat);
                ship.setLon(curLon);
                db.addShip(ship);
                lat.add(curLat);
                lon.add(curLon);

                mMap.clear();

                for (int i=0; i<lat.size(); i++) {

                    markerPosition = new LatLng(lat.get(i),lon.get(i));
                    mMap.addMarker(new MarkerOptions().position(markerPosition)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ship1)));
                }
            }
        });
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

        refreshData();
    }

    private void refreshData() {

        lat.clear();
        lon.clear();
        ArrayList<Ship> shipsFromDb = db.getShips();

        for (int i = 0; i < shipsFromDb.size(); i++) {

            lat.add(shipsFromDb.get(i).getLat());
            lon.add(shipsFromDb.get(i).getLon());
        }

        for (int i=0; i<lat.size(); i++) {

            markerPosition = new LatLng(lat.get(i),lon.get(i));
            mMap.addMarker(new MarkerOptions().position(markerPosition)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ship1)));
        }
    }
}
