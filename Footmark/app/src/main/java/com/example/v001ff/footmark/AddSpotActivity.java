package com.example.v001ff.footmark;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddSpotActivity extends AppCompatActivity {
    private Realm mRealm;                                       //このオブジェクトはDB更新に使う

    private int PID;

    EditText mAddPlaceName;                             //投稿画面の場所の名前入力部分に対応
    EditText mAddReview;                                //投稿画面のレビュー部分に対応
    private EditText mDate;                                      //投稿された日時
    String latitudeRef;                                          //画像から取得する緯度
    String latitude;
    String longitudeRef;                                         //画像から取得する経度
    String longitude;
    Uri mSaveUri;                                       //画像を保存するために使用するUri.Uriは住所みたいなもの.URLの親戚
    String filename;                                    //画像のファイル名をここに保存する.
    Bitmap capturedImage;
    //private long AccountID                                        アカウント機能実装後に、投稿したユーザのIDもデータベースに保存する

    private boolean image = false;

    static final int REQUEST_CAPTURE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_spot);

        mRealm = Realm.getDefaultInstance();                    //Realmを使用する準備。Realmクラスのインスタンスを取得している
        mAddReview = (EditText) findViewById(R.id.addReview);


        Intent intent = getIntent();                    //ここでShowSpotActivityのintentからPlaceIdを取り出す
        if(intent != null){
            PID = intent.getIntExtra("PlaceId",0);      //PIDにPlaceIdを格納する.データがないときは0が返る.
        }

        ImageView spot_photo = (ImageView) findViewById(R.id.spot_photo);
        spot_photo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {                        //カメラ起動するための処理。

                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    // Android 6.0 のみ、カメラパーミッションが許可されていない場合
                    final int REQUEST_CODE = 1;
                    ActivityCompat.requestPermissions(AddSpotActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);            //修正予定ですごめんなさい

                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddSpotActivity.this, Manifest.permission.CAMERA)) {                //修正予定ですごめんなさい
                        // パーミッションが必要であることを明示するアプリケーション独自のUIを表示
                        Snackbar.make(view, R.string.rationale, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // 許可済みの場合、もしくはAndroid 6.0以前
                    // パーミッションが必要な処理。以下でカメラ起動。
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    filename = System.currentTimeMillis() + ".jpg";             //画像ファイルの名前を決める
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, filename);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    mSaveUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mSaveUri);         //mSaveUriにカメラで撮った画像を格納する.これで画質向上狙える//

                    startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);      //カメラで撮影終わった後,onActivityResultを実行

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(REQUEST_CAPTURE_IMAGE == requestCode && resultCode == Activity.RESULT_OK){
            //capturedImage = (Bitmap) data.getExtras().get("data");
            //((ImageView) findViewById(R.id.spot_photo)).setImageBitmap(capturedImage);
            //capturedImage = (Bitmap) data.getExtras().get("data");
            //ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            //capturedImage.compress(Bitmap.CompressFormat.PNG,100,byteArrayStream);

            try {
                capturedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),mSaveUri);        //capturedImageにFileInputStreamで中継してきた画像ファイルを格納
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageView) findViewById(R.id.spot_photo)).setImageBitmap(capturedImage);
            image = true;
        }
    }

    public void onPostingButtonTapped(View view) {
        if(!image && mAddReview == null){
            Toast.makeText(this, "画像・本文が未入力です!", Toast.LENGTH_SHORT).show();
        }else if(!image){
            Toast.makeText(this, "画像が未入力です!", Toast.LENGTH_SHORT).show();
        }else if(mAddReview.getText().toString().equals("")){
            Log.e("","mAddReviewが未入力です");
            Toast.makeText(this, "本文が未入力です!", Toast.LENGTH_SHORT).show();
        }else{
            final Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");        //日付の取得（この段階ではString型）
            final byte[] bytes = MyUtils.getByteFromImage(capturedImage);

            Resources r = getResources();                                  //デモユーザーの情報を設定
            Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.yamame);
            final byte[] byteDemoUser = MyUtils.getByteFromImage(bmp);/*

        try {
            dateParse = sdf.parse(mDate.getText().toString());
            ExifInterface exifInterface = new ExifInterface(capturedImage.toString());              //p283にRealmでの画像の扱い方書いてるので参照して修正予定
            latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);        //緯度の取得
            latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);      //経度の取得
            longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        */
            //final Date date = dateParse;
            final String date2 = df.format(date);
            mRealm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    Number maxPostNum = realm.where(FootmarkDataTable.class).max("PostNum");                        //最大の投稿数を取得する
                    long nextPostNum = 0;
                    if(maxPostNum != null) nextPostNum = maxPostNum.longValue() + 1;
                    RealmResults<FootmarkDataTable> query = mRealm.where(FootmarkDataTable.class).equalTo("PlaceId",PID).findAll();
                    Number maxPlaceNum = query.max("PlaceNum");                                                              //最大の場所ごとの投稿数を取得する
                    int nextPlaceNum = 0;
                    if(maxPlaceNum != null) nextPlaceNum = maxPlaceNum.intValue() + 1;
                    //realm.beginTransaction();
                    FootmarkDataTable footmarkDataTable = realm.createObject(FootmarkDataTable.class, new Long(nextPostNum));
                    footmarkDataTable.setPlaceNum(nextPlaceNum);
                    footmarkDataTable.setAccountName("デモユーザーさん");
                    footmarkDataTable.setAccountImage(byteDemoUser);
                    footmarkDataTable.setReviewBody(mAddReview.getText().toString());
                    footmarkDataTable.setReviewDate(date2);
                    footmarkDataTable.setPlaceImage(bytes);
                    footmarkDataTable.setPlaceId(PID);
                    //footmarkDataTable.setLatitude(latitude);
                    //footmarkDataTable.setLongitude(longitude);
                    //realm.commitTransaction();
                }
            });
            //ここにRealmにデータ追加する文を書く
            Toast.makeText(this, "投稿しました!", Toast.LENGTH_SHORT).show();

            //追加箇所
        /*
        RealmResults<FootmarkDataTable> query = mRealm.where(FootmarkDataTable.class).findAll();
        FootmarkDataTable footmarkdatatable = query.first();
        byte[] bytes = footmarkdatatable.getPlaceImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        ((ImageView) findViewById(R.id.place_image)).setImageBitmap(bitmap);
        */


            startActivity(new Intent(AddSpotActivity.this, MapsActivity.class));
            finish();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();                         //投稿画面から離れるときにDBのリソース開放
    }

}
