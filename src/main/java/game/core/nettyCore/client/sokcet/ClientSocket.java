package game.core.nettyCore.client.sokcet;

import com.alibaba.fastjson.JSON;
import game.core.nettyCore.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientSocket {
    private static final Logger logger = LoggerFactory.getLogger(ClientSocket.class);
    private static final int MESSAGE_FRAME_SIZE = 4;
    private String host;
    private int port;
    private Socket socket = null;

    public void connect(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        socket = new Socket(host, port);
        socket.setKeepAlive(true);//开启保持活动状态的套接字
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(5000);//设置超时时间
        this.run();

    }

    public void reConnect() throws IOException {
        socket = new Socket(host, port);
        socket.setKeepAlive(true);//开启保持活动状态的套接字
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(5000);//设置超时时间
        logger.error("重新连接服务器");
        connectStatus = true;
    }

    public <T> T sendMessage(int cmd, Object message, Class responseClazz) throws IOException {
//        Message mes = Message.newBuilder().cmd(cmd).message(message).build();
        byte[] bb = JSON.toJSONBytes(message);
        int totalLength = MESSAGE_FRAME_SIZE + bb.length;
        ByteBuffer wbb = ByteBuffer.allocate(totalLength);
        wbb.putShort((short) totalLength);
        wbb.putShort((short) cmd);
        wbb.put(bb);
        String s = JSON.parseObject(new String(bb, "utf-8")).toString();
        byte[] oarray = wbb.array();
        InputStream sInputStream = null;
        OutputStream sOutputStream = null;
        try {
            if (sInputStream == null)
                sInputStream = socket.getInputStream();
            if (sOutputStream == null)
                sOutputStream = socket.getOutputStream();
            sOutputStream.write(oarray);
            sOutputStream.flush();

            DataInputStream br = new DataInputStream(sInputStream);
            short len = br.readShort();
            short c = br.readShort();
            byte[] resbb = new byte[len - MESSAGE_FRAME_SIZE];
            br.read(resbb);
            if (responseClazz == null) {
                return null;
            } else {
                return JSON.parseObject(resbb, responseClazz);
            }
        } catch (Exception e) {
//            logger.error("异常", e);
            throw e;
        } finally {
            try {
                if (null != sInputStream) {
//                    sInputStream.close();
                }
                if (null != sOutputStream) {
//                    sOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    boolean connectStatus = true;

    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (connectStatus) {
                        try {
                            sendMessage(1, Message.newBuilder().cmd(1).build(), null);
                        } catch (Exception e) {
                            connectStatus = false;
                            logger.error("服务器断开连接", e);
                        }
                    } else {
                        try {
                            reConnect();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            connectStatus = false;
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
