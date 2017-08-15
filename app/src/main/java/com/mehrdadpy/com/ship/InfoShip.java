package com.mehrdadpy.com.ship;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mehrdadpy.com.ship.data.DatabaseHandler;
import com.mehrdadpy.com.ship.model.Ship;

import java.util.ArrayList;

public class InfoShip extends AppCompatActivity {

    private TextView name;
    private TextView origin;
    private TextView distin;
    private TextView lata;
    private TextView longi;
    private TextView type;
    private Button trace;
    private Button share;
    private Button del;
    private DatabaseHandler db;
    private int id;
    private ArrayList<Ship> shipsFromDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ship);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHandler(getApplicationContext());
        shipsFromDb = db.getShips();
        Bundle extras = getIntent().getExtras();
        final String getId = extras.getString("id");

        name = (TextView) findViewById(R.id.addEditName);
        origin = (TextView) findViewById(R.id.addEditOrigin);
        distin = (TextView) findViewById(R.id.addEditFinal);
        lata = (TextView) findViewById(R.id.addTextLat);
        longi = (TextView) findViewById(R.id.addTextLon);
        type = (TextView) findViewById(R.id.addTextType);
        trace = (Button) findViewById(R.id.traceB);
        share = (Button) findViewById(R.id.shareB);
        del = (Button) findViewById(R.id.delB);

        for (int i = 0; i < shipsFromDb.size(); i++)

            if (shipsFromDb.get(i).getShipTitle().toString().equals(getId)) {

                id = i;
                break;
            }

        name.setText(shipsFromDb.get(id).getShipName());
        origin.setText(shipsFromDb.get(id).getShipOrigin());
        distin.setText(shipsFromDb.get(id).getShipFinal());
        longi.setText(String.format("%.5f", shipsFromDb.get(id).getLon()));
        lata.setText(String.format("%.5f", shipsFromDb.get(id).getLat()));
        type.setText(shipsFromDb.get(id).getShipType());

        trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareData(shipsFromDb, id);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = new DatabaseHandler(getApplicationContext());
                db.deleteShip(getId);

                Toast.makeText(getApplicationContext(), "کشتی مورد نظر حذف شد", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    private void shareData(ArrayList<Ship> shipsFromDb, int id) {

        StringBuilder dataString = new StringBuilder();

        String name = shipsFromDb.get(id).getShipName();
        String origin = shipsFromDb.get(id).getShipOrigin();
        String distin = shipsFromDb.get(id).getShipFinal();
        String lon = String.format("%.5f", shipsFromDb.get(id).getLon());
        String lat = String.format("%.5f", shipsFromDb.get(id).getLat());
        String type = shipsFromDb.get(id).getShipType();

        dataString.append("نام کشتی: " + name + "\n");
        dataString.append("مبدا: " + origin + "\n");
        dataString.append("مقصد: " + distin + "\n");
        dataString.append("طول: " + lon + "\n");
        dataString.append("عرض: " + lat + "\n");
        dataString.append("نوع: " + type + "\n");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "مشخصات کشتی");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"recipient@example.com"});
        intent.putExtra(Intent.EXTRA_TEXT, dataString.toString());

        try {

            startActivity(Intent.createChooser(intent, "Send mail..."));

        }catch (ActivityNotFoundException e) {

            Toast.makeText(getApplicationContext(), "Please install email client before sending", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
