package com.nc.rockstar.caller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import java.util.regex.Pattern;

public class initialActivity extends AppCompatActivity {

    public String filePath1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);



        //material file picker code

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},99);
        }

        //to open next activity by "NEXT" button
        Button btn = (Button) findViewById(R.id.button);
        final TextView tv6 = (TextView) findViewById(R.id.textView6);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){

                if(tv6.getText().toString()=="") {
                    Toast.makeText(getApplicationContext(), "Please upload the excel file", Toast.LENGTH_SHORT).show();
                }
                else {
                    //to send filepath to next activity
                    Intent intent = new Intent(initialActivity.this, phonenoActivity.class);

                    TextView tv6 = (TextView) findViewById(R.id.textView6);
                    String file1 = tv6.getText().toString();
                    intent.putExtra("keyName", file1);  // pass your values and retrieve them in the other Activity using keyName
                    startActivity(intent);
                }


            }
        });


        Button btn5 = (Button) findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){

                new MaterialFilePicker()
                        .withActivity(initialActivity.this)
                        .withRequestCode(1)

                        .withFilter(Pattern.compile(".*\\.(csv$|xlsx$|xls$|xlsm$)")) // Filtering files and directories by file name using regexp
                        //.withFilterDirectories(true) // Set directories filterable (false by default)
                        .withHiddenFiles(false) // Show hidden files and folders
                        .start();


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath1 = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            TextView tv6 = (TextView) findViewById(R.id.textView6);
            tv6.setText(filePath1);
            TextView tv10 = (TextView) findViewById(R.id.textView10);
            tv10.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Media Permission granted!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"Media Permission not granted!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}