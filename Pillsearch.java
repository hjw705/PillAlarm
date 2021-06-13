package com.SE.team12.pillalarm2021;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Pillsearch extends AppCompatActivity {


    TextView textView;
    String data;
    Button returnbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pillsearch);
        returnbtn=findViewById(R.id.button);
        textView=findViewById(R.id.textView);

        data=getIntent().getStringExtra("data");
        Log.i("check",data);
        textView.setText(data);
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Pillsearch.this, MainActivity.class);
                startActivity(intent);

            }});

//        OpenAPI dust = new OpenAPI(url);
//        dust.execute();

    }}
// 버튼을 클릭했을 때 쓰레드를 생성하여 해당 함수를 실행하여 텍스트뷰에 데이터 출력





