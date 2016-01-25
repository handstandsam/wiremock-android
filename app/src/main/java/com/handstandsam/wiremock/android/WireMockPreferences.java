package com.handstandsam.wiremock.android;

public interface WireMockPreferences {
    int getPort();

    void setPort(int port);

    int getHttpsPort();

    void setHttpsPort(int port);

    boolean getUseHttps();

    void setUseHttps(boolean useHttps);

    String getLocalUrl();
}
