package com.example.v001ff.footmark;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppLaunchChecker;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.v001ff.footmark.R.mipmap.sample;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener, LocationListener {

    private GoogleMap mMap;
    private final int REQUEST_PERMISSION = 1000;
    private Realm mRealm;
    private LocationManager manager = null;

    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    protected Location mLastLocation;

    long counter = 0;
    public double x;
    public double y;
    public LatLng z;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        setTheme(R.style.AppTheme);//splash表示する

        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);        //位置情報を利用するための準備
        LocationProvider provider = manager.getProvider(manager.GPS_PROVIDER);              //providerはGPSを用いて位置情報を取得できるか確認するのに使う

        //対応検討中(も)
        //InputSpotFragment fragment = new InputSpotFragment();
        //getFragmentManager().beginTransaction().add
        //        (android.R.id.content, fragment, "InputSpotFragment").commit();

        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= 23)                                //パーミッションを全部許可してないと設定画面に飛ばされる
            checkPermission();
        else
            start();

        mRealm = Realm.getDefaultInstance();                  //データベース使用する準備
    }

    @Override
    protected  void onStart(){
        super.onStart();
        final boolean gpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnabled){
            enableLocationSettings();
        }
    }

    @Override
    protected void onResume(){                  //アクティビティが最前面に復帰したときの処理
        if(manager!=null){
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);        //位置情報のパーミッションがオンなら位置情報を取得する
        }
        super.onResume();
    }

    @Override
    protected void onPause(){               //ほかのアクティビティが最前面になると実行
        super.onPause();
        if(manager!=null){
            manager.removeUpdates(this);                //位置情報取得を中断
        }
    }

    public void start() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }


    @Override
    //開いたときに実行される関数
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng yu = new LatLng(33.9567058, 131.2727738);
        LatLng zu = new LatLng(33.9304745, 131.2556893);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
