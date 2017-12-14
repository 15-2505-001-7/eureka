package com.example.v001ff.footmark;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class InputSpotActivity extends AppCompatActivity {
    private Realm mRealm;                                       //このオブジェクトはDB更新に使う

    EditText mAddPlaceName;                             //投稿画面の場所の名前入力部分に対応
    EditText mAddReview;                                //投稿画面のレビュー部分に対応
    //private Date mDate;                                      //投稿された日時
    String latitudeRef;                                          //画像から取得する緯度
    String latitude;
    String longitudeRef;                                         //画像から取得する経度
    String longitude;
    Bitmap capturedImage;
    //private long AccountID                                        アカウント機能実装後に、投稿したユーザのIDもデータベースに保存する

    static final int REQUEST_CAPTURE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_input_spot);

        mRealm = Realm.getDefaultInstance();//Realmを使用する準備。Realmクラスのインスタンスを取得している
        mAddPlaceName = (EditText) findViewById(R.id.addPlaceName);
        mAddReview = (EditText) findViewById(R.id.addReview);

        ImageView spot_photo = (ImageView) findViewById(R.id.spot_photo);
        spot_photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {                        //カメラ起動するための処理。試作。

                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    // Android 6.0 のみ、カメラパーミッションが許可されていない場合
                    final int REQUEST_CODE = 1;
                    ActivityCompat.requestPermissions(InputSpotActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);            //修正予定ですごめんなさい

                    if (ActivityCompat.shouldShowRequestPermissionRationale(InputSpotActivity.this, Manifest.permission.CAMERA)) {                //修正予定ですごめんなさい
                        // パーミッションが必要であることを明示するアプリケーション独自のUIを表示
                        Snackbar.make(view, R.string.rationale, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // 許可済みの場合、もしくはAndroid 6.0以前
                    // パーミッションが必要な処理。以下でカメラ起動。
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CAPTURE_IMAGE == requestCode && resultCode == Activity.RESULT_OK) {

            //capturedImage = (Bitmap) data.getExtras().get("data");
            //((ImageView) findViewById(R.id.spot_photo)).setImageBitmap(capturedImage);
            capturedImage = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            Bitmap capturedImage1 = Bitmap.createScaledBitmap(capturedImage, 300, 469, false); //300×469にリサイズ
            capturedImage1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayStream);
            ((ImageView) findViewById(R.id.spot_photo)).setImageBitmap(capturedImage1);
            //((ImageView) findViewById(R.id.place_image)).setImageBitmap(capturedImage1);

            /*
            String saveDir = Environment.getExternalStorageDirectory().getPath() + "/test";
            File file = new File(saveDir);

            if (!file.exists()) {
                if (!file.mkdir()) {
                    Log.e("Debug", "Make Dir Error");
                }
            }
            */

            // 画像保存パス

        }
    }

    public void onPostingButtonTapped(View view) {
        //long currentTimeMillis = System.currentTimeMillis();
        final Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");        //日付の取得（この段階ではString型）
        //String dateParse = new String();
        final byte[] bytes = MyUtils.getByteFromImage(capturedImage);

        try {
            //String date2 = df.format(date);
            //[課題]画像からの位置情報を取得

            Log.e("!!!!!!","1実行できてます!");
            ExifInterface exifInterface = new ExifInterface(saveBitmap(capturedImage));//p283にRealmでの画像の扱い方書いてるので参照して修正予定
            //ExifInterface exifInterface = new ExifInterface(capturedImage.toString());
            Log.e("!!!!!!","2実行できてます!");
            latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);        //緯度の取得
            latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);      //経度の取得
            longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            Log.e("!!!!!!","3実行できてます!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final String date2 = df.format(date);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxId = realm.where(FootmarkDataTable.class).max("PlaceId");
                long nextId = 0;
                if (maxId != null) nextId = maxId.longValue() + 1;
                //realm.beginTransaction();
                FootmarkDataTable footmarkDataTable = realm.createObject(FootmarkDataTable.class, new Long(nextId));
                footmarkDataTable.setPlaceName(mAddPlaceName.getText().toString());
                footmarkDataTable.setReviewBody(mAddReview.getText().toString());
                footmarkDataTable.setReviewDate(date2);
                //footmarkDataTable.setPlaceDate(date);
                footmarkDataTable.setPlaceImage(bytes);
                footmarkDataTable.setLatitude(latitude);
                footmarkDataTable.setLongitude(longitude);

                //realm.commitTransaction();
            }
        });
        //ここにRealmにデータ追加する文を書く
        Toast.makeText(this, "投稿しました!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(InputSpotActivity.this, MapsActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();                         //投稿画面から離れるときにDBのリソース開放
    }

    public String saveBitmap(Bitmap saveImage) throws IOException {

        final String SAVE_DIR = "/MyPhoto/";
        File file = new File(Environment.getExternalStorageDirectory().getPath() + SAVE_DIR);
        try {
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            throw e;
        }


        Date mDate = new Date();
        SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = fileNameDate.format(mDate) + ".jpg";
        String AttachName = file.getAbsolutePath() + "/" + fileName;

        try {
            Log.e("!!!!!",fileName);
            Log.e("!!!!!",AttachName);
            FileOutputStream out = new FileOutputStream(AttachName);
            Log.e("!!!!!","実行!!");
            saveImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.e("!!!!!","実行!!!");
            out.flush();
            Log.e("!!!!!","実行!!!!");
            out.close();
            Log.e("!!!!!","実行!!!!!");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        Log.e("!!!","実行!!!");
        // save index
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put("_data", AttachName);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        System.out.println("!!!!!!!!!!!!!!!!!!fileName="+fileName);
        System.out.println("!!!!!!!!!!!!!!!!!!AttachName="+AttachName);
        Log.e("debug",fileName);//実行できてない
        return fileName;
    }

}