package com.mehrdadpy.com.ship.model;

import java.io.Serializable;

/**
 * Created by Bio-imaging Lab on 8/14/2017.
 */

public class Ship implements Serializable {

    private static final long serialVersionUID = 10L;
    private int shipId;
    private String shipName;
    private String shipTitle;
    private String shipOrigin;
    private String shipFinal;
    private double lat;
    private double lon;
    private String shipType;

    public Ship (String shipName, String shipTitle, double lat, double lon) {

        this.shipName = shipName;
        this.shipName = shipTitle;
        this.lat = lat;
        this.lon = lon;
    }

    public Ship () {

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipTitle() {
        return shipTitle;
    }

    public void setShipTitle(String shipTitle) {
        this.shipTitle = shipTitle;
    }

    public String getShipOrigin() {
        return shipOrigin;
    }

    public void setShipOrigin(String shipOrigin) {
        this.shipOrigin = shipOrigin;
    }

    public String getShipFinal() {
        return shipFinal;
    }

    public void setShipFinal(String shipFinal) {
        this.shipFinal = shipFinal;
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

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }
}
