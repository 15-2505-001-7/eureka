package com.example.v001ff.footmark;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by v071ff on 2017/10/12.
 */

public class FootmarkDataTable extends RealmObject {
    @PrimaryKey
    private int PlaceId;                     //投稿された場所を管理するためのID
    private long AccountId;                  //アカウントID
    public byte[] AccountImage;            //アカウント画像
    public String AccountName;              //アカウント名
    private String PlaceName;                //投稿された場所の名前
    private String Latitude;                  //投稿された場所の緯度
    private String Longitude;                 //投稿された場所の経度
    private byte[] PlaceImage;              //投稿された場所のイメージ画像
    private Date PlaceDate;                  //投稿された場所の追加された時の日付
    private String Title;                     //レビュータイトル
    public String ReviewBody;               //レビュー本文
    public String ReviewDate;                 //レビューが投稿された日付
    private long MyPlaceName;               //ユーザー自身が投稿した場所の名前
    private String MyLatitude;                //ユーザー自身が投稿した場所の緯度
    private String MyLongitude;               //ユーザー自身が投稿した場所の経度


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

    public String getLatitude() {return Latitude;}

    public String getLongitude() {
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

    public String getReviewBody() {
        return ReviewBody;
    }

    public String getReviewDate() {               //引数を変更するためのセッター
        return ReviewDate;
    }

    public long getMyPlaceName() {
        return MyPlaceName;
    }

    public String getMyLatitude() {
        return MyLatitude;
    }

    public String getMyLongitude() {
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

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
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

    public void setReviewBody(String reviewBody)  { ReviewBody = reviewBody;}

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public void setMyPlaceName(long myPlaceName) {
        MyPlaceName = myPlaceName;
    }

    public void setMyLatitude(String myLatitude) {
        MyLatitude = myLatitude;
    }

    public void setMyLongitude(String myLongitude) {
        MyLongitude = myLongitude;
    }
}
