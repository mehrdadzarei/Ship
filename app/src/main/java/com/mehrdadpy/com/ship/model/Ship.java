package com.mehrdadpy.com.ship.model;

import java.io.Serializable;

/**
 * Created by Bio-imaging Lab on 8/14/2017.
 */

public class Ship implements Serializable {

    private static final long serialVersionUID = 10L;
    private String shipName;
    private double lat;
    private double lon;

    public Ship (String shipName, double lat, double lon) {

        this.shipName = shipName;
        this.lat = lat;
        this.lon = lon;
    }

    public Ship () {

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
