package com.example.v001ff.footmark;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by v071ff on 2017/10/11.
 * アプリ起動時にアクティビティよりも先に呼び出されます
 */

public class FootmarkApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
