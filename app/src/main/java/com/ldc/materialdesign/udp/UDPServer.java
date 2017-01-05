package com.ldc.materialdesign.udp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by AA on 2017/1/4.
 */

public class UDPServer implements Runnable{
    private static final int PORT = 6000;
    private byte[] msg = new byte[1024];
    private boolean life = true;
    public UDPServer() {
    }

    public boolean isLife() {
        return life;
    }

    public void setLife(boolean life) {
        this.life = life;
    }

    @Override
    public void run() {
        DatagramSocket dSocket = null;
        DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
        try {
            dSocket = new DatagramSocket(PORT);
            while (life) {
                try {
                    dSocket.receive(dPacket);
                    Log.i("UDPServer", new String(dPacket.getData()));
                    dPacket.setData("ok".getBytes());
                    dSocket.send(dPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
