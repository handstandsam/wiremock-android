package com.handstandsam.wiremock.android;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.tomakehurst.wiremock.WireMockServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class DashboardFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(DashboardFragment.class);


    @BindString(R.string.start_server)
    String startServerString;

    @BindString(R.string.you_are_not_connected_to_wifi_warning)
    String youAreNotConnectedToWiFiWarning;

    @BindString(R.string.stop_server)
    String stopServerString;

    @BindString(R.string.view_admin_mappings_browser)
    String viewAdminMappingsBrowserString;

    @Bind(R.id.port)
    EditText portEditText;

    @Bind(R.id.server_running_container)
    View serverIsRunningContainer;

    @Bind(R.id.ip_address)
    TextView ipAddressTextView;
    @Bind(R.id.warning)
    TextView warningTextView;

    @Bind(R.id.buttonOpenBrowser)
    Button buttonOpenBrowser;

    @Bind(R.id.button)
    Button startStopButton;

    @Inject
    WireMockPreferences wireMockPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

//        setContentView(R.layout.fragment_dashboard);
        ButterKnife.bind(this, fragmentView);
        ((WireMockAndroidApplication) getActivity().getApplication()).getWireMockServerComponent().inject(this);

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUIAgainIn(1000);
        initUI();
    }

    private void initUIAgainIn(int i) {
        //Check again in a second to see if it's just starting up...
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (getActivity() != null) {
                            initUI();
                        }
                    }
                },
                i);
    }

    private void initUI() {
        if (!NetworkUtils.isWifiConnected(getActivity())) {
            warningTextView.setText(youAreNotConnectedToWiFiWarning);
            warningTextView.setVisibility(View.VISIBLE);
        } else {
            warningTextView.setVisibility(View.GONE);
        }
        String url = wireMockPreferences.getLocalUrl();
        buttonOpenBrowser.setText(viewAdminMappingsBrowserString);
        ipAddressTextView.setText(url);
        portEditText.setText(Integer.toString(wireMockPreferences.getPort()));
        if (WireMockAndroidApplication.wireMockServer.isRunning()) {
            startStopButton.setText(stopServerString);
            portEditText.setText(Integer.toString(WireMockAndroidApplication.wireMockServer.port()));
            portEditText.setEnabled(false);
            serverIsRunningContainer.setVisibility(View.VISIBLE);
        } else {
            startStopButton.setText(startServerString);
            portEditText.setEnabled(true);
            serverIsRunningContainer.setVisibility(View.GONE);
        }
    }

    @OnTextChanged(R.id.port)
    public void portChanged() {
        try {
            Integer port = Integer.parseInt(portEditText.getText().toString());
            wireMockPreferences.setPort(port);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @OnClick(R.id.button)
    public void startStopButtonClicked() {
        if (WireMockAndroidApplication.wireMockServer.isRunning()) {
            WireMockAndroidApplication.wireMockServer.shutdown();
        } else {
            int port = wireMockPreferences.getPort();

            WireMockAndroidApplication.wireMockServer = new WireMockServer(wireMockConfig().port(port));
            WireMockAndroidApplication.wireMockServer.start();
            initUIAgainIn(1000);
            initUI();
        }
    }

    @OnClick(R.id.buttonOpenBrowser)
    public void openBrowser() {
        String url = wireMockPreferences.getLocalUrl();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
