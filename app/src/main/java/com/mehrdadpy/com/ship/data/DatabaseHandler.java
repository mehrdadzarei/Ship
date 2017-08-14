package com.mehrdadpy.com.ship.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mehrdadpy.com.ship.model.Ship;

import java.util.ArrayList;

/**
 * Created by Bio-imaging Lab on 8/13/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Ship> shipList = new ArrayList<>();

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" +
                Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.SHIP_NAME + " TEXT, " +
                Constants.SHIP_TITLE + " TEXT, " + Constants.SHIP_LAT + " FLOAT, " +
                Constants.SHIP_LONG + " FLOAT, " + Constants.DATE_NAME + " LONG);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + Constants.TABLE_NAME);
        onCreate(db);
    }

    public void addShip (Ship ship) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.SHIP_NAME, ship.getShipName());
        values.put(Constants.SHIP_TITLE, ship.getShipTitle());
        values.put(Constants.SHIP_LAT, ship.getLat());
        values.put(Constants.SHIP_LONG, ship.getLon());
        values.put(Constants.DATE_NAME, System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Ship> getShips() {

        shipList.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID, Constants.SHIP_NAME,
                    Constants.SHIP_TITLE,Constants.SHIP_LAT, Constants.SHIP_LONG, Constants.DATE_NAME},
                    null, null, null, null, Constants.DATE_NAME + " DESC ");

        if (cursor.moveToFirst())

            do {

                Ship ship = new Ship();
                ship.setShipId(cursor.getColumnIndex(Constants.KEY_ID));
                ship.setShipName(cursor.getString(cursor.getColumnIndex(Constants.SHIP_NAME)));
                ship.setShipTitle(cursor.getString(cursor.getColumnIndex(Constants.SHIP_TITLE)));
                ship.setLat(cursor.getDouble(cursor.getColumnIndex(Constants.SHIP_LAT)));
                ship.setLon(cursor.getDouble(cursor.getColumnIndex(Constants.SHIP_LONG)));

                shipList.add(ship);
            } while (cursor.moveToNext());

        cursor.close();
        db.close();

        return shipList;
    }
}
