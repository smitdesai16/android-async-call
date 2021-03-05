package com.smitdesai16.androidasynccall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvMain;
    private String text;

    static class MainActivityOnCreateHandler extends Handler {
        private final MainActivity mainActivity;
        private String text;

        MainActivityOnCreateHandler(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        void InitializeAdditionalParameter(String text) {
            this.text = text;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mainActivity.tvMain.setText(text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity context = this;
        tvMain = findViewById(R.id.tvMain);
        MainActivityOnCreateHandler mainActivityOnCreateHandler = new MainActivityOnCreateHandler(context);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    TodoService todoService = new TodoService();
                    text = todoService.execute().get();
                    mainActivityOnCreateHandler.InitializeAdditionalParameter(text);
                    mainActivityOnCreateHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}