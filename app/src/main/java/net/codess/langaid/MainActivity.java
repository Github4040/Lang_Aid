package net.codess.langaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText etName;
    Button btn_submit,btn_play;
    TextView tvWelcome,tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        etName = (EditText) findViewById(R.id.etName);
        tv = (TextView)findViewById(R.id.tv);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        //btn_play = (Button) findViewById(R.id.btn_play);
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                Intent intent = new Intent(MainActivity.this, net.codess.langaid.Activity2.class);
                intent.putExtra("player" , name);
                startActivity(intent);
            }
        });
    }
}
