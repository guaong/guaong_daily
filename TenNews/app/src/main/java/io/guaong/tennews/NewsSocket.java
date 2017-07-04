package io.guaong.tennews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by 关桐 on 2017/7/3.
 */

public class NewsSocket {

    private Socket socket;

    public NewsSocket(){
        try {
            socket = new Socket("192.168.1.103", 8887);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收服务器发送的html内容
     * @return html内容
     */
    public String receiveData(){
        String content = null;
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while (!((line = br.readLine())== null)){
                content += line;
            }
        } catch (IOException e) {
            return null;
        }
        return content;
    }

}
