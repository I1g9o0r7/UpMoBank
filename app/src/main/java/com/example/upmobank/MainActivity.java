package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textViewName, textViewEmail, textViewFetchResult;
    SharedPreferences sharedPreferences;
    Button buttonLogout, buttonFetchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewName = findViewById(R.id.name);
        textViewEmail = findViewById(R.id.email);
        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        buttonLogout = findViewById(R.id.logout);
        buttonFetchUser = findViewById(R.id.fetchProfile);
        textViewFetchResult = findViewById(R.id.fetchResult);


        if(sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://192.168.3.4/login-registration-android/login.php"; //http://192.168.3.4"; //http://login-registration-android //http://login-registration-android

                System.out.println("-------------+++++++++++++++++++++++++++++----------------------------------------" + url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("-------------+++++++++++++++++++++++++++++----------------------------------------" + response);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("logged", "true");
                                editor.putString("name", name);
                                editor.putString("email", email);
                                editor.putString("apiKey", apiKey);
                                editor.apply();


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        textViewError.setText(error.getLocalizedMessage());
                        textViewError.setVisibility(View.VISIBLE);
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });


    }
}