package com.cs411.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AdvancedFunction2 extends AppCompatActivity {
    String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_function2);
        final Spinner spinner = findViewById(R.id.spinner_af2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.position_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        final String[] position_to_search = {""};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position_to_search[0] = spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoAF2);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        Button searchByPosition = findViewById(R.id.search_by_position);
        searchByPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    send(position_to_search[0], true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TextView result = findViewById(R.id.search_result);
                result.setText(output);
                result.setMovementMethod(ScrollingMovementMethod.getInstance());
                result.getBackground().setAlpha(100);
            }
        });
        Button searchByChamp = findViewById(R.id.search_by_champ);
        searchByChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    send(autoCompleteTextView.getText().toString(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TextView result = findViewById(R.id.search_result);
                result.setText(output);
                result.setMovementMethod(ScrollingMovementMethod.getInstance());
                result.getBackground().setAlpha(100);
            }
        });
    }

    private void send(final String toSend, final boolean isPosition) throws InterruptedException {
        Thread getInfo = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url;
                    if (isPosition) {
                        url = new URL("http://10.0.2.2:8080/CS411Project/search_user?position=" + toSend);
                    } else {
                        url = new URL("http://10.0.2.2:8080/CS411Project/search_user?fav_champ=" + toSend);
                    }
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
                        output = "No matching data in database!";
                        return;
                    }
                    JSONArray toBeSaved = new JSONArray(result.toString());
                    Log.e("get", toBeSaved.toString());
                    output = "";
                    for (int i = 0; i < toBeSaved.length(); i++) {
                        output += "Username: ";
                        output += toBeSaved.getJSONObject(i).getString("user_name");
                        output += "\nEmail: ";
                        output += toBeSaved.getJSONObject(i).getString("email");
                        output += "\nPhone: ";
                        output += toBeSaved.getJSONObject(i).getString("phone");
                        output += "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
                    }

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
