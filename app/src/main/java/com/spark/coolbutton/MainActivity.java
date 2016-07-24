package com.spark.coolbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.spark.submitbutton.SubmitButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SubmitButton sb = (SubmitButton) findViewById(R.id.btn3);
        sb.setOnClickListener(new View.OnClickListener() {
            public static final String TAG = "onclick";

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submit");
            }
        });
    }
}
