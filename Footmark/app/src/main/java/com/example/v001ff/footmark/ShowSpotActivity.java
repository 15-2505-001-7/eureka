package com.example.v001ff.footmark;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShowSpotActivity extends AppCompatActivity
        implements PostListFragment.OnFragmentInteractionListener, View.OnClickListener {

    private Realm mRealm;
    private int PID;                    //受け取ったPlaceIdをここに格納する

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_spot);

        //リストビューにアダプタを設定
        mRealm = Realm.getDefaultInstance();

        Intent intent = getIntent();                    //ここでMapsActivityのintentからPlaceIdを取り出す
        if(intent != null){
            PID = intent.getIntExtra("PlaceId",0);      //PIDにPlaceIdを格納する.データがないときは0が返る.
        }

        /*
        mListView = (ListView) findViewById(R.id.listView);
        RealmResults<FootmarkDataTable> footmarkDataTables
                = mRealm.where(FootmarkDataTable.class).findAll();
        PostingAdapter adapter = new PostingAdapter(footmarkDataTables);
        mListView.setAdapter(adapter);
        */
        /*
        Bitmap bitmap = BitmapFactory.decodeByteArray(getPlaceImage,0,getPlaceImage.length);
        */
        //Number maxPlace = mRealm.where(FootmarkDataTable.class).max("PlaceId");
        //ArrayList<LatLng> latlng = new ArrayList<LatLng>();
        //RealmResults<FootmarkDataTable> query = mRealm.where(FootmarkDataTable.class).equalTo("PlaceId", "0").findAll();

        //データベースから画像の情報を取り出し、show_spot_activityに画像を表示
        //[課題]閲覧画面の画像がデモ画像のままなので追加投稿で画像を更新できるようにする!
        RealmResults<FootmarkDataTable> query = mRealm.where(FootmarkDataTable.class).equalTo("PlaceId",PID).findAll();
        FootmarkDataTable footmarkdatatable = query.first();
        byte[] bytes = footmarkdatatable.getPlaceImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        ((ImageView) findViewById(R.id.place_image)).setImageBitmap(bitmap);

        //createTestData();
        showSpotList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    /*private void createTestData() {
        mRealm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                Number maxId = mRealm.where(FootmarkDataTable.class) .max("PlaceId");
                long nextId = 0;
            if(maxId != null) nextId = maxId.longValue() + 1;
            FootmarkDataTable post = realm.createObject(FootmarkDataTable.class, new Long(nextId));
            post.getAccountName();
            post.getReviewBody();
            post.date = "Feb 22";
            }
        });
    }*/

    private void showSpotList() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("PostListFragment");
        if(fragment == null) {
            fragment = new PostListFragment();
            Bundle args = new Bundle();                 //PostListFragmentにPlaceIdを渡すためにBundleを使う
            args.putInt("PIDkey",PID);                  //BundleにPlaceIdをセット　ここまでは正常に動作
            fragment.setArguments(args);                //PostListFragmentにPlaceIdを渡す
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.content, fragment, "PostListFragment");
            transaction.commit();
        }
    }

    @Override
    public void onAddDiarySelected() {
        Toast.makeText(this, "押されました", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this,AddSpotActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplication(), AddSpotActivity.class);
        intent.putExtra("PlaceId",PID);                     //intentにPlaceIdを格納して,AddSpotActivityに渡す.
        startActivity(intent);                                //ちなみにPlaceIdはAddSpotからShowSpotに戻ってくるときに使う
    }
}
