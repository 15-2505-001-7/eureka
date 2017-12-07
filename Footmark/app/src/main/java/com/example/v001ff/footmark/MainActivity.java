package com.example.v001ff.footmark;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements PostListFragment.OnFragmentInteractionListener {

    private Realm mRealm;
    private ListView mListView;
    //final static private String TAG = "screen2camera";

    static final int REQUEST_CAPTURE_IMAGE = 100;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {                        //カメラ起動するための処理。試作。

                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    // Android 6.0 のみ、カメラパーミッションが許可されていない場合
                    final int REQUEST_CODE = 1;
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);            //修正予定ですごめんなさい

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {                //修正予定ですごめんなさい
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

        //リストビューにアダプターを設定
        mRealm = Realm.getDefaultInstance();

        /*
        mListView = (ListView) findViewById(R.id.listView);
        RealmResults<FootmarkDataTable> footmarkDataTables
                = mRealm.where(FootmarkDataTable.class).findAll();
        PostingAdapter adapter = new PostingAdapter(footmarkDataTables);
        mListView.setAdapter(adapter);
        */




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(REQUEST_CAPTURE_IMAGE == requestCode && resultCode == Activity.RESULT_OK){
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            ((ImageView) findViewById(R.id.spot_photo)).setImageBitmap(capturedImage);
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            capturedImage.compress(Bitmap.CompressFormat.PNG,0,byteArrayStream);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onAddDiarySelected() {

    }
    //データベースの中身表示

    /*
    private void createTestData(){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm)
        });
    }
    */
    /*
    public void onClickButton(View view){
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.button);
    }
    */

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_setting) {
            return true;
            @Override
            protected void onActivityResult ( int requestCode, int resultCode, Intent data){
                if (REQUEST_CAPTURE_IMAGE == requestCode
                        && resultCode == Activity.RESULT_OK) {
                    Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
                    ((ImageView) findViewById(R.id.image)).setImageBitmap(capturedImage);
                }
                return super.onOptionsItemSelected(item);
            }
*/
}
