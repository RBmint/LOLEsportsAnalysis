package com.cs411.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AdvancedFunction1 extends AppCompatActivity {
    float winRate = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_function1);
        View v = findViewById(R.id.af1);
        v.getBackground().setAlpha(80);
        final CustomCircle c = findViewById(R.id.customCircle);
        c.setVisibility(View.GONE);
        final TextView predict = findViewById(R.id.predictText);
        predict.setVisibility(View.GONE);
        final Button calculate = findViewById(R.id.calculateButton);
        String[] champs={"Aatrox","Ahri","Akali","Alistar","Amumu","Anivia","Annie","Aphelios",
                "Ashe","Aurelion Sol","Azir","Bard","Blitzcrank","Brand","Braum","Caitlyn","Camille",
                "Cassiopeia","Cho'Gath","Corki","Darius","Diana","Dr. Mundo","Draven","Ekko","Elise",
                "Evelynn","Ezreal","Fiddlesticks","Fiora","Fizz","Galio","Gangplank","Garen","Gnar",
                "Gragas","Graves","Hecarim","Heimerdinger","Illaoi","Irelia","Ivern","Janna",
                "Jarvan IV","Jax","Jayce","Jhin","Jinx","Kai'Sa","Kalista","Karma","Karthus",
                "Kassadin","Katarina","Kayle","Kayn","Kennen","Kha'Zix","Kindred","Kled","Kog'Maw",
                "LeBlanc","Lee Sin","Leona","Lissandra","Lucian","Lulu","Lux","Malphite","Malzahar",
                "Maokai","Master Yi","Miss Fortune","Mordekaiser","Morgana","Nami","Nasus","Nautilus",
                "Neeko","Nidalee","Nocturne","Nunu and Willump","Olaf","Orianna","Ornn","Pantheon",
                "Poppy","Pyke","Qiyana","Quinn","Rakan","Rammus","Rek'Sai","Renekton","Rengar",
                "Riven","Rumble","Ryze","Sejuani","Senna","Sett","Shaco","Shen","Shyvana","Singed",
                "Sion","Sivir","Skarner","Sona","Soraka","Swain","Sylas","Syndra","Tahm Kench",
                "Taliyah","Talon","Taric","Teemo","Thresh","Tristana","Trundle","Tryndamere",
                "Twisted Fate","Twitch","Udyr","Urgot","Varus","Vayne","Veigar","Vel'Koz","Vi",
                "Viktor","Vladimir","Volibear","Warwick","Wukong","Xayah","Xerath","Xin Zhao",
                "Yasuo","Yorick","Yuumi","Zac","Zed","Ziggs","Zilean","Zoe","Zyra"};
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView2 = findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView2.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView3 = findViewById(R.id.autoCompleteTextView3);
        autoCompleteTextView3.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView4 = findViewById(R.id.autoCompleteTextView4);
        autoCompleteTextView4.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView5 = findViewById(R.id.autoCompleteTextView5);
        autoCompleteTextView5.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView6 = findViewById(R.id.autoCompleteTextView6);
        autoCompleteTextView6.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView7 = findViewById(R.id.autoCompleteTextView7);
        autoCompleteTextView7.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView8 = findViewById(R.id.autoCompleteTextView8);
        autoCompleteTextView8.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView9 = findViewById(R.id.autoCompleteTextView9);
        autoCompleteTextView9.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        final AutoCompleteTextView autoCompleteTextView10 = findViewById(R.id.autoCompleteTextView10);
        autoCompleteTextView10.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    send(autoCompleteTextView.getText().toString(),autoCompleteTextView2.getText().toString(),
                            autoCompleteTextView3.getText().toString(),autoCompleteTextView4.getText().toString(),
                            autoCompleteTextView5.getText().toString(),autoCompleteTextView6.getText().toString(),
                            autoCompleteTextView7.getText().toString(),autoCompleteTextView8.getText().toString(),
                            autoCompleteTextView9.getText().toString(),autoCompleteTextView10.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                calculate.setVisibility(View.GONE);
                predict.setVisibility(View.VISIBLE);
                c.setVisibility(View.VISIBLE);
                //c.setInsideColor(R.color.teamBlue);
                c.setProgress(winRate);
            }
        });
    }
    private void send(final String b0, final String b1, final String b2, final String b3, final String b4,
                      final String r0, final String r1, final String r2, final String r3, final String r4) throws InterruptedException {
        Thread getInfo = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                BufferedReader reader = null;
                try {
                    url = new URL("http://10.0.2.2:8080/CS411Project/search_game");
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

                String jsonInputString = "{\"blue_champ0\": \"" + b0 +
                        "\" ,\"blue_champ1\":\"" + b1 + "\" ,\"blue_champ2\":\"" + b2
                        + "\" ,\"blue_champ3\":\"" + b3 + "\" ,\"blue_champ4\":\"" + b4
                        + "\" ,\"red_champ0\":\"" + r0 + "\" ,\"red_champ1\":\"" + r1
                        + "\" ,\"red_champ2\":\"" + r2 + "\" ,\"red_champ3\":\"" + r3
                        + "\" ,\"red_champ4\":\"" + r4 +"\"}";
                Log.e("s",jsonInputString);

                assert con != null;
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream in = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    if (result.toString().equals("[]")) {
                        return;
                    }
                    JSONObject toBeSaved = new JSONObject(result.toString());
                    Log.e("get",toBeSaved.toString());
                    winRate = (float) toBeSaved.getDouble("blue_team_win_rate");
                    if (winRate < 0) {
                        winRate = 50;
                    }
                    System.out.println(winRate);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                int code = 0;
                try {
                    code = con.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(code);
            }
        });
        getInfo.start();
        getInfo.join();

    }
}
