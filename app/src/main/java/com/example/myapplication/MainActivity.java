package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        Button nextBtn = (Button)findViewById(R.id.nextBtn);
        TextView textView = (TextView)findViewById(R.id.txtTitle);

        //textView.setText("Select Encryption Level");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId != -1){
                    RadioButton radioButton = (RadioButton)findViewById(checkedId);

                    if(radioButton != null){
                        choice = radioButton.getText().toString(); //암호화 강도 저장 = high/medium/low
                    }
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(choice.equals("전체암호")) {
                    Intent i = new Intent(MainActivity.this, highActivity.class);
                    //i.putExtra("level", choice); //level 전달
                    startActivity(i); //인텐트 전달
                }

                else if(choice.equals("선택암호")){
                    Intent i = new Intent(MainActivity.this, mediumActivity.class);
                    //i.putExtra("level", choice); //level 전달
                    startActivity(i); //인텐트 전달
                }
                else if(choice.equals("암호화하지 않음")){
                    Intent i = new Intent(MainActivity.this, lowActivity.class);
                    //i.putExtra("level", choice);
                    startActivity(i);
                }

            }
        });
    }
}
