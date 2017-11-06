package com.example.v001ff.footmark;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by v071ff on 2017/10/12.
 */

public class FootmarkDataTable extends RealmObject {
    @PrimaryKey
    private long AccountId;                  //アカウントID
    private byte[] AccountImage;            //アカウント画像
    private String AccountName;              //アカウント名
    private int PlaceId;                     //投稿された場所を管理するためのID
    private int AccountPlaceSynthId;       //アカウントと投稿された場所の数を足したものを格納。自分が投稿した場所の検索に使う
    private String PlaceName;                //投稿された場所の名前
    private long Latitude;                  //投稿された場所の緯度
    private long Longitude;                 //投稿された場所の経度
    private byte[] PlaceImage;              //投稿された場所のイメージ画像
    private Date PlaceDate;                  //投稿された場所の追加された時の日付
    private String Title;                     //レビュータイトル
    private String Text;                      //レビュー本文
    private Date ReviewDate;                 //レビューが投稿された日付
    private long MyPlaceName;               //ユーザー自身が投稿した場所の名前
    private long MyLatitude;                //ユーザー自身が投稿した場所の緯度
    private long MyLongitude;               //ユーザー自身が投稿した場所の経度


    public long getAccountId() {              //引数を受け取るゲッター
        return AccountId;
    }

    public byte[] getAccountImage() {
        return AccountImage;
    }

    public String getAccountName() {
        return AccountName;
    }

    public int getPlaceId() {
        return PlaceId;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public long getLatitude() {
        return Latitude;
    }

    public long getLongitude() {
        return Longitude;
    }

    public byte[] getPlaceImage() {
        return PlaceImage;
    }

    public Date getPlaceDate() {
        return PlaceDate;
    }

    public String getTitle() {
        return Title;
    }

    public String getText() {
        return Text;
    }

    public Date getReviewDate() {               //引数を変更するためのセッター
        return ReviewDate;
    }

    public long getMyPlaceName() {
        return MyPlaceName;
    }

    public long getMyLatitude() {
        return MyLatitude;
    }

    public long getMyLongitude() {
        return MyLongitude;
    }


    public void setAccountId(long accountId) {
        AccountId = accountId;
    }

    public void setAccountImage(byte[] accountImage) {
        AccountImage = accountImage;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public void setPlaceId(int placeId) {
        PlaceId = placeId;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public void setLatitude(long latitude) {
        Latitude = latitude;
    }

    public void setLongitude(long longitude) {
        Longitude = longitude;
    }

    public void setPlaceImage(byte[] placeImage) {
        PlaceImage = placeImage;
    }

    public void setPlaceDate(Date placeDate) {
        PlaceDate = placeDate;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setReviewDate(Date reviewDate) {
        ReviewDate = reviewDate;
    }

    public void setMyPlaceName(long myPlaceName) {
        MyPlaceName = myPlaceName;
    }

    public void setMyLatitude(long myLatitude) {
        MyLatitude = myLatitude;
    }

    public void setMyLongitude(long myLongitude) {
        MyLongitude = myLongitude;
    }
}
