package net.codess.langaid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.squareup.picasso.Picasso;


import net.codess.langaid.volley.MyVolleySingelton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.Random;
import java.util.List;


public class API extends AppCompatActivity{
    PinView imageView;
    TextView tv;
    ListView lv;
    Button ans,next;
    private static final String subscriptionKey = "7e4db780020d4b61b23514f93704b0fc";

    private static final String uriBase ="https://api.cognitive.microsoft.com/bing/v7.0/images/search?maxFileSize=100000&q=";
    ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        iv2 = (ImageView)findViewById(R.id.iv2);
        tv = (TextView)findViewById(R.id.tv);
        lv = (ListView)findViewById(R.id.lv);
        ans = (Button)findViewById(R.id.ans);
        next = (Button)findViewById(R.id.next);


        int nolines = 0;
        int nolinesout = 0;
        String urlfrapi = "";

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard,"/LangAid/words.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();


        int randno = 0;

        try{
            BufferedReader brr = new BufferedReader(new FileReader(file));
            while ((brr.readLine()) != null) {
                nolines++;
                System.out.println("num lines "+nolines);
            }
            brr.close();

            if(nolines==0 || nolines==1){
                randno = 0;
            }
            else{
                Random r = new Random();
                randno = 1+r.nextInt(nolines);//errors out if nolines=0
                System.out.println("random num is:"+randno);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }


        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String[] array = new String[nolines+4];
            array[0]="table";
            array[1]="chair";
            array[2]="sofa";
            array[3]="flower pot";
            int rand = 0;
            Random r = new Random();
            if (nolines>4) {
                rand = r.nextInt(nolines - 4);
            }
            else{
                rand=r.nextInt(3);
            }
            while ((line = br.readLine()) != null) {
                List<StringBuilder>options = new ArrayList<StringBuilder>();
                for (int i=4; i<nolines;i++){
                    array[i] = line;
                    System.out.println("array ith val is: "+array[i]);
                }
                text.append(line);
                text.append('\n');
                nolinesout++;
                System.out.println("each line is: "+line);
                if (nolinesout==randno){
                    urlfrapi = line;
                    final String[] opt = new String[] {urlfrapi, array[rand], array[rand+1], array[rand+2] };
                    ArrayAdapter<String>adapter = new ArrayAdapter<String>(API.this,android.R.layout.simple_list_item_single_choice,opt);
                    lv.setAdapter(adapter);
                    ans.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(API.this, "Correct answer is " + opt[0], Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            br.close();

            if (nolines==0 || nolines==1){
                urlfrapi = "time to learn";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            urlfrapi = "time to learn";
        }
        String urlfrapin = urlfrapi.replace(" ", "+");
        String sendapilink = uriBase+urlfrapin;
        System.out.println("urlfinal is:"+ sendapilink);
        analyzeApiCall(getApplicationContext(), sendapilink);
    }


    private void analyzeApiCall(Context context, String s)
    {
        System.out.println("inside api call");
        StringRequest request = new StringRequest(Request.Method.GET, s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Codess","Ack");
                Log.d("Codess",response);
                try {
                    JSONObject r=new JSONObject(response);
                    JSONArray rar = new JSONArray();
                    rar = (JSONArray) r.get("value");
                    JSONObject rarobj = (JSONObject) rar.get(0);

                    String imgurl = (String) rarobj.get("contentUrl");
                    System.out.println("Bing Api Result is: "+r.toString());
                    System.out.println("image value is: "+imgurl);

                    Picasso.get().load(imgurl).resize(250,250).into(iv2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Response", "Error: " +error.toString());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                //headers.put("Content-Type", "application/octet-stream");
                headers.put("Ocp-Apim-Subscription-Key", subscriptionKey);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyVolleySingelton.getInstance(context).addToRequestQueue(request);
    }

}