package com.example.myapplication;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ScoreHistoryActivity extends Activity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        // 初始化数据库
        ScoreDbHelper dbHelper = new ScoreDbHelper(this);
        db = dbHelper.getWritableDatabase();

        // 查询历史得分
        Cursor cursor = db.query("scores", new String[]{"_id", "score", "time"}, null, null, null, null, "time DESC");

        // 设置适配器
        String[] from = {"time", "score"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cursor, from, to, 0);
        ListView listView = findViewById(R.id.scoreListView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}