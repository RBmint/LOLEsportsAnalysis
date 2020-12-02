package com.cs411.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.microedition.khronos.opengles.GL;

public class SearchActivity extends AppCompatActivity {
    boolean jumpToNewActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        View v = findViewById(R.id.searchLayout);
        v.getBackground().setAlpha(200);
        Button searchButton = findViewById(R.id.search);
        final EditText editText = findViewById(R.id.editText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToNewActivity = false;
                String str = editText.getText().toString();
                try {
                    send(str);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (jumpToNewActivity) {
                    Intent intent = new Intent(getApplicationContext(), PlayerDisplayActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"Such player does NOT exist in database", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

    }
    private void send(final String gameID) throws InterruptedException {
        Thread getInfo = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://10.0.2.2:8080/CS411Project/player?game_id=" + gameID);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    if (result.toString().equals("[]")) {
                        return;
                    }
                    JSONArray toBeSaved = new JSONArray(result.toString());
                    GlobalVariables.photoURL = toBeSaved.getJSONObject(0).getString("image_url");
                    GlobalVariables.name = "Full Name: " + toBeSaved.getJSONObject(0).getString("full_name");
                    GlobalVariables.nationality = "Nationality: " + toBeSaved.getJSONObject(0).getString("nationality");
                    GlobalVariables.birthdate = "Birth Date: " + toBeSaved.getJSONObject(0).getString("birth_date");
                    GlobalVariables.playerid = "Game ID: " + toBeSaved.getJSONObject(0).getString("game_id");
                    GlobalVariables.team = "Team Name: " + toBeSaved.getJSONObject(0).getString("team_name");
                    GlobalVariables.position = "Position: " + toBeSaved.getJSONObject(0).getString("position");
                    jumpToNewActivity = true;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
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
