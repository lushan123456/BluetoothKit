package com.inuker.bluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.IBluetoothClient;
import com.inuker.bluetooth.library.connect.response.BluetoothResponse;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResponse;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.security.BleRegisterConnector;

public class MainActivity extends Activity {

    private static final String MAC = "B0:D5:9D:6F:E7:A5";

    private Button mBtnConnect;
    private Button mBtnDisconnect;

    private IBluetoothClient mClient;

    private BleRegisterConnector mConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnector = new BleRegisterConnector(MAC, 149);

        mClient = BluetoothClient.getInstance(this);

        mBtnConnect = (Button) findViewById(R.id.connect);
        mBtnConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                connect();
            }
        });

        mBtnDisconnect = (Button) findViewById(R.id.disconnect);
        mBtnDisconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                disconnect();
            }
        });

    }

    private void connect() {
//        mConnector.connect(new BluetoothResponse() {
//            @Override
//            public void onResponse(int code, Bundle data) throws RemoteException {
//                BluetoothLog.v(String.format("MainActivity.onResponse code = %d", code));
//            }
//        });

        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)
                .searchBluetoothClassicDevice(5000)
                .searchBluetoothLeDevice(2000)
                .build();

        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() throws RemoteException {
                BluetoothLog.v(String.format("MainActivity.onSearchStarted in %s", Thread.currentThread().getName()));
            }

            @Override
            public void onDeviceFounded(SearchResult device) throws RemoteException {
                BluetoothLog.v(String.format("MainActivity.onDeviceFounded in %s, mac = %s, rssi = %d",
                        Thread.currentThread().getName(), device.device.getAddress(), device.rssi));
            }

            @Override
            public void onSearchStopped() throws RemoteException {
                BluetoothLog.v(String.format("MainActivity.onSearchStopped in %s", Thread.currentThread().getName()));
            }

            @Override
            public void onSearchCanceled() throws RemoteException {
                BluetoothLog.v(String.format("MainActivity.onSearchCanceled in %s", Thread.currentThread().getName()));
            }
        });
    }

    private void disconnect() {
//        mClient.disconnect(MAC);

        ClientManager.getClient().stopSearch();
    }
}