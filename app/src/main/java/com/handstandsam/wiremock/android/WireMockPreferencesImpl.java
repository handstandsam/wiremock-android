package com.handstandsam.wiremock.android;

import android.content.Context;
import android.content.SharedPreferences;

public class WireMockPreferencesImpl implements WireMockPreferences {

    private static final String PORT = "PORT";

    private final SharedPreferences sharedPreferences;

    public WireMockPreferencesImpl(Context context) {
        this.sharedPreferences = context.getSharedPreferences("wiremock", Context.MODE_PRIVATE);
    }

    @Override
    public int getPort() {
        return sharedPreferences.getInt(PORT, 8080);
    }

    @Override
    public void setPort(int port) {
        sharedPreferences.edit().putInt(PORT, port).apply();
    }
}
