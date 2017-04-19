package finalproject.ui.vaclient;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

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
    public static final int RequestPermissionCode = 1;
    public static final boolean debug = true;
    private String TAG = "Recording";
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private int currentFormat = 0;
    private int output_formats[] = { MediaRecorder.OutputFormat.MPEG_4,             MediaRecorder.OutputFormat.THREE_GPP };
    private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };

    private static final String SESSION_TOKEN = generateSessionToken();

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private boolean permissionStorage = false;
    private String [] permissions = {android.Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    permissionToRecordAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    permissionStorage = (grantResults[1] == PackageManager.PERMISSION_GRANTED);

                    if(permissionToRecordAccepted && permissionStorage)
                    {
                        Log.i("I", "PERMISSION GRANTED");
                    }
                    else
                        Log.i("I", "PERMISSION DENIED");

                }
        }
        if (!permissionToRecordAccepted ) {
            System.out.print("no permission mic");
            finish();
        }
    }

    protected static String generateSessionToken() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder token = new StringBuilder();
        Random rnd = new Random();
        while (token.length() < 24) { // length of the random string.
            int index = (int) (rnd.nextFloat() * alphabet.length());
            token.append(alphabet.charAt(index));
        }
        return token.toString();
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

        microphone = (ImageButton)findViewById(R.id.micButton);
        //microphone.setVisibility(View.VISIBLE);
        //TextView instruct = (TextView)findViewById(R.id.textView);

        //Set onTouch listener for mic button
//        microphone=(ImageButton)findViewById(R.id.micButton);
//        microphone.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event){
//                    switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.i(TAG, "Begin recording");
//                        startRecord();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.i(TAG, "End recording");
//                        stopRecord();
//                        break;
//                }
//                return true;
//            }
//        });

        txt = (EditText)findViewById(R.id.query);
        txt.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event)
            {
                send = (ImageButton)findViewById(R.id.send);
                send.setVisibility(View.VISIBLE);
                //microphone.setVisibility(View.INVISIBLE);
                showSoftKeyboard(v);
                return true;
            }
        });
        txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("TEST RESPONSE", "Action ID = " + actionId + "KeyEvent = " + event);
                return true;
            }
        });


        messagesContainer = (ListView)findViewById(R.id.chatView);
        adapter = new ChatAdapter(MainActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);
        messagesContainer.setDivider(null);
        messagesContainer.setDividerHeight(0);
        ChatMessage starter = new ChatMessage(true, "Hey there! I'm Mercury, your personal assistant! Call me Merk for short." + "\n\n" + new SimpleDateFormat("hh:mm a").format(new Date()));
        adapter.add(starter);
        adapter.notifyDataSetChanged();
        scroll();
        ChatMessage starter2 = new ChatMessage(true, "Type a command or press the mic to get started \n (type \"help\" to know what I can do)"+ "\n\n" + new SimpleDateFormat("hh:mm a").format(new Date()));
        adapter.add(starter2);
        adapter.notifyDataSetChanged();
        scroll();


        txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    submitMessage(txt.getText().toString(), adapter);
                    handled = true;
                }
                return handled;
            }
        });

        recorder = new MediaRecorder();


        send =(ImageButton)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = txt.getText().toString();
                submitMessage(msg, adapter);

//                if(TextUtils.isEmpty(msg)){
//                    return;
//                }
//                String temp = msg;
//                temp += ("\n\n" + new SimpleDateFormat("hh:mm a").format(new Date()));
//                ChatMessage usrInput = new ChatMessage();
//                ChatMessage tempMsg = new ChatMessage();
//                usrInput.setMe(false);
//                tempMsg.setMessage(temp);
//                usrInput.setLocation("San Antonio"); //Placeholder val for now...
//                //TODO: Implement GPS functionality and replace this placeholder w/coordinates
//                txt.setText("");
//                adapter.add(tempMsg);
//                adapter.notifyDataSetChanged();
//                scroll();
//                usrInput.setMessage(msg);
//                new SendTask(adapter).execute(usrInput);
//
////                ChatMessage automaticResponse = new ChatMessage(true, "The sending of queries is not yet supported!");
////                adapter.add(automaticResponse);
//                adapter.notifyDataSetChanged();
//                scroll();
//                Toast.makeText(getApplicationContext(), "The sending of queries is not yet supported!", Toast.LENGTH_SHORT).show();


