package com.example.loginprt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TetrisActivity extends AppCompatActivity {
    private TetrisView tetrisView;
    private Handler handler = new Handler();
    private Runnable dropRunnable;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        tetrisView = findViewById(R.id.tetrisView);
        Button leftButton = findViewById(R.id.leftButton);
        Button rightButton = findViewById(R.id.rightButton);
        Button dropButton = findViewById(R.id.dropButton);
        Button rotateButton = findViewById(R.id.rotateButton);
        Button restartButton = findViewById(R.id.restartButton);
        TextView scoreText = findViewById(R.id.scoreText);

        // 버튼 클릭 연결
        leftButton.setOnClickListener(v -> tetrisView.moveLeft());
        rightButton.setOnClickListener(v -> tetrisView.moveRight());
        rotateButton.setOnClickListener(v -> tetrisView.rotateBlock());
        restartButton.setOnClickListener(v -> {
            tetrisView.restartGame();
            restartButton.setVisibility(Button.GONE);
        });

        // 점수 리스너
        tetrisView.setOnScoreChangeListener(score -> scoreText.setText("Score: " + score));

        // 게임오버 리스너
        tetrisView.setOnGameOverListener(() -> {
            restartButton.setVisibility(Button.VISIBLE);
        });

        // 드롭 버튼 꾹 누르기
        dropRunnable = new Runnable() {
            @Override
            public void run() {
                tetrisView.moveDownFast();
                handler.postDelayed(this, 100);
            }
        };

        dropButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.post(dropRunnable);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacks(dropRunnable);
                    break;
            }
            return true;
        });
    }
}
