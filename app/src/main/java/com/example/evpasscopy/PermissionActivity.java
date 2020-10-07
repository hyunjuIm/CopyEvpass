package com.example.evpasscopy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class PermissionActivity extends AppCompatActivity implements AutoPermissionsListener {

    private static PermissionActivity instance;

    int permissonCheck1;
    int permissonCheck2;
    int permissonCheck3;
    int permissonCheck4;
    int permissonCheck5;
    int permissonCheck6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        instance = this;

        TextView title = findViewById(R.id.textTitle);
        title.setText("앱 접근 권한 안내");

        permissonCheck1= ContextCompat.checkSelfPermission(instance, Manifest.permission.ACCESS_COARSE_LOCATION);
        permissonCheck2= ContextCompat.checkSelfPermission(instance, Manifest.permission.ACCESS_FINE_LOCATION);
        permissonCheck3= ContextCompat.checkSelfPermission(instance, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissonCheck4= ContextCompat.checkSelfPermission(instance, Manifest.permission.READ_EXTERNAL_STORAGE);
        permissonCheck5= ContextCompat.checkSelfPermission(instance, Manifest.permission.CAMERA);
        permissonCheck6= ContextCompat.checkSelfPermission(instance, Manifest.permission.CALL_PHONE);

        if(permissonCheck1 == PackageManager.PERMISSION_GRANTED && permissonCheck2 == PackageManager.PERMISSION_GRANTED
                && permissonCheck3 == PackageManager.PERMISSION_GRANTED && permissonCheck4 == PackageManager.PERMISSION_GRANTED
                && permissonCheck5 == PackageManager.PERMISSION_GRANTED && permissonCheck6 == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

       findViewById(R.id.btnPermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(permissonCheck1 != PackageManager.PERMISSION_GRANTED || permissonCheck2 != PackageManager.PERMISSION_GRANTED
                    || permissonCheck3 != PackageManager.PERMISSION_GRANTED || permissonCheck4 != PackageManager.PERMISSION_GRANTED
                    || permissonCheck5 != PackageManager.PERMISSION_GRANTED || permissonCheck6 != PackageManager.PERMISSION_GRANTED){
                        AutoPermissions.Companion.loadAllPermissions(instance, 101);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);

        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }
}