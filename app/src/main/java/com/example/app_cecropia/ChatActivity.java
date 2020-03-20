package com.example.app_cecropia;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private TextView message;
    private ListView chatListview;
    private Button sendButton;
    private ArrayList<MessageModel> messages;
    private ChatAdapter adapter;
    private TextView titleTextView;
    private String ip;

    private ChatService chatService;

    public static final int CHAT_MESSAGE_RECEIVED = 2;
    public static final String CHAT_MESSAGE = "chat_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ip = getIntent().getStringExtra("ip");

        message = (TextView) findViewById(R.id.message);
        chatListview = (ListView) findViewById(R.id.chat_list_view);
        sendButton = (Button) findViewById(R.id.send_button);
        titleTextView = (TextView) findViewById(R.id.title_text_view);

        titleTextView.setText(ip);

        messages = new ArrayList<>();

        adapter= new ChatAdapter(messages, getApplicationContext());

        chatListview.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!message.getText().toString().equals("")) {
                    messages.add(new MessageModel(message.getText().toString(), 0xFFFFFFFF));

                    sendMessage(ip, message.getText().toString());

                    adapter.notifyDataSetChanged();
                    message.setText("");
                }
            }
        });

        chatService = new ChatService(mHandler, ip);
        chatService.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatService.stop();
    }

    @SuppressLint("HandlerLeak")
    // The Handler that gets information back from the thread
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHAT_MESSAGE_RECEIVED:
                    //Log.d("MAIN", "chat message arrived");

                    // Chat message received. Put it in the conversation list
                    String chat_received = msg.getData().getString(CHAT_MESSAGE);

                    //Log.d("Message", chat_received);

                    if(chat_received != "" && chat_received != ""){
                        messages.add(new MessageModel(chat_received, 0xFF43A047));
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    // method that send the messages in the chat
    public void sendMessage(String ip, String msg) {
        try {
            //Open a random port to send the message
            DatagramSocket c = new DatagramSocket();

            byte[] sendData = msg.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), 8888);
            c.send(sendPacket);
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
