package com.example.v001ff.footmark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;

public class ShowSpotActivity extends AppCompatActivity
        implements PostListFragment.OnFragmentInteractionListener, View.OnClickListener {

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_spot);

        mRealm = Realm.getDefaultInstance();

        createTestData();
        showSpotList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void createTestData() {
        mRealm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                Number maxId = mRealm.where(Post.class) .max("id");
                long nextId = 0;
            if(maxId != null) nextId = maxId.longValue() + 1;
            Post post = realm.createObject(Post.class, new Long(nextId));
            post.userName = "ユーザーの名前";
            post.spotInfo = "場所の情報";
            post.date = "Feb 22";
            }
        });
    }

    private void showSpotList() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("PostListFragment");
        if(fragment == null) {
            fragment = new PostListFragment();
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
        startActivity(intent);
    }
}
