package finalproject.ui.vaclient;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by pizzapoindexter on 4/3/17.
 */

public class SendTask extends AsyncTask<String, Void, RSSFeed> {
    private Exception exception;

    protected void doInBackground(ChatMessage usrInput){
        URL url = null;
        HttpURLConnection conn = null;
        try{
            url = new URL("http://localhost:5000/q");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Key", "Value");
            conn.setDoOutput(true);
            OutputStream outputPost = new BufferedOutputStream(conn.getOutputStream());

            //DO A LOT MORE STUFF EVERYWHERE


            outputPost.write(usrInput.getMessage().getBytes());
            outputPost.flush();
            outputPost.close();
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (SocketTimeoutException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
