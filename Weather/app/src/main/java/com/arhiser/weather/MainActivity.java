package com.arhiser.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button main_bth, clear_bth, buttonLink;
    private TextView result_info, result_info2, result_info3, result_info4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_bth = findViewById(R.id.main_btn);
        clear_bth = findViewById(R.id.clear_btn);
        result_info = findViewById(R.id.result);
        result_info2 = findViewById(R.id.result2);
        result_info3 = findViewById(R.id.result3);
        result_info4 = findViewById(R.id.result4);
        buttonLink = findViewById(R.id.buttonLink);


        main_bth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_field.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                } else if (user_field.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                } else {
                    String city = user_field.getText().toString();
                    String key = "2cf5f4a4b6c576926ae7177032a3d282";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";
                    new GetURLData().execute(url);

                }
            }


        });

        clear_bth.setOnClickListener(new View.OnClickListener() {   //очистка
            @Override
            public void onClick(View view) {
                user_field.setText("");
                result_info.setText("");
                result_info2.setText("");
                result_info3.setText("");
                result_info4.setText("");
            }
        });

        buttonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://sinoptik.ua/"));
                startActivity(browserIntent);
            }
        });


    }

    private class GetURLData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {

                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                result_info.setText("Температура:      " + jsonObject.getJSONObject("main").getLong("temp") + " °C");
                result_info2.setText("Відчувається:     " + jsonObject.getJSONObject("main").getLong("feels_like") + " °C");
                result_info3.setText("Максимальна:    " + jsonObject.getJSONObject("main").getLong("temp_max") + " °C");
                result_info4.setText("Мінімальна:         " + jsonObject.getJSONObject("main").getLong("temp_min") + " °C");
                result_info4.setText("Мінімальна:         " + jsonObject.getJSONObject("main").getLong("temp_min") + " °C");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
