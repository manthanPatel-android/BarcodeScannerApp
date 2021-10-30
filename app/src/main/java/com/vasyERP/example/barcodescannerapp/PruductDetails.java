package com.vasyERP.example.barcodescannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PruductDetails extends AppCompatActivity {

    TextView name,qty,price;
    Button checkout;
    String nametext="",qtytext="",pricetext="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruduct_details);

        Intent intent= getIntent();
        if (intent!=null){
             nametext = intent.getStringExtra("name");
             qtytext = intent.getStringExtra("qty");
             pricetext = intent.getStringExtra("price");
        }
        name =findViewById(R.id.idname);
        qty =findViewById(R.id.idqty);
        price =findViewById(R.id.idPrice);

        name.setText(nametext);
        qty.setText(qtytext);
        price.setText(pricetext);

        checkout = findViewById(R.id.idcheckot);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}