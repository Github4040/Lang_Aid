package net.codess.langaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {
    TextView tvWelcome,tv;
    Button btn_play, btn_learn, button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        String name = getIntent().getStringExtra("player");
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tv = (TextView)findViewById(R.id.tv);
        //btn_play = (Button) findViewById(R.id.btn_play);
        tvWelcome.setText(name + ", Welcome to LANG AID!");
        btn_learn = (Button) findViewById(R.id.btn_learn);
        button = (Button) findViewById(R.id.button);

        btn_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity2.this, net.codess.langaid.result.class);
                startActivity(intent);
            }
        });

//        btn_play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(Activity2.this, net.codess.langaid.play.class);
//                startActivity(intent1);
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, net.codess.langaid.play.class);
                startActivity(intent);
            }
        });



    }



    }

