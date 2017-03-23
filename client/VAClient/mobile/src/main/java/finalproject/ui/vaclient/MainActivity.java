package finalproject.ui.vaclient;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageButton microphone;
    Button send;
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
        microphone.setVisibility(View.INVISIBLE);
        TextView instruct = (TextView)findViewById(R.id.textView);
        instruct.setVisibility(View.INVISIBLE);

        //Set onTouch listener for mic button
        /*microphone=(ImageButton)findViewById(R.id.micButton);
        microphone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                    switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "Begin recording");
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "End recording");
                        stopRecord();
                        break;
                }
                return false;
            }
        });*/

        txt = (EditText)findViewById(R.id.query);
        txt.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event)
            {
                send = (Button)findViewById(R.id.send);
                send.setVisibility(View.VISIBLE);
               // microphone.setVisibility(View.INVISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(txt,InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        checkOffTouch((View)findViewById(R.id.parent_view));

        /*//onClick listener for play button
        play=(Button)findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onPlay(notPlaying);
                notPlaying = !notPlaying;
            }
        });*/

        messagesContainer = (ListView)findViewById(R.id.chatView);
        final ChatAdapter adapter = new ChatAdapter(MainActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);
        ChatMessage starter = new ChatMessage(true, "Welcome to Voice Assistant!");
        adapter.add(starter);
        adapter.notifyDataSetChanged();
        scroll();
        ChatMessage starter2 = new ChatMessage(true, "Type a query to get started:");
        adapter.add(starter2);
        adapter.notifyDataSetChanged();
        scroll();

        send =(Button)findViewById(R.id.send);
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
                txt.setText("");
                adapter.add(usrInput);
                adapter.notifyDataSetChanged();
                scroll();
                ChatMessage automaticResponse = new ChatMessage(true, "The sending of queries is not yet supported!");
                adapter.add(automaticResponse);
                adapter.notifyDataSetChanged();
                scroll();
            }
        });
    }

    public void checkOffTouch(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(MainActivity.this);
                    send.setVisibility(View.INVISIBLE);
                   // microphone.setVisibility(View.VISIBLE);
                    return true;
                }
            });
        }
    }

    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
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
    /*
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(getFilename());
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("Error", "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }
    */
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
}