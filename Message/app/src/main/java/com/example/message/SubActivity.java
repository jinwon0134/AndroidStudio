package com.example.message;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();                    //전달된 데이터를 수신한다.
        String msg = intent.getStringExtra(MainActivity.TAG_MSG);//데이터 추출해서 msg 저장
        textView = findViewById(R.id.textView);
        textView.setText(msg);              //메세지를 textView에 출력한다.

    }
}