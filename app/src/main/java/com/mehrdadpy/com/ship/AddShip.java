package com.mehrdadpy.com.ship;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mehrdadpy.com.ship.data.DatabaseHandler;
import com.mehrdadpy.com.ship.model.Ship;

public class AddShip extends AppCompatActivity {

    private EditText name;
    private EditText origin;
    private EditText eFinal;
    private TextView lat;
    private TextView lon;
    private Spinner type;
    private Button save;
    private Button cancel;
    private String[] itemsType = new String[]{"مسافری", "تجاری"};
    private DatabaseHandler db;
    private Ship ship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ship);

        db = new DatabaseHandler(getApplicationContext());
        ship = new Ship();
        Bundle extras = getIntent().getExtras();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,itemsType);
        final double getLat = extras.getDouble("lat");
        final double getLon = extras.getDouble("lon");
        final String getTitle = extras.getString("id");


        name = (EditText) findViewById(R.id.addEditName);
        origin = (EditText) findViewById(R.id.addEditOrigin);
        eFinal = (EditText) findViewById(R.id.addEditFinal);
        lat = (TextView) findViewById(R.id.addTextLat);
        lon = (TextView) findViewById(R.id.addTextLon);
        type = (Spinner) findViewById(R.id.addSpinnerType);
        save = (Button) findViewById(R.id.addSave);
        cancel = (Button) findViewById(R.id.addCancel);

        lon.setText(String.format("%.5f", getLon));
        lat.setText(String.format("%.5f", getLat));
        type.setAdapter(adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ship.setShipName(name.getText().toString());
                ship.setShipTitle(getTitle);
                ship.setShipOrigin(origin.getText().toString());
                ship.setShipFinal(eFinal.getText().toString());
                ship.setLat(getLat);
                ship.setLon(getLon);
                ship.setShipType(type.getSelectedItem().toString());

                db.addShip(ship);
                db.close();

                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.close();

                finish();
            }
        });
    }
}
