package com.example.v001ff.footmark;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener {

    private GoogleMap mMap;
    private final int REQUEST_PERMISSION = 1000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //対応検討中(も)
        //InputSpotFragment fragment = new InputSpotFragment();
        //getFragmentManager().beginTransaction().add
        //        (android.R.id.content, fragment, "InputSpotFragment").commit();

        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= 23)
            checkPermission();
        else
            start();
    }


    public void start() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    //開いたときに実行される関数
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng yu = new LatLng(33.956416, 131.2725288);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(yu).title("Marker in YU"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yu));


        /*//LocationManagerの取得
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        //GPSから現在地の情報を取得
        Location myLocate = locationManager.getLastKnownLocation("gps");
        //MapControllerの取得
        MapController MapCtrl = mapView.getController();
        if(myLocate != null){
        //現在地情報取得成功
        //緯度の取得
        int latitude = (int) (myLocate.getLatitude() * 1e6);
        //経度の取得
        int longitude = (int) (myLocate.getLongitude() * 1e6);
        //GeoPointに緯度・経度を指定
        GeoPoint GP = new GeoPoint(latitude, longitude);
        //現在地までアニメーションで移動
        MapCtrl.animateTo(GP);
        //現在地までパッと移動
        MapCtrl.setCenter(GP);
        }else{
        //現在地情報取得失敗時の処理
        Toast.makeText(this, "現在地取得できませーん！", Toast.LENGTH_SHORT).show();
        }
        */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    //位置情報許可の確認
    public void checkPermission() {
        //すでに許可していた場合
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            start();
            //拒否していた場合
        else
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION,},
                    REQUEST_PERMISSION);

    }

    //結果の受け取り
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if ((requestCode == REQUEST_PERMISSION) &&
                (grantResults[0] == PackageManager.
                        PERMISSION_GRANTED))
            start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplication(), InputSpotActivity.class);
        startActivity(intent);
    }

    // メニュー作成
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_drawer, menu);
        return true;
    }

    // メニューアイテム選択イベント
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                // コードを追加
                break;
            case R.id.nav_gallery:
                finish();
                break;
            case R.id.nav_slideshow:
                finish();
                break;
            case R.id.nav_manage:
                finish();
                break;
            case R.id.nav_share:
                finish();
                break;
            case R.id.nav_send:
                finish();
                break;
            case R.id.nav_view:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}