package com.example.evpasscopy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.evpasscopy.common.NaverMapHelper;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.pedro.library.AutoPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.evpasscopy.common.ConstantValue.defaultMapBound;
import static com.example.evpasscopy.common.ConstantValue.home_latitude;
import static com.example.evpasscopy.common.ConstantValue.home_longitude;
import static com.example.evpasscopy.common.ConstantValue.map_default_zoom_level;
import static com.example.evpasscopy.common.RestUrl.URL_API_STATION_LIST;
import static com.example.evpasscopy.volley.VolleyManager.requestQueue;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";

    //지도
    private NaverMap mNaverMap;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //위험 권한 자동 부여 라이브러리
        AutoPermissions.Companion.loadAllPermissions(this, 101);

        initView();
        initMap();
    }

    private void initView(){
        findViewById(R.id.button1).setBackgroundResource(R.drawable.bt_nav_01_pressed);

        if(requestQueue ==null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    //맵 초기화
    private void initMap() {
        //프래그먼트와 상호작용
        FragmentManager fm = getSupportFragmentManager();
        mMapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance();
            //beginTransaction() -> Fragment에서의 작업
            fm.beginTransaction().add(R.id.map_fragment, mMapFragment).commit();
        }
        //맵 사용 준비 완료
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mNaverMap = naverMap;

        // 카메라 초기 위치 설정
        LatLng initialPosition = new LatLng(home_latitude, home_longitude);

        naverMap.getUiSettings().setZoomControlEnabled(false); //줌 컨트롤 활성화 여부
        naverMap.getUiSettings().setCompassEnabled(false);// 나침반 활성화 여부
        naverMap.getUiSettings().setScaleBarEnabled(false); // 축척바 활성화 여부
        naverMap.getUiSettings().setRotateGesturesEnabled(false); //회전 제스처 활성화 여부
        naverMap.getUiSettings().setLogoMargin(10, 0, 0, 10); //네이버 로고 마진


        naverMap.setMinZoom(5.0);
        naverMap.setMaxZoom(18.0);
        naverMap.setExtent(defaultMapBound);
        naverMap.setLiteModeEnabled(true); //라이트 모드 활성화
        naverMap.setLocationTrackingMode(LocationTrackingMode.None); //위치 추적 모드
        naverMap.setCameraPosition(new CameraPosition(initialPosition, map_default_zoom_level));
    }

}