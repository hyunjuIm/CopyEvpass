package com.example.evpasscopy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pedro.library.AutoPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.evpasscopy.common.ConstantValue.*;
import static com.example.evpasscopy.common.RestUrl.*;
import static com.example.evpasscopy.volley.VolleyManager.requestQueue;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(requestQueue ==null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        requestPolicy();
    }

    private void requestPolicy(){
        String requestUrl = URL_API_POLICY;

        //지정된 URL에서 JSONObject의 응답 본문을 가져오기 위한 요청, 요청 본문의 일부로 선택적 JSONObject를 전달할 수 있음.
        Request<JSONObject> request = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("app_token");
                            app_token = token.substring(8, token.length()-8);

                            Log.e(TAG, app_token);

                            Intent intent = new Intent(getBaseContext(), PermissionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "정책 데이터 가져오기 실패");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("secret", "bU0xc3hRTWJyemkvb0ZUZkhjTUYybmFLOFV3PQ==");

                return params;
            }
        };
        request.setShouldCache(false); //이전 응답 결과 사용하지 않겠다 ->cache 사용 안함
        requestQueue.add(request); //요청 큐 넣어주기
    }
}