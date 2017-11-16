package com.example.v001ff.footmark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ShowSpotActivity extends AppCompatActivity{

    private final int BTN_START = 0;
    private final int BTN_PREV = 1;
    private final int BTN_NEXT = 2;
    private final int BTN_END = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_spot);
    }
}
