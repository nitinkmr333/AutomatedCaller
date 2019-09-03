package com.nc.rockstar.caller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.InputStream;

import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class phonenoActivity extends AppCompatActivity {




    private static final int REQUEST_CALL = 1;
    private EditText mEditTextNumber;
    String storedData="";


    //for csv file
    InputStream inputStream;
    String[] data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneno);

        //to read filePath sent by previous activity
        final String file1 = getIntent().getExtras().getString("keyName","defaultKey");       //get excel file path

        File file = new File(file1);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        int flag=0;
        try{
            String csvLine;
            while((csvLine = reader.readLine()) != null) {
                data = csvLine.split(",");
                    try {
                        if(flag==0){
                            storedData=data[0];
                            flag=1;
                        }
                        else{
                        storedData = storedData + "\n" + data[0];        //reading only column 1 of excel file
                    }} catch (Exception e) {                       //exception handling
                        Log.e("Problem ", e.toString());
                    }
                }
        }
        catch (IOException ex){
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        Log.e("Data", "" + storedData);
        TextView tv = findViewById(R.id.textView);
        tv.setText(storedData);
        tv.setMovementMethod(new ScrollingMovementMethod());



    Button btn2 = (Button) findViewById(R.id.button2);
    btn2.setOnClickListener(new View.OnClickListener() {
        public void onClick (View v){
            TextView tv = (TextView) findViewById(R.id.textView);

            if (tv.getText().toString().matches(".*[a-zA-Z]+.*")) {     //to check it does not have any alphabets
                Toast.makeText(phonenoActivity.this, "Excel file must not have any alphabets", Toast.LENGTH_SHORT).show();
            }
            else if(tv.getText().toString().matches("")){
                Toast.makeText(phonenoActivity.this, "Excel file should contain at least one phone number", Toast.LENGTH_SHORT).show();
            }
            else{
            Intent intent = new Intent(phonenoActivity.this, MainActivity.class);
            intent.putExtra("keyName", file1);  // pass your values and retrieve them in the other Activity using keyName
            startActivity(intent);
        }

        }
    });

    }
}
