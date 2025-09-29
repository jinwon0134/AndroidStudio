package com.example.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;   // ✅ 날짜 포맷용
import java.util.Date;              // ✅ 날짜 포맷용
import java.util.Locale;            // ✅ 날짜 포맷용

import static com.example.sqlite.SQLiteHelper.TABLE_NAME;
import static com.example.sqlite.SQLiteHelper.ID;
import static com.example.sqlite.SQLiteHelper.TIME;
import static com.example.sqlite.SQLiteHelper.TITLE;

public class MainActivity extends AppCompatActivity {

    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sqLiteHelper = new SQLiteHelper(this);

        // ✅ “Hello Android” 저장 (현재 시각 millis 함께)
        writeDB("Hello Android");

        Cursor cursor = readDB();
        displayDB(cursor);
        cursor.close();
        sqLiteHelper.close();
    }

    private void displayDB(Cursor cursor){
        StringBuilder builder = new StringBuilder("Saved DB:\n"); // "\n" 로 줄바꿈
        while (cursor.moveToNext()){
            long id = cursor.getLong(0);      // ID
            long time = cursor.getLong(1);    // TIME (epoch millis)
            String title = cursor.getString(2); // TITLE

            builder.append(id).append(": ");
            builder.append(formatEpochMillis(time)).append(" - "); // ✅ 년월일 시간 포맷
            builder.append(title).append("\n");
        }
        TextView textView = findViewById(R.id.textView);
        textView.setText(builder.toString());
    }

    private Cursor readDB() {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    private void writeDB(String title){
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis()); // ✅ epoch millis
        values.put(TITLE, title);
        db.insertOrThrow(TABLE_NAME, null, values);   // ✅ 실제 insert 수행
    }

    // ✅ “yyyy년 MM월 dd일 HH:mm:ss” 포맷으로 표시
    private String formatEpochMillis(long epochMillis){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss", Locale.KOREA);
        return sdf.format(new Date(epochMillis));
    }
}
