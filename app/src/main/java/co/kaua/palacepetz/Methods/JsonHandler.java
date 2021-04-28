package co.kaua.palacepetz.Methods;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHandler {
    public static String getJson(String URL_TEXT){
        InputStream inputStream;
        StringBuilder json_text = new StringBuilder();
        try {
            URL url = new URL(URL_TEXT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine()) != null){
                json_text.append("\n").append(line);
            }
        }catch (Exception e){
            Log.d("ErrorNetwork", e.toString());
        }
        return json_text.toString();
    }
}
