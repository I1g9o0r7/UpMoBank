package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView textViewRegisterNow;
    TextInputEditText textInputEditTextPhone, textInputEditTextPassword;
    Button buttonSubmit;

    String numCard, phone, password, firstName, lastName, email, apiKey;
    String balanceUA, balanceUS, balanceEU;
    TextView textViewError;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextPhone = findViewById(R.id.phone);
        textInputEditTextPassword = findViewById(R.id.password);

        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);

        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "false").equals("true")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        buttonSubmit = findViewById(R.id.submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewError.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                phone = String.valueOf(textInputEditTextPhone.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.3.2/php-for-UpMoBank/login.php"; //http://192.168.3.4"; //http://login-registration-android //http://login-registration-android

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++=" + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equals("success")) {

                                        numCard = jsonObject.getString("id");
                                        phone = jsonObject.getString("phone");
                                        firstName = jsonObject.getString("firstName");
                                        lastName = jsonObject.getString("lastName");
                                        email = jsonObject.getString("email");
                                        balanceUA = jsonObject.getString("balanceUA");
                                        balanceUS = jsonObject.getString("balanceUS");
                                        balanceEU = jsonObject.getString("balanceEU");
                                        apiKey = jsonObject.getString("apiKey");


                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged", "true");

                                        editor.putString("numCard", numCard);
                                        editor.putString("phone", phone);
                                        editor.putString("firstName", firstName);
                                        editor.putString("lastName", lastName);
                                        editor.putString("email", email);
                                        editor.putString("balanceUA", balanceUA);
                                        editor.putString("balanceUS", balanceUS);
                                        editor.putString("balanceEU", balanceEU);
                                        editor.putString("apiKey", apiKey);
                                        editor.apply();


                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
//                                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                        textViewError.setText(message);
                                        textViewError.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                        paramV.put("phone", phone);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        textViewRegisterNow = findViewById(R.id.registerNow);
        textViewRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
                finish();
            }
        });
    }
}






























