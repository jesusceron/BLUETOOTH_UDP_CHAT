package com.example.app_cecropia;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// This class receives the packets sent to the port 8888
// If the received message is "CHAT_DISCOVER_MSG", this class responds to the sender with a "CHAT_RESPONSE_MSG" message.
// The main activity receives that response and add the IP address of the sender to the list.

public class DiscoverDevices {

    private boolean discover;
    private final Handler mHandler;
    private DiscoverThread mDiscoverThread;

    DatagramSocket socket;

    public DiscoverDevices(Handler handler) {
        mHandler = handler;
    }

    public synchronized void start() {
        // Start the thread
        discover = true;
        mDiscoverThread = new DiscoverThread();
        mDiscoverThread.start();
    }

    public synchronized void stop() {
        // Start the thread
        discover = false;
        socket.close();
    }

    public class DiscoverThread extends Thread {

        public DiscoverThread() {

        }

        public void run() {
            try {
                while (discover) {

                    //Socket to listen UDP packets sent to port 8888
                    socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
                    socket.setBroadcast(true);

                    Log.d("DiscoverDevices", "Listening packets");

                    //Buffer to save the packet to be received
                    byte[] buff_msg_received = new byte[20000];
                    DatagramPacket packet = new DatagramPacket(buff_msg_received, buff_msg_received.length);
                    socket.receive(packet);

                    Log.d("Discover Devices", "Discover MSG received from: " + packet.getAddress().getHostAddress());

                    String ip_received = packet.getAddress().getHostAddress();
                    String data = new String(packet.getData()).trim();

                    if (data.equals("CHAT_DISCOVER_MSG")) {

                        byte[] buff_msg_sent = "CHAT_RESPONSE_MSG".getBytes();

                        //Send a response
                        DatagramPacket sendPacket = new DatagramPacket(buff_msg_sent, buff_msg_sent.length, packet.getAddress(), packet.getPort());
                        socket.send(sendPacket);


                        // Send to the UI the IP address of the device that responded to the 'CHAT_DISCOVER_MSG'
                        Message msg = mHandler.obtainMessage(MainActivity.IP_RECEIVED);
                        Bundle bundle = new Bundle();
                        bundle.putString(MainActivity.IP, ip_received);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);


                        Log.d("Discover Devices", "Response MSG sent to: " + sendPacket.getAddress().getHostAddress());
                    }

                    socket.close();

                }
            } catch (IOException ex) {
                Log.d("Discover Devices", ex.toString());
            }
        }
    }
}
