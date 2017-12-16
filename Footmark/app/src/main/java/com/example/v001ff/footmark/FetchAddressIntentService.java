package com.example.v001ff.footmark;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;

/**
 * Created by enPiT-P20 on 2017/12/14.
 */

public class FetchAddressIntentService extends IntentService {
    protected ResultReceiver mReceiver;

    public FetchAddressIntentService(String name) {
        super(name);
    }

    private void delivarResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        //bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
