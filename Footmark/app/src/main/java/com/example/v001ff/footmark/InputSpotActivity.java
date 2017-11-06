package com.example.v001ff.footmark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;

public class InputSpotActivity extends AppCompatActivity {
    private Realm mRealm;                                       //このオブジェクトはDB更新に使う

    private EditText mAddPlaceName;                             //投稿画面の場所の名前入力部分に対応
    private EditText mAddReview;                                //投稿画面のレビュー部分に対応

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_input_spot);

        mRealm = Realm.getDefaultInstance();                    //Realmを使用する準備。Realmクラスのインスタンスを取得している
        mAddPlaceName = (EditText) findViewById(R.id.addPlaceName);
        mAddReview = (EditText) findViewById(R.id.addReview);
    }

    public void onPostingButtonPTapped(View view) {

        //ここにRealmにデータ追加する文を書く
        //あとボタンの名前をinputから変えたほうがいい

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();                         //投稿画面から離れるときにDBのリソース開放
    }
}