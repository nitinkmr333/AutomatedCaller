package com.nc.rockstar.caller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CALL = 1;
    String storedData = "";

    //for csv file
    InputStream inputStream;
    String[] data;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = (ImageView) findViewById(R.id.imageView);
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.4F);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);      //imageview click animation


                String file1 = getIntent().getExtras().getString("keyName", "defaultKey");       //get excel file path

                SharedPreferences sharedPref = getSharedPreferences("fileinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("file1", file1);
                editor.apply();
                //end of shared preferences

                Log.e("excel path:", file1);
                File file = new File(file1);
                FileInputStream inputStream = null;


                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(MainActivity.TELEPHONY_SERVICE);
                PhoneStateListener phoneStateListener = new PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        String number = incomingNumber;
                        if (state == TelephonyManager.CALL_STATE_OFFHOOK) {     //2
                            Log.d("DEBUG", "OFFHOOK");
                        }
                        if (state == TelephonyManager.CALL_STATE_IDLE) {    //0
                            Log.d("DEBUG", "IDLE");
                        }
                        if (state == TelephonyManager.CALL_STATE_RINGING) {        //1
                            Log.d("DEBUG", "RINGING");
                        }
                    }
                };


                //call numbers one by one
                try {
                    String csvLine;
                    while ((csvLine = reader.readLine()) != null) {


                        data = csvLine.split(",");
                        try {
                            Thread.sleep(1000);
                            storedData = data[0];
                            Log.e("call status", "STARTING CALL");
                            makePhoneCall(storedData);                 //calling one by one
                            Thread.sleep(2000);
                            Log.e("Message", "This message is after phone call");

                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                Log.e("PLAYER ERROR IN MAIN", "Player cannot start (maybe already started)");
                            }


                            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
                            while (true) {
                                if (telephonyManager.getCallState() == 2) {
                                    Log.e("OFFHOOK DATA", "Still in offhook");

                                } else {Thread.sleep(1000);
                                    break;
                                }
                            }

                            Log.e("Data", "" + data[0]);    //to show excel data in Logcat
                        } catch (Exception e) {                       //exception handling
                            Log.e("Problem", e.toString());
                        }


                    }
                } catch (IOException ex) {
                    throw new RuntimeException("Error in reading CSV file: " + ex);
                }
              }
        });
    }


    private void makePhoneCall(String phoneno) {
        String number = phoneno;
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Calling Permission was Denied!\nPlease restart app.", Toast.LENGTH_SHORT).show();
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(storedData);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}