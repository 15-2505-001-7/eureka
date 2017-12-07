package com.example.v071ff.testtt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class EditCorner extends AppCompatActivity {
    private Realm mRealm;
    EditText mDateEdit;
    EditText mTitleEdit;
    EditText mDetailEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_corner);
        mRealm = Realm.getDefaultInstance();
        mDateEdit = (EditText) findViewById(R.id.dateEdit);
        mTitleEdit = (EditText) findViewById(R.id.titleEdit);
        mDetailEdit = (EditText) findViewById(R.id.detailEdit);
    }

    public void onSaveTapped(View view){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateParse = new Date();
        try{
            dateParse = sdf.parse(mDateEdit.getText().toString());
        }catch(ParseException e){
            e.printStackTrace();
        }

        final Date date = dateParse;
        mRealm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                Number maxId = realm.where(DataBase.class).max("id");
                long nextId = 0;
                if(maxId != null) nextId = maxId.longValue() + 1;
                DataBase database = realm.createObject(DataBase.class, new Long(nextId));
                database.setDate(date);
                database.setTitle(mTitleEdit.getText().toString());
                database.setDetail(mDetailEdit.getText().toString());
            }
        });
        Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
        finish();
    }
}
