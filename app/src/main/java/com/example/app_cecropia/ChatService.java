package com.example.app_cecropia;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// Class to receive message in the chat
public class ChatService {

    private boolean chat;
    private final Handler mHandler;
    private final String mIp;
    private ChatThread mChatThread;

    DatagramSocket socket;

    public ChatService(Handler handler, String ip) {
        mHandler = handler;
        mIp = ip;
    }

    public synchronized void start() {
        // Start the thread
        chat = true;
        mChatThread = new ChatThread();
        mChatThread.start();
    }

    public synchronized void stop() {
        // Start the thread
        socket.close();
        chat = false;
    }

    public class ChatThread extends Thread {

        public ChatThread() {

        }

        public void run() {
            try {
                while (chat) {
                    //Socket to listen UDP packets sent to port 8888
                    socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));

                    //Buffer to save the packet to be received
                    byte[] buff_msg_received = new byte[20000];
                    DatagramPacket packet = new DatagramPacket(buff_msg_received, buff_msg_received.length);
                    socket.receive(packet);

                    String ip_received = packet.getAddress().getHostAddress();
                    String data = new String(packet.getData()).trim();

                    if(mIp.equals(ip_received) && !data.equals("CHAT_DISCOVER_MSG")){
                        Message msg = mHandler.obtainMessage(ChatActivity.CHAT_MESSAGE_RECEIVED);
                        Bundle bundle = new Bundle();
                        bundle.putString(ChatActivity.CHAT_MESSAGE, data);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }

                    socket.close();
                }
            } catch (IOException ex) {
            }
        }
    }

}
