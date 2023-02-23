package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.upmobank.filters.DecimalDigitsInputFilter;
import com.example.upmobank.filters.NegativeDecimalInputFilter;

import java.util.HashMap;
import java.util.Map;

public class SendMoney extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    String[] currency = {"UA", "US", "EU"};
    String[] methodsSend = {"Card", "Phone"};
    ImageView imageViewBackToMain;
    Spinner spinnerCurrencyСhoice, spinnerMethodsSendChoice;
    TextView textViewAmountOfMoney, textViewSimbolBalance;

    //----------------------------------------------------
    String valute, amount;
    //----------------------------------------------------

    EditText editTextMethodSendMoney;
    EditText editTextAmountOfMoney;

    Button buttonSendMoney;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        imageViewBackToMain = findViewById(R.id.imageViewBackToMain);

        spinnerCurrencyСhoice = findViewById(R.id.spinnerCurrencyСhoice);
        spinnerMethodsSendChoice = findViewById(R.id.spinnerMethodSendСhoice);
        textViewAmountOfMoney = findViewById(R.id.textViewAmountOfMoney);
        textViewSimbolBalance = findViewById(R.id.textViewSimbolBalance);

        editTextMethodSendMoney = findViewById(R.id.editTextMethodSendMoney);
        editTextAmountOfMoney = findViewById(R.id.editTextAmountOfMoney);
        editTextAmountOfMoney.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2), new NegativeDecimalInputFilter()});

        editTextAmountOfMoney.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().matches("^-")){editTextAmountOfMoney.setText("");}}
            @Override
            public void afterTextChanged(Editable editable) {}}
        );

        buttonSendMoney = findViewById(R.id.buttonSendMoney);

        buttonSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("-----------------------------------------------------" + spinnerCurrencyСhoice.getSelectedItem().toString());
//                System.out.println("-----------------------------------------------------" + spinnerMethodsSendChoice.getSelectedItem().toString());


                switch(spinnerCurrencyСhoice.getSelectedItem().toString()){
                    case "UA":
                        valute = "balanceUA";
                        break;
                    case "US":
                        valute = "balanceUS";
                        break;
                    case "EU":
                        valute = "balanceEU";
                        break;
                }

                if(Double.parseDouble(editTextAmountOfMoney.getText().toString()) > 0){
                    if(Double.parseDouble(editTextAmountOfMoney.getText().toString()) <= Double.parseDouble(sharedPreferences.getString(valute, ""))){
                        amount = editTextAmountOfMoney.getText().toString();
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url = "http://192.168.3.4/php-for-UpMoBank/senderSM.php"; //http://192.168.3.4"; //http://login-registration-android //http://login-registration-android
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++=" + response);
                                        if (response.equals("success")) {
                                            Toast.makeText(getApplicationContext(), "Changes successful", Toast.LENGTH_SHORT).show();

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("logged", "true");
                                            editor.putString(valute, "" + (Double.parseDouble(sharedPreferences.getString(valute, "")) - Double.parseDouble(amount)));
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

                                paramV.put("valute", valute);
                                paramV.put("method", spinnerMethodsSendChoice.getSelectedItem().toString());
                                paramV.put("recipient", editTextMethodSendMoney.getText().toString());
                                paramV.put("anount", amount);
                                paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));

                                return paramV;
                            }
                        };
                        queue.add(stringRequest);
                    }else
                        Toast.makeText(getApplicationContext(), "You don't have that much money", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "The amount of money cannot be negative", Toast.LENGTH_SHORT).show();
            }

        });

        choiceCurrency();
        choiceMethodSend();

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String) parent.getItemAtPosition(position);

                if (item.equals("UA")) {
                    textViewAmountOfMoney.setText("" + sharedPreferences.getString("balanceUA", ""));
                    textViewSimbolBalance.setText(R.string.uaSymbol);
                }
                if (item.equals("US")) {
                    textViewAmountOfMoney.setText("" + sharedPreferences.getString("balanceUS", ""));
                    textViewSimbolBalance.setText(R.string.usSymbol);
                }
                if (item.equals("EU")) {
                    textViewAmountOfMoney.setText("" + sharedPreferences.getString("balanceEU", ""));
                    textViewSimbolBalance.setText(R.string.euSymbol);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinnerCurrencyСhoice.setOnItemSelectedListener(itemSelectedListener);

        imageViewBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void choiceCurrency() {
        ArrayAdapter<String> adapterCurrency = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currency);
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrencyСhoice.setAdapter(adapterCurrency);
    }

    private void choiceMethodSend() {
        ArrayAdapter<String> adapterMethodsSend = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, methodsSend);
        adapterMethodsSend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMethodsSendChoice.setAdapter(adapterMethodsSend);
    }

}
