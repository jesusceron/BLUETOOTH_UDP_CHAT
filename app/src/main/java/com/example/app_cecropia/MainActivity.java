package com.example.app_cecropia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DiscoverDevices mdiscoverDevices = null;
    private Client mclient = null;
    public static final int IP_RECEIVED = 1;
    public static final String IP = "ip";

    private ListView devicesListView;
    private ArrayList<String> ips;
    private ArrayAdapter<String> adapter;

    String phone_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // List for devices detected in the network
        ips = new ArrayList<String>();
        devicesListView = (ListView) findViewById(R.id.devices_list_view);
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, ips
        );
        devicesListView.setAdapter(adapter);

        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("ip", ips.get(position));
                startActivity(intent);
                mdiscoverDevices.stop();
                mclient.stop();
                //finish();
            }
        });

        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        phone_IP = Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());

        // Initialize Threads
        mdiscoverDevices = new DiscoverDevices(mHandler);
        mdiscoverDevices.start();
        mclient = new Client(mHandler);
        mclient.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("HandlerLeak")
    // Handler that gets information back from the Threads
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IP_RECEIVED:

                    String ip_received = msg.getData().getString(IP);

                    if (phone_IP.equals(ip_received)) {
                        // Do not add to list
                    } else {

                        // Add IP address to the list
                        if(!ips.contains(ip_received)){
                            ips.add(ip_received);
                            adapter.notifyDataSetChanged();
                        }

                    }
                    break;
            }
        }
    };
}
