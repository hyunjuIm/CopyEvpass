package com.example.evpasscopy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.evpasscopy.common.AES128;
import com.example.evpasscopy.common.NaverMapHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import static com.example.evpasscopy.common.ConstantValue.app_token;
import static com.example.evpasscopy.common.RestUrl.*;
import static com.example.evpasscopy.volley.VolleyManager.requestQueue;

public class LoginActivity extends AppCompatActivity {

    private static LoginActivity instance;
    private static final String TAG = "LoginActivity";

    private EditText inputPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPhoneNumber = findViewById(R.id.etPhoneNumber);

        initView();
    }

    private void initView(){
        findViewById(R.id.btnSendAuthNum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    requestSMSAuth();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestSMSAuth() throws Exception {
        String requestUrl = URL_API_AUTH_REQUEST;

        Map<String, String> params = new HashMap<String, String>();
        AES128 AES128 = new AES128();
        String phone = AES128.encAES(inputPhoneNumber.getText().toString().trim());
        params.put("phone", phone);
        params.put("lang_cd", "LANG_0001");

        Request<JSONObject> request = new JsonObjectRequest(Request.Method.POST, requestUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject result = response.getJSONObject("results");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "SMS 인증 요청 실패");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("app-token", app_token);

                return params;
            }
        };
        request.setShouldCache(false); //이전 응답 결과 사용하지 않겠다 ->cache 사용 안함
        requestQueue.add(request); //요청 큐 넣어주기
    }
}