package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textViewName, textViewEmail, textViewFetchResult;
    SharedPreferences sharedPreferences;
    Button buttonLogout, buttonSetings, buttonSendMoney;
    Switch switchlock;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewName = findViewById(R.id.name);
        textViewEmail = findViewById(R.id.email);

        switchlock = findViewById(R.id.switchLock);
        buttonSendMoney = findViewById(R.id.sendMoney);
        buttonLogout = findViewById(R.id.logout);
        buttonSetings = findViewById(R.id.setings);
        textViewFetchResult = findViewById(R.id.fetchResult);

        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);
        if(sharedPreferences.getString("logged", "false").equals("false")){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

//        textViewName.setText(sharedPreferences.getString("name", ""));
//        textViewEmail.setText(sharedPreferences.getString("email", ""));


        System.out.println("--------------------------------------------------------------------"+ sharedPreferences.getString("email", ""));
        System.out.println("--------------------------------------------------------------------"+ sharedPreferences.getString("name", ""));
        System.out.println("--------------------------------------------------------------------"+ sharedPreferences.getString("password", ""));
        System.out.println("--------------------------------------------------------------------"+ sharedPreferences.getString("apiKey", ""));



        setButtonFunctions();
        switchlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked == true){
                    Toast.makeText(getBaseContext(), "false", Toast.LENGTH_SHORT).show();
                    buttonSendMoney.setOnClickListener(null);
                    buttonSetings.setOnClickListener(null);
                } else{
                    Toast.makeText(getBaseContext(), "true", Toast.LENGTH_SHORT).show();
                    setButtonFunctions();
                }
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.3.4/login-registration-android/logout.php"; //http://192.168.3.4"; //http://login-registration-android //http://login-registration-android


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equals("success")){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("name", "");
                                    editor.putString("email", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();


                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else{
                                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

    }

    private void setButtonFunctions() {

        buttonSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SendMoney.class);
                startActivity(intent);
                finish();

            }
        });

        buttonSetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





//                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//                String url = "http://192.168.3.4/login-registration-android/profile.php"; //http://192.168.3.4"; //http://login-registration-android //http://login-registration-android
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//                                System.out.println("================================================================================"+response);
//                                textViewFetchResult.setText(response);
//                                textViewFetchResult.setVisibility(View.VISIBLE);
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }) {
//                    protected Map<String, String> getParams() {
//                        Map<String, String> paramV = new HashMap<>();
//                        paramV.put("email", sharedPreferences.getString("email", ""));
//                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
//                        return paramV;
//                    }
//                };
//                queue.add(stringRequest);
            }
        });

    }




}