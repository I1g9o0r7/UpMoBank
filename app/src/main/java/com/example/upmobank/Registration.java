package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    TextInputEditText textInputEditTextFirstName, textInputEditTextLastName, textInputEditTextPhone, textInputEditTextEmail, textInputEditTextPassword;
    Button buttonSubmit;
    String firstName, lastName, phone, email, password;
    TextView textViewError, textViewLoginNow;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textInputEditTextFirstName = findViewById(R.id.firstName);
        textInputEditTextLastName = findViewById(R.id.lastName);
        textInputEditTextPhone = findViewById(R.id.phone);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        textViewLoginNow = findViewById(R.id.loginNow);

        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);

        buttonSubmit = findViewById(R.id.submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textViewError.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                firstName = String.valueOf(textInputEditTextFirstName.getText());
                lastName = String.valueOf(textInputEditTextLastName.getText());
                phone = String.valueOf(textInputEditTextPhone.getText());
                email = String.valueOf(textInputEditTextEmail.getText());
                password = String.valueOf(textInputEditTextPassword.getText());


                //------------------------------------------------------ Connect to DB
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.3.4/php-for-UpMoBank/register.php"; //http://192.168.3.4"; //http://login-registration-android //http://login-registration-android

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++=" + response);
                                if (response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    textViewError.setText(response);
                                    textViewError.setVisibility(View.VISIBLE);
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

                        paramV.put("firstName", firstName);
                        paramV.put("lastName", lastName);
                        paramV.put("phone", phone);
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
                //------------------------------------------ End DB


            }
        });

        textViewLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


    }
}