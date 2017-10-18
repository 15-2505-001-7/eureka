package com.example.v001ff.footmark;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    //final static private String TAG = "screen2camera";

    static final int REQUEST_CAPTURE_IMAGE = 100;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if(savedInstanceState == null){
           getSupportFragmentManager().beginTransaction().add(R.id.container, new CameraFragment()).commit();
        }*/
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
            }
        });
    }

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
        if(id == R.id.action_setting){
            return true;
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {
        if(REQUEST_CAPTURE_IMAGE == requestCode
                && resultCode == Activity.RESULT_OK ){
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
            ((ImageView) findViewById(R.id.image)).setImageBitmap(capturedImage);
        }
        return super.onOptionsItemSelected(item);
    }*/
}

