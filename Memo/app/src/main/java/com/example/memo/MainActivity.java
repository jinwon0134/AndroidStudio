package com.example.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button addButton;
    private ListView memoListView;
    private ArrayList<String> memoList;
    private ArrayList<String> memoListShort; // 20자만 보여줄 리스트
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        memoListView = findViewById(R.id.memoListView);

        memoList = new ArrayList<>();
        memoListShort = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memoListShort);
        memoListView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MemoActivity.class);
            startActivityForResult(intent, 1);
        });

        // 리스트 클릭 시 전체 메모 보여주기
        memoListView.setOnItemClickListener((parent, view, position, id) -> {
            String fullMemo = memoList.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("메모 내용");
            builder.setMessage(fullMemo);

            // 닫기 버튼
            builder.setPositiveButton("닫기", (dialog, which) -> dialog.dismiss());

            // 삭제 버튼
            builder.setNegativeButton("삭제", (dialog, which) -> {
                memoList.remove(position);
                memoListShort.remove(position);
                adapter.notifyDataSetChanged();
            });

            builder.show();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String memoText = data.getStringExtra("memo");
            memoList.add(memoText);

            // 20자까지만 리스트에 표시
            if (memoText.length() > 20) {
                memoListShort.add(memoText.substring(0, 20) + "...");
            } else {
                memoListShort.add(memoText);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
