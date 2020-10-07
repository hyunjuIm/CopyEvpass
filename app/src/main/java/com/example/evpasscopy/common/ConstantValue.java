package com.example.evpasscopy.common;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;

public class ConstantValue {
    public static String app_token                      = null;

    public static final float home_latitude             = 33.397474f;
    public static final float home_longitude            = 126.550150f;
    public static final float map_default_zoom_level    = 8.2f;

    public static final LatLng notrhEast                = new LatLng(44.35, 132);
    public static final LatLng southWest                = new LatLng(31.43, 122.37);
    public static final LatLngBounds defaultMapBound    = new LatLngBounds(southWest, notrhEast);
}
