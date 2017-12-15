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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppLaunchChecker;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.v001ff.footmark.R.mipmap.sample;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , LocationListener {

    private GoogleMap mMap;
    private final int REQUEST_PERMISSION = 1000;
    private Realm mRealm;
    //位置情報用
    private LocationManager manager = null;
    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    long counter = 0;
    public double x;
    public double y;
    //private RequestingLocationUpdates mRequestingLocationUpdates;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateValuesFromBundle(savedInstanceState);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        setTheme(R.style.AppTheme);//splash表示する

        //対応検討中(も)
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationProvider provider =
                manager.getProvider(manager.GPS_PROVIDER);
        //latitude = (TextView) findViewById(R.id.latitude_id);
        //longitude = (TextView) findViewById(R.id.longitude_id);
        //altitude = (TextView) findViewById(R.id.altitude_id);

        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= 23)
            checkPermission();
        else
            start();

        mRealm = Realm.getDefaultInstance();//データベース使用する準備

        /*mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Got last known location In some rare situations this can be null.
                if (location != null) {
                    //Logic to handle location object
                }
            }
        });
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    //Update UI with location data
                    // ...
                }
            }

            ;
        };*/
    }

    @Override
    protected void onPause() {
        if (manager != null) {
            manager.removeUpdates(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (manager != null) {
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
            manager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER, 10, 10, this);
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final boolean gpsEnabled = manager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
        );
        if(!gpsEnabled) {
            enableLocationSettings();
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(counter<3) {
            String str = "緯度 : " + location.getLatitude();
            System.out.println("いどおおおおおおおおおおおおお" + str);
            //latitude.setText(str);
            str = "経度 : " + location.getLongitude();
            System.out.println("けいどおおおおおおおおおおおお" + str);
            //longitude.setText(str);
            str = "高度 : " + location.getAltitude();
            System.out.println("こうどおおおおおおおおおおおお" + str);
            //altitude.setText(str);
            x = location.getLatitude();
            y = location.getLongitude();
            LatLng z = new LatLng(x, y);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(z));
            counter += 1;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider){

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        //Update the value of mRequestingLocationUpdates from the Bundle.
        /*if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            mRequestingLocationUpdates = savedInstanceState.getBoolean(
                    REQUESTING_LOCATION_UPDATES_KEY);
        }
        updateUI();*/
    }


    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATTION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    private void fetchAddressButtonHander(View view) {
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
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mLastLocation = location;

                        if (mLastKnownLocation == null) {
                            return;
                        }

                        if (!Geocoder.isPresent()) {
                            Toast.makeText(MapsActivity.this,
                                    R.string.no_geocoder_available,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        startIntentService();
                        updateUI();
                    }
                });
    }*/
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
        LatLng yu = new LatLng(33.9567058, 131.2727738);
        LatLng zu = new LatLng(33.9304745, 131.2556893);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /*mMap.addMarker(new MarkerOptions().position(yu).title("山口大学工学部")
                .icon(BitmapDescriptorFactory.fromResource(sample)));
        mMap.addMarker(new MarkerOptions().position(zu).title("フジグラン宇部")
                .icon(BitmapDescriptorFactory.fromResource(sample)));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yu));                     //緯度経度の情報がアプリ起動時に中心に表示される
        mMap.setOnInfoWindowClickListener(this);                                //InfoWindowがタップされたときの処理

/*        ここから先はデータベースの処理です
          画像をデータベースに入れるとこでエラーが出るんで,そこを解決できればデモデータもデータベースに格納できます
*/

        if (AppLaunchChecker.hasStartedFromLauncher(this)) {                              //2回目以降の起動はデモの格納はしない
            Log.d("AppLaunchChecker", "2回目以降");
        } else {
            Log.d("AppLaunchChecker", "はじめてアプリを起動した");                 //初回の起動はデモデータをデータベースに入れる
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");              //デモ用の日付をここで設定してます.
            Date date = new Date();
            final String mDate = sdf.format(date);
            Resources r1 = getResources();                                               //デモ用の画像を設定
            Bitmap bmp1 = BitmapFactory.decodeResource(r1, R.drawable.demo0);
            final byte[] bytes1 = MyUtils.getByteFromImage(bmp1);
            Resources r2 = getResources();
            Bitmap bmp2 = BitmapFactory.decodeResource(r2, R.drawable.demo2);
            final byte[] bytes2 = MyUtils.getByteFromImage(bmp2);


            mRealm.executeTransaction(new Realm.Transaction() {                      //デモ用のデータをここでデータベースに格納しています.
                @Override
                public void execute(Realm realm) {
                    FootmarkDataTable footmarkDataTable = realm.createObject(FootmarkDataTable.class, 0);
                    footmarkDataTable.setPlaceName("山口大学工学部");
                    footmarkDataTable.setTitle("山口大学工学部です");
                    footmarkDataTable.setReviewBody("山口大学工学部です");
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(bytes1);
                    footmarkDataTable.setLatitude("33.9567058");
                    footmarkDataTable.setLongitude("131.2727738");

                    footmarkDataTable = realm.createObject(FootmarkDataTable.class, 1);
                    footmarkDataTable.setPlaceName("フジグラン宇部");
                    footmarkDataTable.setTitle("フジグラン宇部です");
                    footmarkDataTable.setReviewBody("フジグラン宇部です");
                    //footmarkDataTable.setPlaceDate(mDate);
                    footmarkDataTable.setReviewDate(mDate);
                    footmarkDataTable.setPlaceImage(bytes2);
                    footmarkDataTable.setLatitude("33.9304745");
                    footmarkDataTable.setLongitude("131.2556893");
                }
            });
        }

        AppLaunchChecker.onActivityCreate(this);


        //ここはデータベースにアクセスして,すべてのPlaceIdに対応する緯度経度を取得してグーグルマップにマーカーを設置します


        Number maxPlace = mRealm.where(FootmarkDataTable.class).max("PlaceId");
        ArrayList<LatLng> latlng = new ArrayList<LatLng>();
        for (int i = 0; i <= maxPlace.intValue(); i++) {
            RealmResults<FootmarkDataTable> query = mRealm.where(FootmarkDataTable.class).equalTo("PlaceId", i).findAll();
            FootmarkDataTable footmarkdatatable = query.first();
            String stringLatitude = footmarkdatatable.getLatitude();
            double Latitude = Double.parseDouble(stringLatitude);                   //PlaceIdに対応する緯度の取得
            String stringLongitude = footmarkdatatable.getLongitude();
            double Longitude = Double.parseDouble(stringLongitude);                 //PlaceIdに対応する経度の取得
            String mPlaceName = footmarkdatatable.getPlaceName();                   //PlaceIdに対応する場所の名前の取得


            latlng.add(new LatLng(Latitude, Longitude));                             //緯度経度を渡してlatlngクラス作成
            System.out.print(latlng.get(i));
            LatLng u = new LatLng(Latitude, Longitude);
            mMap.addMarker(new MarkerOptions().position(u).title(mPlaceName)
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
        intent.putExtra("ido",xstring);
        intent.putExtra("keido",ystring);
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

    /*@Override
    protected void onResume() {
        super.onResume();
        /*if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }*/

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

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        super.onSaveInstanceState(outState);*/
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

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