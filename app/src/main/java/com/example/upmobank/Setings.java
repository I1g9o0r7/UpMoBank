package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Setings extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ImageView imageViewBackToMain;

    Button buttonChange;
    TextView textViewAccountCode, textViewFirstName, textViewLastName;
    EditText editTextChangeEmailAddress, editTextChangeNumberPhone, editTextChangePassword;

    String phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);

        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        imageViewBackToMain = findViewById(R.id.imageViewBackToMain2);
        buttonChange = findViewById(R.id.buttonChange);

        textViewAccountCode = findViewById(R.id.textViewAccountCode);
        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);

        editTextChangeEmailAddress = findViewById(R.id.editTextChangeEmailAddress);
        editTextChangeNumberPhone = findViewById(R.id.editTextChangeNumberPhone);
        editTextChangePassword = findViewById(R.id.editTextChangePassword);


        textViewAccountCode.setText(sharedPreferences.getString("numCard", ""));
        textViewFirstName.setText(sharedPreferences.getString("firstName", ""));
        textViewLastName.setText(sharedPreferences.getString("lastName", ""));
        editTextChangeEmailAddress.setText(sharedPreferences.getString("email", ""));
        editTextChangeNumberPhone.setText(sharedPreferences.getString("phone", ""));


        imageViewBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.3.4/php-for-UpMoBank/changeInfo.php"; //http://192.168.3.4"; //http://login-registration-android //http://login-registration-android
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++=" + response);
                                if (response.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Changes successful", Toast.LENGTH_SHORT).show();

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "true");
                                    editor.putString("phone", editTextChangeNumberPhone.getText().toString());
                                    editor.putString("email", editTextChangeEmailAddress.getText().toString());
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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

                        paramV.put("email", editTextChangeEmailAddress.getText().toString());
                        paramV.put("phone", editTextChangeNumberPhone.getText().toString());
                        paramV.put("password", editTextChangePassword.getText().toString());
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));

                        return paramV;
                    }
                };
                queue.add(stringRequest);


            }
        });
    }



}




