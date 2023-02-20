package com.example.upmobank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class SendMoney extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Spinner spinnerCurrencyСhoice, spinnerMethodsSendChoice;
    TextView textViewAmountOfMoney, textViewSimbolBalance;

    ImageView imageViewBackToMain;


    EditText editTextMethodSendMoney;
    String[] currency = {"UA", "US", "EU"};

    String[] methodsSend = {"Phone", "Card"};

    String currencyItem, methodsSendItem;

    Button buttonSendMoney;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);


        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        spinnerCurrencyСhoice = findViewById(R.id.spinnerCurrencyСhoice);
        spinnerMethodsSendChoice = findViewById(R.id.spinnerMethodSendСhoice);
        buttonSendMoney = findViewById(R.id.buttonSendMoney);
        editTextMethodSendMoney = findViewById(R.id.editTextMethodSendMoney);
        textViewAmountOfMoney = findViewById(R.id.textViewAmountOfMoney);
        textViewSimbolBalance = findViewById(R.id.textViewSimbolBalance);
        imageViewBackToMain = findViewById(R.id.imageViewBackToMain);

        choiceCurrency();
        choiceMethodSend();


        buttonSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("-----------------------------------------------------" + spinnerCurrencyСhoice.getSelectedItem().toString());
//                System.out.println("-----------------------------------------------------" + spinnerMethodsSendChoice.getSelectedItem().toString());
            }
        });

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
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
                //editTextMethodSendMoney.setHint(item);
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
        ArrayAdapter<String> adapterMethodsSend = new ArrayAdapter(this, android.R.layout.simple_spinner_item, methodsSend);
        adapterMethodsSend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMethodsSendChoice.setAdapter(adapterMethodsSend);
    }
}
