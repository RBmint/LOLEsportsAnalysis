package com.cs411.project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class PlayerDisplayActivity extends AppCompatActivity {
    private ImageView playerPhoto;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
                playerPhoto.setImageBitmap((Bitmap) msg.obj);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_display);
        playerPhoto = findViewById(R.id.playerPhoto);
        new Thread() {
            @Override
            public void run() {
                try {
                    Bitmap result = get();
                    Message msg=Message.obtain();
                    msg.obj=result;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        TextView name = findViewById(R.id.realname);
        name.setText(GlobalVariables.name);
        TextView nationality = findViewById(R.id.nationality);
        nationality.setText(GlobalVariables.nationality);
        TextView birthdate = findViewById(R.id.birthdate);
        birthdate.setText(GlobalVariables.birthdate);
        TextView playerid = findViewById(R.id.playerid);
        playerid.setText(GlobalVariables.playerid);
        TextView team = findViewById(R.id.team);
        team.setText(GlobalVariables.team);
        TextView position = findViewById(R.id.position);
        position.setText((GlobalVariables.position));
    }

    private Bitmap get() throws Exception {
        URL url = new URL(GlobalVariables.photoURL);
        InputStream is=url.openStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        is.close();
        return bitmap;
    }
}
