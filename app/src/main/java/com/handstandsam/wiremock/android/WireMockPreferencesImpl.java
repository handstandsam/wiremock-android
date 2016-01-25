package com.handstandsam.wiremock.android;

import android.content.Context;
import android.content.SharedPreferences;

public class WireMockPreferencesImpl implements WireMockPreferences {

    private static final String PORT = "PORT";
    private static final String HTTPS_PORT = "HTTPS_PORT";
    private static final String USE_HTTPS = "USE_HTTPS";

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

    @Override
    public int getHttpsPort() {
        return sharedPreferences.getInt(HTTPS_PORT, 8081);
    }

    @Override
    public void setHttpsPort(int port) {
        sharedPreferences.edit().putInt(HTTPS_PORT, port).apply();
    }

    @Override
    public boolean getUseHttps() {
        return sharedPreferences.getBoolean(USE_HTTPS, false);
    }

    @Override
    public void setUseHttps(boolean useHttps) {
        sharedPreferences.edit().putBoolean(USE_HTTPS, useHttps).apply();
    }

    @Override
    public String getLocalUrl() {
        int port = getPort();
        String protocol = "http";
        if (getUseHttps()) {
            protocol = "https";
        }
        String url = protocol + "://" + NetworkUtils.getIPAddress(true) + ":" + port + "/__admin";
        return url;
    }
}
