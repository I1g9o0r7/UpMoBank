package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    TextView textViewCodeAcc, textViewUABalance, textViewUSBalance, textViewEUBalance;
    TextView textViewUSRatesPurchase, textViewUSRatesSale, textViewEURatesPurchase, textViewEURatesSale;
    String dollar_purchase, dollar_sale, euro_purchase, euro_sale;
    SharedPreferences sharedPreferences;
    Button buttonLogout, buttonSetings, buttonSendMoney, buttonSupport, buttonConversion;
    Document doc;
    Thread secThread;
    Runnable runnable;

    List<String> arrayList;



    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchlock;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCodeAcc = findViewById(R.id.textViewCodeAcc);
        textViewUABalance = findViewById(R.id.textViewUABalance);
        textViewUSBalance = findViewById(R.id.textViewUSBalance);
        textViewEUBalance = findViewById(R.id.textViewEUBalance);

        textViewUSRatesPurchase = findViewById(R.id.textViewUSRatesPurchase);
        textViewUSRatesSale = findViewById(R.id.textViewUSRatesSale);
        textViewEURatesPurchase = findViewById(R.id.textViewEURatesPurchase);
        textViewEURatesSale = findViewById(R.id.textViewEURatesSale);

        switchlock = findViewById(R.id.switchLock);
        buttonSendMoney = findViewById(R.id.sendMoney);
        buttonLogout = findViewById(R.id.logout);
        buttonSetings = findViewById(R.id.setings);
        buttonSupport = findViewById(R.id.buttonSupport);
        buttonConversion = findViewById(R.id.buttonConversion);

        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        textViewCodeAcc.setText(sharedPreferences.getString("numCard", ""));
        textViewCodeAcc.setTextIsSelectable(true);
        textViewUABalance.setText(sharedPreferences.getString("balanceUA", ""));
        textViewUSBalance.setText(sharedPreferences.getString("balanceUS", ""));
        textViewEUBalance.setText(sharedPreferences.getString("balanceEU", ""));

        setButtonFunctions();
        switchlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getBaseContext(), "false", Toast.LENGTH_SHORT).show();
                    buttonSendMoney.setOnClickListener(null);
                    buttonSetings.setOnClickListener(null);
                    buttonLogout.setOnClickListener(null);
                    buttonSupport.setOnClickListener(null);
                    buttonConversion.setOnClickListener(null);
                } else {
                    Toast.makeText(getBaseContext(), "true", Toast.LENGTH_SHORT).show();
                    setButtonFunctions();
                }
            }
        });

        init();

    }

    void init(){

        runnable = new Runnable() {
            @Override
            public void run() {
                String[] rates = getWeb();

                textViewUSRatesPurchase.setText(rates[0]);
                textViewUSRatesSale.setText(rates[1]);
                textViewEURatesPurchase.setText(rates[2]);
                textViewEURatesSale.setText(rates[3]);

            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private String[] getWeb(){
        try {
            doc = Jsoup.connect("https://minfin.com.ua/currency/").get();
            String[] result = new String[4];

            Elements tables = doc.getElementsByTag("tbody");
            Element table = tables.get(0);

            String dollar_purchase_str = "" + table.children().get(0).children().get(1).text();
            String[] dollar_purchase_elem = dollar_purchase_str.split(" ");
            dollar_purchase = dollar_purchase_elem[0];
            result[0] = dollar_purchase;

            String dollar_sale_str = "" + table.children().get(0).children().get(2).text();
            String[] dollar_sale_elem = dollar_sale_str.split(" ");
            dollar_sale = dollar_sale_elem[0];
            result[1] = dollar_sale;

            String euro_purchase_str = "" + table.children().get(1).children().get(1).text();
            String[] euro_purchase_elem = euro_purchase_str.split(" ");
            euro_purchase = euro_purchase_elem[0];
            result[2] = euro_purchase;

            String euro_sale_str = "" + table.children().get(1).children().get(2).text();
            String[] euro_sale_elem = euro_sale_str.split(" ");
            euro_sale = euro_sale_elem[0];
            result[3] = euro_sale;

            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void setButtonFunctions() {

        buttonSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SendMoney.class);
                startActivity(intent);
                //finish();

            }
        });

        buttonSetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Setings.class);
                startActivity(intent);
                //finish();
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.3.2/php-for-UpMoBank/logout.php"; //http://192.168.3.4"; //http://login-registration-android //http://login-registration-android

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++=" + response);
                                if (response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");


                                    editor.putString("numCard", "");
                                    editor.putString("phone", "");
                                    editor.putString("firstName", "");
                                    editor.putString("lastName", "");
                                    editor.putString("email", "");
                                    editor.putString("balanceUA", "");
                                    editor.putString("balanceUS", "");
                                    editor.putString("balanceEU", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();


                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
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
                        paramV.put("phone", sharedPreferences.getString("phone", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });

        buttonSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Support.class);
                startActivity(intent);
            }
        });

//        <Button
//        android:id="@+id/buttonConversion"
//        android:layout_width="160dp"
//        android:layout_height="wrap_content"
//        android:layout_marginEnd="25dp"
//        android:text="@string/conversion" />
        buttonConversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Support.class);
                startActivity(intent);
            }
        });

    }

}