package com.example.evpasscopy.data;

import com.naver.maps.map.overlay.Marker;

public class Station {
    private String id               = null;
    private String area             = null;
    private String type             = null;
    private String name             = null;
    private String nameEN           = null;
    private String nameCN           = null;
    private String nameJP           = null;
    private Double x                = null;
    private Double y                = null;
    private String address          = null;
    private int geofence            = 0;
    private String chargeReturn     = null;
    private int pakingCount         = 0;
    private Marker marker           = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    public String getNameJP() {
        return nameJP;
    }

    public void setNameJP(String nameJP) {
        this.nameJP = nameJP;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGeofence() {
        return geofence;
    }

    public void setGeofence(int geofence) {
        this.geofence = geofence;
    }

    public String getChargeReturn() {
        return chargeReturn;
    }

    public void setChargeReturn(String chargeReturn) {
        this.chargeReturn = chargeReturn;
    }

    public int getPakingCount() {
        return pakingCount;
    }

    public void setPakingCount(int pakingCount) {
        this.pakingCount = pakingCount;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
