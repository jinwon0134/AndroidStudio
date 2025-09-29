package com.example.inputmessage; // ← 패키지명 프로젝트에 맞게 수정하세요

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // xml 파일 이름과 맞아야 함

        // xml 뷰 연결
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        // 버튼 클릭 이벤트
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString().trim();
                if (!input.isEmpty()) {
                    textView.setText(input);
                } else {
                    textView.setText("⚠ 입력칸이 비어있습니다!");
                }
            }
        });
    }
}
