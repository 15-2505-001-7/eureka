package com.example.v001ff.footmark;

import android.graphics.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    final static private String TAG = "screen2camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new CameraFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_setting){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class CameraFragment extends Fragment {

        private Camera camera;
        View rootView;
        SurfaceView surfaceView;

        private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder){
                camera = Camera.open();
                try {
                    camera.setPreviewDisplay(holder);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.release();
                camera = null;
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d(TAG, "surfaceChanged width:"+width+"height:"+height);

                Camera.Parameters parameters = camera.getParameters();

                Size size = parameters.getPictureSize();
                Log.d(TAG, "getPictureSize width:" + size.width + "size.height:" + size.height);
                size = parameters.getPreviewSize();
                Log.d(TAG, "getPreviewSize width:" + size.width + "size.height:" + size.height);

                parameters.setPreviewSize(640, 480);
                camera.setParameters(parameters);
                camera.startPreview();
            }
        };

        private Camera.ShutterCallback shutterListener = new Camera.ShutterCallback() {
            public void onShutter(){
            }
        };

        private Camera.PictureCallback pictureListener = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                if(data != null) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/camera_test.jpg");
                        fos.white(data);
                        fos.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    camera.startPreview();
                }
            }
        };

        OnTouchListener ontouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(camera != null) {
                        camera.takePicture(shutterListener,null,pictureListener);
                    }
                }
                return false;
            }
        };

        public CameraFragment(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            surfaceView = (SurfaceView) rootView.findViewById(R.id.surface_view);

            SurfaceHolder holder = surfaceView.getHolder();
            holder.addCallback(surfaceListener);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            rootView.setOnTouchListener(ontouchListener);

            return  rootView;
        }
    }
}


