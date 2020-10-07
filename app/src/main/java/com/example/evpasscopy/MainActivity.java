package com.example.evpasscopy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.evpasscopy.common.NaverMapHelper;
import com.example.evpasscopy.data.Station;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.Pickable;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.pedro.library.AutoPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.evpasscopy.common.ConstantValue.app_token;
import static com.example.evpasscopy.common.ConstantValue.defaultMapBound;
import static com.example.evpasscopy.common.ConstantValue.home_latitude;
import static com.example.evpasscopy.common.ConstantValue.home_longitude;
import static com.example.evpasscopy.common.ConstantValue.map_default_zoom_level;
import static com.example.evpasscopy.common.RestUrl.*;
import static com.example.evpasscopy.volley.VolleyManager.requestQueue;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static MainActivity instance;
    private static final String TAG = "MainActivity";

    private TextView textNotice;

    private ArrayList<Station> stationList = null;

    //지도
    private NaverMap mNaverMap;
    private MapFragment mMapFragment;
    private InfoWindow mCurrentWindow; //지도 위에 올리는 정보창

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

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

        naverMap.setOnMapClickListener(mMapClickListener);
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

        requestStation();
    }

    private void requestStation(){
        String requestUrl = URL_API_STATION_LIST;

        //지정된 URL에서 JSONObject의 응답 본문을 가져오기 위한 요청, 요청 본문의 일부로 선택적 JSONObject를 전달할 수 있음.
        Request<JSONObject> request = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            textNotice = findViewById(R.id.textnotice);
                            // 최근 공지사항 가져오기
                            JSONObject notice = (JSONObject) response.getJSONArray("notice").get(0);
                            textNotice.setText(notice.optString("title"));

                            //대여소정보 가져오기
                            stationList = NaverMapHelper.getStationInfo(response.getJSONArray("station"));
                            Log.e(TAG, stationList.get(0).getAddress());

                            getMarkerItems();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "대여소 데이터 가져오기 실패");
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

    private void getMarkerItems() {
        if (stationList != null) {
            for (int i=0; i<stationList.size(); i++) {
                addMarker(stationList.get(i));
            }
        }
    }

    //공공 대여소 마커 추가
    public Marker addMarker(Station markerItem) {
        double x = markerItem.getX();
        double y = markerItem.getY();
        final LatLng position = new LatLng(x, y);

        final Marker marker = new Marker();
        marker.setPosition(position);
        marker.setWidth(150);
        marker.setHeight(150);

        if(markerItem.getType().equals("STAT_0005")){
            View rootMarker = LayoutInflater.from(this).inflate(R.layout.evzone_marker, null);
            FrameLayout iv = (FrameLayout) rootMarker.findViewById(R.id.iv_marker);
            TextView textMarker = (TextView) rootMarker.findViewById(R.id.tv_marker);

            textMarker.setText(String.valueOf(markerItem.getPakingCount()));
            iv.setBackgroundResource(R.drawable.marker_zone_outs);
            marker.setIcon(OverlayImage.fromView(rootMarker));
        } else {
            View rootMarker = LayoutInflater.from(this).inflate(R.layout.stay_marker, null);
            Button iv = (Button) rootMarker.findViewById(R.id.iv_marker);

            iv.setBackgroundResource(R.drawable.marker_stays);
            marker.setIcon(OverlayImage.fromView(rootMarker));
        }

        marker.setMap(mNaverMap);

        // 정보창 텍스트
        final String infoText = markerItem.getName() + "\r\n 대여가능 자전거 : " + " " + markerItem.getPakingCount();

        final InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.ViewAdapter() {
            @NonNull
            @Override
            public View getView(@NonNull InfoWindow infoWindow) {
                return getInfoWindow(infoText);
            }
        });

        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                if (mCurrentWindow != null) {
                    mCurrentWindow.close();
                }
                mCurrentWindow = infoWindow;
                infoWindow.open((Marker)overlay);
                mNaverMap.moveCamera(CameraUpdate.scrollTo(position).animate(CameraAnimation.Easing, 200));
                return false;
            }
        });

        markerItem.setMarker(marker);
        return null;
    }

    //마커 정보창 setText
    private View getInfoWindow(String text) {
        View calloutParent = LayoutInflater.from(this).inflate(R.layout.callout_overlay_view, null);
        TextView calloutText = calloutParent.findViewById(R.id.callout_text);
        calloutText.setText(text);

        return calloutParent;
    }

    private final NaverMap.OnMapClickListener mMapClickListener = new NaverMap.OnMapClickListener() {
        @Override
        public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
            int radius = 1;
            //특정 화면 좌표 주변 radius 픽셀 내에 나타난 모든 오버레이 및 심벌을 가져옴
            List<Pickable> pickableList = mNaverMap.pickAll(pointF, radius);
            ArrayList<Marker> markers = new ArrayList<>();

            //선택된 마커 정보
            for (Pickable pickable : pickableList) {
                if (pickable instanceof Marker) {
                    markers.add(((Marker) pickable));
                }
            }

            if (markers.size() <= 0) {
                if (mCurrentWindow != null) {
                    mCurrentWindow.close();
                }
            }
        }
    };
}