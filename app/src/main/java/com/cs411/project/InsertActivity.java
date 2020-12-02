package com.cs411.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class InsertActivity extends AppCompatActivity {
    private String isModify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        View v = findViewById(R.id.insertLayout);
        v.getBackground().setAlpha(70);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            isModify = Objects.requireNonNull(b.get("modify")).toString();
        }
        if (isModify.equals("true")) {
            EditText toHide = findViewById(R.id.username);
            toHide.setVisibility(View.GONE);
        }
        final Spinner spinner = findViewById(R.id.spinner_insert);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.position_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        final String[] preferred_position = {""};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferred_position[0] = spinner.getSelectedItem().toString();
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
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoChamp);
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, champs));
        Button submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = GlobalVariables.username;
                if (isModify.equals("false")) {
                    EditText username = findViewById(R.id.username);
                    uname = username.getText().toString();
                    if (TextUtils.isEmpty(uname)) {
                        username.setError("This cannot be empty");
                        return;
                    }
                }
                EditText password = findViewById(R.id.pw_re);
                String pw = password.getText().toString();
                if (TextUtils.isEmpty(pw)) {
                    password.setError("This cannot be empty");
                    return;
                }

                EditText email = findViewById(R.id.email);
                String em = email.getText().toString();
                if (TextUtils.isEmpty(em)) {
                    email.setError("This cannot be empty");
                    return;
                }
                EditText phone = findViewById(R.id.phone);
                String p = phone.getText().toString();
                if (TextUtils.isEmpty(p)) {
                    phone.setError("This cannot be empty");
                    return;
                }
                String champ = autoCompleteTextView.getText().toString();
                try {
                    send(uname, pw, em, p, champ, preferred_position[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isModify.equals("true")) {
                    Toast.makeText(getApplicationContext(),"Change Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Register Successful", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void send(final String uname, final String pw, final String em, final String p,
                      final String champ, final String pos) throws InterruptedException {
        Thread getInfo = new Thread(new Runnable() {
            @Override
            public void run() {
            URL url = null;
            try {
                url = new URL("http://10.0.2.2:8080/CS411Project/register");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection con = null;
            try {
                assert url != null;
                con = (HttpURLConnection) url.openConnection();
                if (isModify.equals("true")) {
                    con.setRequestMethod("PUT");
                } else {
                    GlobalVariables.username = uname;
                    con.setRequestMethod("POST");
                }
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String jsonInputString = "{\"user_name\": \"" + uname +
                        "\" ,\"password\":\"" + pw +  "\" ,\"email\":\"" + em + "\" ,\"phone\":\"" + p +
                        "\" ,\"fav_champ\":\"" + champ + "\" ,\"position\":\"" + pos +"\"}";
            Log.e("s",jsonInputString);


            assert con != null;
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (IOException e) {
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
