package com.handstandsam.wiremock.android.dagger.modules;

import android.app.Application;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.handstandsam.wiremock.android.WireMockPreferences;
import com.handstandsam.wiremock.android.WireMockPreferencesImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WireMockModule {

    private final Application application;

    public WireMockModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    WireMockServer wireMockServer(WireMockPreferences wireMockPreferences) {
        WireMockServer wireMockServer = new WireMockServer(wireMockPreferences.getPort());
        return wireMockServer;
    }

    @Provides
    @Singleton
    WireMockPreferences wireMockPreferences() {
        return new WireMockPreferencesImpl(application);
    }


}