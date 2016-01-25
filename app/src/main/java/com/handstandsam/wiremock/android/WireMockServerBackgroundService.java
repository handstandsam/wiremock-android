package com.handstandsam.wiremock.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.github.tomakehurst.wiremock.WireMockServer;

import javax.inject.Inject;

public class WireMockServerBackgroundService extends Service {

//    @Inject
//    WireMockServer wireMockServer;

    @Inject
    WireMockPreferences wireMockPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        ((WireMockAndroidApplication) getApplication()).getWireMockServerComponent().inject(this);
//        Toast.makeText(this, "WireMock starting.", Toast.LENGTH_SHORT).show();
//        wireMockServer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "WireMock stopping.", Toast.LENGTH_SHORT).show();
        WireMockServer wireMockServer = WireMockAndroidApplication.wireMockServer;
        if (wireMockServer != null) {
            if (wireMockServer.isRunning()) {
                wireMockServer.stop();
            }
        }
    }
}