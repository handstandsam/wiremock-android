package com.handstandsam.wiremock.android;

import android.app.Application;
import android.content.Intent;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.handstandsam.wiremock.android.dagger.components.DaggerWireMockComponent;
import com.handstandsam.wiremock.android.dagger.components.WireMockComponent;
import com.handstandsam.wiremock.android.dagger.modules.AppModule;
import com.handstandsam.wiremock.android.dagger.modules.WireMockModule;

public class WireMockAndroidApplication extends Application {
    private WireMockComponent mWireMockComponent;

    public static WireMockServer wireMockServer = new WireMockServer();

    @Override
    public void onCreate() {
        super.onCreate();

        mWireMockComponent = DaggerWireMockComponent.builder()
                .appModule(new AppModule(this))
                .wireMockModule(new WireMockModule(this))
                .build();

        startService(new Intent(this, WireMockServerBackgroundService.class));
    }

    public WireMockComponent getWireMockServerComponent() {
        return mWireMockComponent;
    }

}
