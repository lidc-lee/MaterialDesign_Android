package com.ldc.materialdesign.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by AA on 2017/1/4.
 */

public class UDPClient {

    private static final int SERVER_PORT = 6000;//端口
    private DatagramSocket dSocket = null;
    private ISocketResponse mISocketResponse = null;

    public UDPClient(ISocketResponse mISocketResponse) {
        super();
        this.mISocketResponse = mISocketResponse;
    }

    public String send(String ip, int port, String msg) {
        StringBuilder sb = new StringBuilder();
        InetAddress local = null;
        try {
            local = InetAddress.getByName(ip); // 本机测试
            sb.append("已找到服务器,连接中...").append("\n");
        } catch (UnknownHostException e) {
            sb.append("未找到服务器.").append("\n");
            e.printStackTrace();
        }
        try {
            dSocket = new DatagramSocket(); // 注意此处要先在配置文件里设置权限,否则会抛权限不足的异常
            sb.append("正在连接服务器...").append("\n");
        } catch (SocketException e) {
            e.printStackTrace();
            sb.append("服务器连接失败.").append("\n");
        }
        int msg_len = msg == null ? 0 : msg.length();
        DatagramPacket dPacket = new DatagramPacket(msg.getBytes(), msg_len,
                local, port);
        try {
            dSocket.send(dPacket);
            sb.append("消息发送成功!").append("\n");
            dSocket.receive(dPacket);
            mISocketResponse.onSocketResponse(dPacket.getData());
        } catch (IOException e) {
            e.printStackTrace();
            sb.append("消息发送失败.").append("\n");
        }
        dSocket.close();
        return sb.toString();
    }
}
