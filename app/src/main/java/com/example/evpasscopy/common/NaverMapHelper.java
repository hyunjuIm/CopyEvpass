package com.example.evpasscopy.common;

import com.example.evpasscopy.data.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NaverMapHelper {
    public static ArrayList<Station> getStationInfo(JSONArray results) {
        try {
            ArrayList<Station> stationList = new ArrayList<Station>();
            Station station;

            for (int i=0; i<results.length(); i++) {
                JSONObject stationData = results.getJSONObject(i);
                station = new Station();

                station.setId(stationData.optString("id"));
                station.setArea(stationData.optString("area"));
                station.setType(stationData.optString("type_cd"));
                station.setName(stationData.optString("name"));
                station.setNameEN(stationData.optString("name_en"));
                station.setNameCN(stationData.optString("name_cn"));
                station.setNameJP(stationData.getString("name_jp"));
                station.setX(stationData.optDouble("x_pos"));
                station.setY(stationData.optDouble("y_pos"));
                station.setAddress(stationData.getString("address"));
                station.setGeofence(stationData.getInt("geofence_distance"));
                station.setChargeReturn(stationData.getString("charge_return_yn"));
                station.setPakingCount(stationData.getInt("parking_count"));
                stationList.add(station);
            }

            return stationList;
        } catch (JSONException e) {
            return null;
        }
    }
}
