package com.cs411.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                intent.putExtra("modify","false");
                startActivity(intent);
                finish();
            }
        });
        Button loginButton = findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname, pw;
                EditText username = findViewById(R.id.username);
                uname = username.getText().toString();
                EditText password = findViewById(R.id.password);
                pw = password.getText().toString();
                int code = 0;
                try {
                    code = send(uname, pw);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (code == 200) {
                    GlobalVariables.username = uname;
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private int send(final String uname, final String pw) throws InterruptedException {
        final int[] code = {0};
        Thread getInfo = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("http://10.0.2.2:8080/CS411Project/login");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection con = null;
                try {
                    assert url != null;
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                    con.setRequestProperty("Accept", "application/json");
                    con.setDoOutput(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String jsonInputString = "{\"user_name\": \"" + uname +
                        "\" ,\"password\":\"" + pw +"\"}";


                assert con != null;
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    code[0] = con.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(code[0]);
            }
        });
        getInfo.start();
        getInfo.join();
        return code[0];
    }
}
