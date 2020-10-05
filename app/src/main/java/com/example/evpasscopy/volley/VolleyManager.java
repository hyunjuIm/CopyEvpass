package com.example.evpasscopy.volley;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.evpasscopy.common.NaverMapHelper;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.evpasscopy.common.RestUrl.URL_API_STATION_LIST;

public class VolleyManager {

    public static RequestQueue requestQueue;

    private static final String TAG = "VolleyManager";

}
