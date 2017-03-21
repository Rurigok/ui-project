package finalproject.ui.vaclient;

import android.support.annotation.XmlRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    String txtLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLine = findViewById(R.id.queryLine).toString();
        System.out.println(txtLine);
    }

    public void handleButton()
    {
        System.out.println("HI :D" + txtLine);
    }
}