package com.example.app_cecropia;

import android.os.Handler;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// This class sends a UDP package with the message "CHAT_DISCOVER_MSG" to all the devices in the local network
// If one of the devices in the networks answer with a message "CHAT_RESPONSE_MSG", this class sends the IP address of that device to the UI to be listed.
public class Client {

    private boolean client;
    private final Handler mHandler;
    private ClientThread mClientThread;

    DatagramSocket c;

    public Client(Handler handler) {
        mHandler = handler;
    }

    public synchronized void start() {
        // Start the thread
        client = true;
        mClientThread = new ClientThread();
        mClientThread.start();
    }

    public synchronized void stop() {
        // Start the thread
        c.close();
        client = false;
    }

    public class ClientThread extends Thread {

        public ClientThread() {}


        public void run() {

            while (client) {
                try {
                    //Open a random port to send the package
                    c = new DatagramSocket();
                    c.setBroadcast(true);

                    byte[] sendData = "CHAT_DISCOVER_MSG".getBytes();

                    // send discover package to broadcast IP ("192.168.0.255")
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("192.168.0.255"), 8888);
                    c.send(sendPacket);
                    Log.d("Client", "discover packet sent to 192.168.0.255");
                    c.close();

                    Thread.sleep(1000);
                } catch (Exception ex) {
                    Log.e("Client", ex.toString());
                }
            }
        }
    }
}