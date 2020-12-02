package com.cs411.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        Button confirm = findViewById(R.id.confirmDelete);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.userToDelete);
                String uname = username.getText().toString();
                if (TextUtils.isEmpty(uname)) {
                    username.setError("This cannot be empty");
                    return;
                }
                try {
                    send(uname);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Delete Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void send(final String userName) throws InterruptedException {
        Thread getInfo = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://10.0.2.2:8080/CS411Project/user?user_name=" + userName);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
        getInfo.start();
        getInfo.join();
    }
}
