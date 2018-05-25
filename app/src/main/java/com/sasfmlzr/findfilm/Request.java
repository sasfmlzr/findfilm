package com.sasfmlzr.findfilm;
import android.net.Proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class Request {
    public String connection(String link) throws IOException {
        URL url = new URL(link);
        // Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("us-68-31-2.fri-gate.eu", 443)); // or whatever your proxy is
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        uc.setRequestMethod("GET");
        uc.connect();

        try {
            InputStream sd = uc.getInputStream();
            String line;
            StringBuffer tmp = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(sd));
            while ((line = in.readLine()) != null) {
                tmp.append(line);
            }
            return String.valueOf(tmp);
        } catch (IOException e){
            System.out.println();
            return null;
        }
    }
}