//                ChatMessage automaticResponse2 = new ChatMessage(true, "hi");
//                adapter.add(automaticResponse2);
//                adapter.notifyDataSetChanged();
//                scroll();

            }
        });
        /////
    }
    /////////////////////////////////voice stuff//////////////////////////
    public void onMicClick(View v){
        promptSpeechInput();
    }
    public void promptSpeechInput(){
        Intent i = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a command");

        try{
            startActivityForResult(i,100);

        }catch (ActivityNotFoundException a){
            Toast.makeText(MainActivity.this, "Sorry! Your device doesn't support speech language", Toast.LENGTH_LONG).show();
        }


    }

    public void onActivityResult(int request_code, int result_code, Intent i){
        super.onActivityResult(request_code,result_code,i);

        switch(request_code){
            case 100: if(result_code == RESULT_OK && i != null){
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                //resulttext.setText(result.get(0));
                for(String s : result){
                    Log.e("RESULT", s);
                }
                Log.e("In", result.get(0));
                submitMessage(result.get(0),adapter);
            }
            break;
        }




    }



    /////////////////////////////////////////end voice stuff/////////////


    public void submitMessage(String msg, ChatAdapter adapter ){
        Log.e("Recording",msg);
        if(msg.isEmpty()) {
            msg = txt.getText().toString();

        }
        String temp = msg;
        if(TextUtils.isEmpty(msg) || TextUtils.isEmpty(temp)){
            return;
        }
        ChatMessage usrInput = new ChatMessage();
        ChatMessage tempMsg = new ChatMessage();
        usrInput.setDate(new SimpleDateFormat("hh:mm a").format(new Date()));
        temp += ("\n\n" + usrInput.getDate());
        usrInput.setMe(false);
        tempMsg.setMessage(temp);
        usrInput.setLocation("San Antonio"); //Placeholder val for now...
        //TODO: Implement GPS functionality and replace this placeholder w/coordinates
        txt.setText("");
        adapter.add(tempMsg);
        adapter.notifyDataSetChanged();
        scroll();
        usrInput.setMessage(msg);
        new SendTask(adapter).execute(usrInput);
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
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

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
            //recorder.reset();
            //recorder.release();
            //recorder = null;
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

    /////////////////////////////send task class/////////////////////////

    private class SendTask extends AsyncTask<ChatMessage, Void, String> {
        private Exception exception;
        private ChatAdapter chatAdapter;

        public SendTask(ChatAdapter chatAdapter)
        {
            super();
            this.chatAdapter = chatAdapter;
        }

        protected String doInBackground(ChatMessage... params){
            String response = sendMessage(params[0]);
            return response;
        }

        public String sendMessage(ChatMessage input){
            URL url = null;
            HttpURLConnection conn = null;
            String response = "Error! No response!";
            try{
                url = new URL("http://162.209.100.212:5000/q");
                //url = new URL("http://10.0.2.2:5000/q");
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

                //Build POST request string. Format: q=<query> l=<location> t=<timestamp>
                HashMap<String, String> form = new HashMap<>();

                form.put("q", input.getMessage());
                form.put("l", input.getLocation());
                form.put("t", timestamp.toString());
                form.put("session_token", SESSION_TOKEN);

                writer.write(urlEncode(form));

                writer.flush();
                writer.close();
                outputPost.close();
                conn.connect();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (SocketTimeoutException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            // check http response
            try {
                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    response = "";
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                }
            } catch (Exception e) {
            }

            return response;
        }

        //If this works, this will be where handling of the response is yo
        @Override
        protected void onPostExecute(String result) {

            String text = null;
            String command = null;

            try {
                JSONObject jsonResponse = new JSONObject(result);
                text = jsonResponse.getString("text");
                command = jsonResponse.getString("command");
            } catch (JSONException e) {

            }

            if (text == null)
                return;

            text += "\n\n" + new SimpleDateFormat("hh:mm a").format(new Date());
            ListView messagesContainer = (ListView)findViewById(R.id.chatView);
            messagesContainer.setAdapter(chatAdapter);
            messagesContainer.setDivider(null);
            messagesContainer.setDividerHeight(0);
            ChatMessage starter = new ChatMessage(true, text);
            chatAdapter.add(starter);
            chatAdapter.notifyDataSetChanged();
            scroll();
//            Log.e("Warning", result);

        }

        public String urlEncode(HashMap<String, String> form) {
            StringBuilder encoded = new StringBuilder();

            // URL encode the form key-value pairs. A trailing ampersand will be added which
            // shouldn't be a problem.
            try {
                for (String key : form.keySet()) {
                    encoded.append(URLEncoder.encode(key, "UTF-8"));
                    encoded.append("=");
                    encoded.append(URLEncoder.encode(form.get(key), "UTF-8"));
                    encoded.append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return encoded.toString();

        }


    }
}