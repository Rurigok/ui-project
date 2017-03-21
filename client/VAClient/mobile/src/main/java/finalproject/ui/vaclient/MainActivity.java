package finalproject.ui.vaclient;

import android.media.Image;
import android.support.annotation.XmlRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    EditText txtLine;
    ImageButton process;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLine = (EditText) findViewById(R.id.queryLine);
        process = (ImageButton) findViewById(R.id.processButton);
    }

    public void handleButton(View v)
    {
        str = txtLine.getText().toString();
        System.out.println(str);
    }
}