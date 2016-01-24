package com.handstandsam.wiremock.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.tomakehurst.wiremock.WireMockServer;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindString(R.string.start_server)
    String startServerString;


    @BindString(R.string.stop_server)
    String stopServerString;

    @Bind(R.id.portEditText)
    EditText portEditText;


    @Bind(R.id.buttonOpenBrowser)
    Button buttonOpenBrowser;

    @Bind(R.id.button)
    Button startStopButton;

    @Inject
    WireMockServer wireMockServer;

    @Inject
    WireMockPreferences wireMockPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((WireMockAndroidApplication) getApplication()).getWireMockServerComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
    }

    private void initUI() {
        if (wireMockServer.isRunning()) {
            startStopButton.setText(stopServerString);
            portEditText.setText(Integer.toString(wireMockServer.port()));
            portEditText.setEnabled(false);
            buttonOpenBrowser.setVisibility(View.VISIBLE);
        } else {
            startStopButton.setText(startServerString);
            portEditText.setText(Integer.toString(wireMockPreferences.getPort()));
            portEditText.setEnabled(true);
            buttonOpenBrowser.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.button)
    public void startStopButtonClicked() {
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        } else {
            int port = Integer.parseInt(portEditText.getText().toString());
            wireMockPreferences.setPort(port);
            wireMockServer = new WireMockServer(port);
            wireMockServer.start();
        }
        initUI();
    }

    @OnClick(R.id.buttonOpenBrowser)
    public void openBrowser() {
        int port = wireMockPreferences.getPort();
        String url = "http://localhost:" + port + "/__admin";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}
