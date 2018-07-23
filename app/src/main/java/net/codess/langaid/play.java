package net.codess.langaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class play extends AppCompatActivity {
    TextView tvPlay,tv;
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        tvPlay = (TextView)findViewById(R.id.tvPlay);
        tv = (TextView)findViewById(R.id.tv);
        btnOK = (Button)findViewById(R.id.btnOK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(play.this, net.codess.langaid.API.class);
                startActivity(intent);
            }
        });
    }
}
