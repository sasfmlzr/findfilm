package com.sasfmlzr.findfilm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.sasfmlzr.findfilm.model.SystemSettings.API_KEY;

public class Request {
    public String connection(String link) {
        HttpURLConnection uc;
        try {
            URL url = new URL(link);
            uc = (HttpURLConnection) url.openConnection();
            uc.setRequestMethod("GET");
            uc.connect();
            InputStream sd = uc.getInputStream();
            String line;
            StringBuffer tmp = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(sd));
            while ((line = in.readLine()) != null) {
                tmp.append(line);
            }
            return String.valueOf(tmp);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String discoverMovie() {
        return connection("https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "" +
                "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1");
    }

    public String viewMovie(int movie_id) {
        return connection("https://api.themoviedb.org/3/movie/" + movie_id +
                "?api_key=" + API_KEY + "&language=en-US");
    }

}
