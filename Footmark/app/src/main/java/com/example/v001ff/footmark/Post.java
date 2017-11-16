package com.example.v001ff.footmark;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by enPiT-P7 on 2017/11/16.
 */

public class Post extends RealmObject {
       @PrimaryKey
        public long id;
        public String userName;
        public String spoyInfo;
        public String date;
        public byte[] userPhoto;
}
