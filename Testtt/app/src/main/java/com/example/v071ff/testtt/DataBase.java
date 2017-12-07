package com.example.v071ff.testtt;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by v071ff on 2017/11/22.
 */

//Realmのモデルを作成するためのクラス

public class DataBase extends RealmObject {
    @PrimaryKey
    private long id;

    private Date date;
    private String title;
    private String detail;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
