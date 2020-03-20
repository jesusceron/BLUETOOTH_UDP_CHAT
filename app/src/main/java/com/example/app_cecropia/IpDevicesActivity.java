package com.example.app_cecropia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class IpDevicesActivity extends AppCompatActivity {

    private ListView listview;
    private ArrayList<String> ips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_devices);

        listview = (ListView) findViewById(R.id.devices_list_view);

        ips = new ArrayList<String>();
        ips.add("192.168.0.1");
        ips.add("192.168.0.2");
        ips.add("192.168.0.3");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, ips
        );
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("ip", ips.get(position));
                startActivity(intent);
            }
        });
    }
}