/*        mMap.addMarker(new MarkerOptions().position(yu).title("山口大学工学部")
        /*mMap.addMarker(new MarkerOptions().position(yu).title("山口大学工学部")
                .icon(BitmapDescriptorFactory.fromResource(sample)));
        mMap.addMarker(new MarkerOptions().position(zu).title("フジグラン宇部")
                .icon(BitmapDescriptorFactory.fromResource(sample)));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yu));                     //緯度経度の情報がアプリ起動時に中心に表示される

        //ここから先はデータベースの処理です
        //画像をデータベースに入れるとこでエラーが出るんで,そこを解決できればデモデータもデータベースに格納できます


        if (AppLaunchChecker.hasStartedFromLauncher(this)) {                              //2回目以降の起動はデモの格納はしない
            Log.d("AppLaunchChecker", "2回目以降");
        } else {
            Log.d("AppLaunchChecker", "はじめてアプリを起動した");                 //初回の起動はデモデータをデータベースに入れる
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");              //デモ用の日付をここで設定してます.
            Date date = new Date();
            final String mDate = sdf.format(date);
            Resources p_r1 = getResources();                                               //デモ用の画像を設定
            Bitmap p_bmp1 = BitmapFactory.decodeResource(p_r1, R.drawable.demo1);
            final byte[] p_bytes1 = MyUtils.getByteFromImage(p_bmp1);
            Resources p_r2 = getResources();
            Bitmap p_bmp2 = BitmapFactory.decodeResource(p_r2, R.drawable.demo2);
            final byte[] p_bytes2 = MyUtils.getByteFromImage(p_bmp2);
            Resources p_r3 = getResources();
            Bitmap p_bmp3 = BitmapFactory.decodeResource(p_r3, R.drawable.demo3);
            final byte[] p_bytes3 = MyUtils.getByteFromImage(p_bmp3);
            Resources p_r4 = getResources();
            Bitmap p_bmp4 = BitmapFactory.decodeResource(p_r4, R.drawable.demo4);
            final byte[] p_bytes4 = MyUtils.getByteFromImage(p_bmp4);
            Resources p_r5 = getResources();
            Bitmap p_bmp5 = BitmapFactory.decodeResource(p_r5, R.drawable.demo5);
            final byte[] p_bytes5 = MyUtils.getByteFromImage(p_bmp5);
            Resources p_r6 = getResources();
            Bitmap p_bmp6 = BitmapFactory.decodeResource(p_r6, R.drawable.demo6);
            final byte[] p_bytes6 = MyUtils.getByteFromImage(p_bmp6);
            Resources p_r7 = getResources();
            Bitmap p_bmp7 = BitmapFactory.decodeResource(p_r7, R.drawable.demo7);
            final byte[] p_bytes7 = MyUtils.getByteFromImage(p_bmp7);
            Resources p_r8 = getResources();
            Bitmap p_bmp8 = BitmapFactory.decodeResource(p_r8, R.drawable.demo8);
            final byte[] p_bytes8 = MyUtils.getByteFromImage(p_bmp8);


            Resources a_r1 = getResources();                                               //デモ用のユーザ情報を設定
            Bitmap a_bmp1 = BitmapFactory.decodeResource(a_r1, R.drawable.man);
            final byte[] a_bytes1 = MyUtils.getByteFromImage(a_bmp1);
            Resources a_r2 = getResources();
            Bitmap a_bmp2 = BitmapFactory.decodeResource(a_r2, R.drawable.washokuman);
            final byte[] a_bytes2 = MyUtils.getByteFromImage(a_bmp2);
            Resources a_r3 = getResources();
            Bitmap a_bmp3 = BitmapFactory.decodeResource(a_r3, R.drawable.sozaicmanjp);
            final byte[] a_bytes3 = MyUtils.getByteFromImage(a_bmp3);
            Resources a_r4 = getResources();
            Bitmap a_bmp4 = BitmapFactory.decodeResource(a_r4, R.drawable.doctor);
            final byte[] a_bytes4 = MyUtils.getByteFromImage(a_bmp4);
            Resources a_r5 = getResources();
            Bitmap a_bmp5 = BitmapFactory.decodeResource(a_r5, R.drawable.syatyo);
            final byte[] a_bytes5 = MyUtils.getByteFromImage(a_bmp5);

            mRealm.executeTransaction(new Realm.Transaction() {                      //デモ用のデータをここでデータベースに格納しています.
                @Override
                public void execute(Realm realm) {
                    FootmarkDataTable footmarkDataTable = realm.createObject(FootmarkDataTable.class, 0);
                    footmarkDataTable.setPlaceName("山口大学工学部");
                    footmarkDataTable.setTitle("山口大学工学部です");
                    footmarkDataTable.setReviewBody("山口大学工学部です");
                    footmarkDataTable.setAccountName("スクラムダイキ");
                    footmarkDataTable.setAccountImage(a_bytes1);
                    footmarkDataTable.setAccountId(0);
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(p_bytes1);
                    footmarkDataTable.setPlaceId(0);
                    footmarkDataTable.setPlaceNum(0);
                    footmarkDataTable.setLatitude("33.9567058");
                    footmarkDataTable.setLongitude("131.2727738");

                    footmarkDataTable = realm.createObject(FootmarkDataTable.class, 1);
                    footmarkDataTable.setPlaceName("フジグラン宇部");
                    footmarkDataTable.setTitle("フジグラン宇部です");
                    footmarkDataTable.setReviewBody("フジグラン宇部です");
                    footmarkDataTable.setAccountName("マスターワカモト");
                    footmarkDataTable.setAccountImage(a_bytes2);
                    footmarkDataTable.setAccountId(1);
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(p_bytes2);
                    footmarkDataTable.setPlaceId(1);
                    footmarkDataTable.setPlaceNum(0);
                    footmarkDataTable.setLatitude("33.9304745");
                    footmarkDataTable.setLongitude("131.2556893");

                    footmarkDataTable = realm.createObject(FootmarkDataTable.class, 2);
                    footmarkDataTable.setPlaceName("常盤公園");
                    footmarkDataTable.setTitle("常盤公園です");
                    footmarkDataTable.setReviewBody("常盤公園です");
                    footmarkDataTable.setAccountName("リーダーミヤケ");
                    footmarkDataTable.setAccountImage(a_bytes3);
                    footmarkDataTable.setAccountId(2);
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(p_bytes3);
                    footmarkDataTable.setPlaceId(2);
                    footmarkDataTable.setPlaceNum(0);
                    footmarkDataTable.setLatitude("33.9457114");
                    footmarkDataTable.setLongitude("131.2830209");

                    footmarkDataTable = realm.createObject(FootmarkDataTable.class, 3);
                    footmarkDataTable.setPlaceName("海岸");
                    footmarkDataTable.setTitle("海岸です");
                    footmarkDataTable.setReviewBody("海岸です");
                    footmarkDataTable.setAccountName("フジモト");
                    footmarkDataTable.setAccountImage(a_bytes4);
                    footmarkDataTable.setAccountId(3);
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(p_bytes4);
                    footmarkDataTable.setPlaceId(3);
                    footmarkDataTable.setPlaceNum(0);
                    footmarkDataTable.setLatitude("33.9273581");
                    footmarkDataTable.setLongitude("131.2777983");

                    footmarkDataTable = realm.createObject(FootmarkDataTable.class, 4);
                    footmarkDataTable.setPlaceName("宇部興産前");
                    footmarkDataTable.setTitle("宇部興産前です");
                    footmarkDataTable.setReviewBody("宇部興産前です");
                    footmarkDataTable.setAccountName("アカギ");
                    footmarkDataTable.setAccountImage(a_bytes5);
                    footmarkDataTable.setAccountId(4);
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(p_bytes5);
                    footmarkDataTable.setPlaceId(4);
                    footmarkDataTable.setPlaceNum(0);
                    footmarkDataTable.setLatitude("33.9545363");
                    footmarkDataTable.setLongitude("131.2116691");

                    footmarkDataTable = realm.createObject(FootmarkDataTable.class, 5);
                    footmarkDataTable.setPlaceName("おいしいお好み焼き店");
                    footmarkDataTable.setTitle("おいしいお好み焼き店です");
                    footmarkDataTable.setReviewBody("おいしいお好み焼き店です");
                    footmarkDataTable.setAccountName("マスターワカモト");
                    footmarkDataTable.setAccountImage(a_bytes2);
                    footmarkDataTable.setAccountId(5);
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(p_bytes6);
                    footmarkDataTable.setPlaceId(5);
                    footmarkDataTable.setPlaceNum(0);
                    footmarkDataTable.setLatitude("33.9571085");
                    footmarkDataTable.setLongitude("131.2764968");

                    footmarkDataTable = realm.createObject(FootmarkDataTable.class, 6);
                    footmarkDataTable.setPlaceName("大草原");
                    footmarkDataTable.setTitle("大草原です");
                    footmarkDataTable.setReviewBody("大草原です");
                    footmarkDataTable.setAccountName("アカギ");
                    footmarkDataTable.setAccountImage(a_bytes5);
                    footmarkDataTable.setAccountId(6);
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(p_bytes7);
                    footmarkDataTable.setPlaceId(6);
                    footmarkDataTable.setPlaceNum(0);
                    footmarkDataTable.setLatitude("33.9685942");
                    footmarkDataTable.setLongitude("131.2825089");

                    footmarkDataTable = realm.createObject(FootmarkDataTable.class, 7);
                    footmarkDataTable.setPlaceName("片倉温泉");
                    footmarkDataTable.setTitle("片倉温泉です");
                    footmarkDataTable.setReviewBody("片倉温泉です");
                    footmarkDataTable.setAccountName("リーダーミヤケ");
                    footmarkDataTable.setAccountImage(a_bytes3);
                    footmarkDataTable.setAccountId(7);
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(p_bytes8);
                    footmarkDataTable.setPlaceId(7);
                    footmarkDataTable.setPlaceNum(0);
                    footmarkDataTable.setLatitude("33.9729733");
                    footmarkDataTable.setLongitude("131.2990880");

                }
            });
        }

        AppLaunchChecker.onActivityCreate(this);

//        ここはデータベースにアクセスして,すべてのPlaceIdに対応する緯度経度を取得してグーグルマップにマーカーを設置します


        Number maxPlaceId = mRealm.where(FootmarkDataTable.class).max("PlaceId");
        System.out.println("maxPlaceIdは" + maxPlaceId + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        ArrayList<LatLng> latlng = new ArrayList<LatLng>();
        for (int i = 0; i <= maxPlaceId.intValue(); i++) {
            RealmResults<FootmarkDataTable> query = mRealm.where(FootmarkDataTable.class).equalTo("PlaceId", i).findAll();
            FootmarkDataTable footmarkdatatable = query.first();
            String stringLatitude = footmarkdatatable.getLatitude();
            double Latitude = Double.parseDouble(stringLatitude);                   //PlaceIdに対応する緯度の取得
            String stringLongitude = footmarkdatatable.getLongitude();
            double Longitude = Double.parseDouble(stringLongitude);                 //PlaceIdに対応する経度の取得
            String mPlaceName = footmarkdatatable.getPlaceName();                   //PlaceIdに対応する場所の名前の取得

            latlng.add(new LatLng(Latitude, Longitude));                             //緯度経度を渡してlatlngクラス作成
            mMap.addMarker(new MarkerOptions().position(latlng.get(i)).title(mPlaceName)
                    .icon(BitmapDescriptorFactory.fromResource(sample)));
        }


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
        mMap.setOnInfoWindowClickListener(this);                               //InfoWindowがタップされたときの処理
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

    //パーミッション結果の受け取り
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if ((requestCode == REQUEST_PERMISSION) &&
                (grantResults[0] == PackageManager.
                        PERMISSION_GRANTED))
            start();
    }

    //右下のボタンをタップしたときにInputSpotActivityに遷移
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplication(), InputSpotActivity.class);
        String xstring = Double.toString(x);
        String ystring = Double.toString(y);
        intent.putExtra("ido", xstring);
        intent.putExtra("keido", ystring);
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

    public void onButton2Tapped(View view) {                     //実験場への道
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {              //マーカーの上の情報ウィンドウをタップしたときの処理
        String mPlaceName = marker.getTitle();                  //マーカーの場所の名前からデータベースを検索して,場所に対応するPlaceIdをもってくる.このPlaceIdで閲覧画面のデータを管理する
        RealmResults<FootmarkDataTable> query = mRealm.where(FootmarkDataTable.class).equalTo("PlaceName", mPlaceName).findAll();
        FootmarkDataTable footmarkdatatable = query.first();
        int mPlaceId = footmarkdatatable.getPlaceId();
        Intent intent = new Intent(getApplication(), ShowSpotActivity.class);
        //Intent intent = new Intent(this, ShowSpotActivity.class);
        intent.putExtra("PlaceId", mPlaceId);                //intentにPlaceIdを格納して,ShowSpotActivityに渡す."PlaceId"は受け渡し時のカギみたいなもの
        startActivity(intent);
    }

    //位置情報やってんの！！！！
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    //by showing the user a dialog.
                    /*try {
                        // show the dialog by calling startResoutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }*/
                }
            }
        });

    }

    private void startLocationUpdates() {
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null  Looper );*/
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        super.onSaveInstanceState(outState);*/
    }

    @Override
    public void onLocationChanged(Location location) {                  //位置情報が取得されたときの処理
        if(counter<3){
            x = location.getLatitude();
            y = location.getLongitude();
            z = new LatLng(x, y);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(z));
            counter += 1;
            System.out.println("緯度 "+ x + "経度" + y + "!!!!!!!!!!!!!!!!!!!!!!!!!!1");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /*@Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";

        Location location = intent.getParcelableExtra(
                Constants.LOCATTION_DATA_EXTRA
        );

        List<Address> addresses= null;

        try{
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (java.io.IOException ioException) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
            "Latitude = " + location.getLatitude() +
            ", Longitude = " +
            location.getLatitude(), illegalArgumentException);
        }

        if(addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragment = new ArrayList<String>();

            for (int i=0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragment.add(address.getAddressLine(i));
            }
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragment));
        }
    }*/

    final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "com.example.v001ff.footmark";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATTION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
    }

    class AddressResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
       /* public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            if(resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }
        }*/
    }
}