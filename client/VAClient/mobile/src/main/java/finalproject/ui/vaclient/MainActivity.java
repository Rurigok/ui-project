package finalproject.ui.vaclient;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ImageButton microphone;
    ImageButton send;
    EditText txt;
    ListView messagesContainer;
    boolean notPlaying = false;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static final boolean debug = true;
    private String TAG = "Recording";
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,             MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {android.Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) {
            System.out.print("no permission mic");
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check if the app has recording permissions, if so continue, if not
        //request said permissions
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.RECORD_AUDIO)) {
            } else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }
        //Hide mic button and instruction view for demo purposes
        microphone = (ImageButton)findViewById(R.id.micButton);
        microphone.setVisibility(View.VISIBLE);
        TextView instruct = (TextView)findViewById(R.id.textView);
        instruct.setVisibility(View.INVISIBLE);

        //Set onTouch listener for mic button
        microphone=(ImageButton)findViewById(R.id.micButton);
        microphone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                    /*switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "Begin recording");
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "End recording");
                        stopRecord();
                        break;
                }*/
                return true;
            }
        });

        txt = (EditText)findViewById(R.id.query);
        txt.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event)
            {
                send = (ImageButton)findViewById(R.id.send);
                send.setVisibility(View.VISIBLE);
                microphone.setVisibility(View.INVISIBLE);
                showSoftKeyboard(v);
                return true;
            }
        });

        messagesContainer = (ListView)findViewById(R.id.chatView);
        final ChatAdapter adapter = new ChatAdapter(MainActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);
        messagesContainer.setDivider(null);
        messagesContainer.setDividerHeight(0);
        ChatMessage starter = new ChatMessage(true, "Welcome to Voice Assistant!");
        adapter.add(starter);
        adapter.notifyDataSetChanged();
        scroll();
        ChatMessage starter2 = new ChatMessage(true, "Type a query to get started:");
        adapter.add(starter2);
        adapter.notifyDataSetChanged();
        scroll();

        send =(ImageButton)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = txt.getText().toString();
                if(TextUtils.isEmpty(msg)){
                    return;
                }
                ChatMessage usrInput = new ChatMessage();
                usrInput.setMe(false);
                usrInput.setMessage(msg);
                usrInput.setLocation("San Antonio"); //Placeholder val for now...
                //TODO: Implement GPS functionality and replace this placeholder w/coordinates
                txt.setText("");
                adapter.add(usrInput);
                adapter.notifyDataSetChanged();
                scroll();
                new SendTask().execute(usrInput);

                ChatMessage automaticResponse = new ChatMessage(true, "The sending of queries is not yet supported!");
                adapter.add(automaticResponse);
                adapter.notifyDataSetChanged();
                scroll();
            }
        });
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }


    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    private void startRecord(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Log.e("Recorder", "Error recording");
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Log.e("Warning", "Warning while recording");
        }
    };

    private void stopRecord(){
        if(null != recorder){
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(recorder != null){
            recorder.release();
            recorder = null;
        }
        if(player != null){
            player.release();
            player = null;
        }
    }

    private class SendTask extends AsyncTask<ChatMessage, Void, String> {
        private Exception exception;

        protected String doInBackground(ChatMessage... params){
            String response = sendMessage(params[0]);
            return response;
        }

        public String sendMessage(ChatMessage input){
            URL url = null;
            HttpURLConnection conn = null;
            String response = "Erorr! No response!";
            try{
                url = new URL("http://localhost:5000/q");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("q", input.getMessage());
                conn.setRequestProperty("l", input.getLocation());
                Long timestamp = System.currentTimeMillis()/1000;
                conn.setRequestProperty("t", timestamp.toString());
                conn.setDoOutput(true);
                conn.setDoOutput(true);
                OutputStream outputPost = new BufferedOutputStream(conn.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputPost, "UTF-8"));
                //Build POST request string. Format: q=<query>&l=<location>&t=<timestamp>
                StringBuilder builder = new StringBuilder();
                builder.append(URLEncoder.encode("q", "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(input.getMessage(), "UTF-8"));
                builder.append("&");
                builder.append(URLEncoder.encode("l", "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(input.getLocation(), "UTF-8"));
                builder.append("&");
                builder.append(URLEncoder.encode("t", "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(timestamp.toString(), "UTF-8"));
                writer.write(builder.toString());
                writer.flush();
                writer.close();
                outputPost.close();
                conn.connect();
                response = conn.getResponseMessage();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (SocketTimeoutException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return response;
        }

        //If this works, this will be where handling of the response is yo
        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }

    }
}