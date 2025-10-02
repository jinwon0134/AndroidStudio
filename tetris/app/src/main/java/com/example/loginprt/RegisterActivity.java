package com.example.loginprt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextId, editTextPw;
    Button signupButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        editTextId = findViewById(R.id.editTextId);
        editTextPw = findViewById(R.id.editTextPw);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(v -> {
            String id = editTextId.getText().toString();
            String pw = editTextPw.getText().toString();

            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.registerUser(id, pw);
            if (success) {
                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                finish(); // 가입 후 화면 종료 → 로그인 화면으로 돌아감
            } else {
                Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
