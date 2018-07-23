package net.codess.langaid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class result extends AppCompatActivity {
    public static String stringname;
    private final String TAG = this.getClass().getName();
    private static final String uriBase_retrofit = "https://westcentralus.api.cognitive.microsoft.com/vision/v2.0/";
    static final int CAMERA_PIC_REQUEST = 2;
    File newImageFile;
    ImageView imageHolder;
    TextView tvMssg_capture,tv;
    Button btn_capture;
    Button upload;
    TextView result_api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        tvMssg_capture = (TextView) findViewById(R.id.tvMssg_capture);
        btn_capture = (Button) findViewById(R.id.btn_capture);
        tv = (TextView)findViewById(R.id.tv);
        result_api = findViewById(R.id.result_apiCall);
        upload = findViewById(R.id.uopload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File t = newImageFile;
                System.out.println("File which is about to get uploaded is : " + t.toString());
                testRetroFit(getApplicationContext(), t);

            }
        });
        imageHolder = findViewById(R.id.imageView);
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);*/
                captureImage();

            }
        });

    }
    public void captureImage() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "image.jpeg");
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(camera_intent, CAMERA_PIC_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            newImageFile = new File(Environment.getExternalStorageDirectory().toString());
            //Uri fileUri = data.getData();
            for (File temp : newImageFile.listFiles()) {
                if (temp.getName().equals("image.jpeg")) {
                    newImageFile = temp;
                    Bitmap thumbnail = BitmapFactory.decodeFile(newImageFile.getAbsolutePath());
                    imageHolder.setImageBitmap(thumbnail);
                }

            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
            return true;
        }
        return false;
    }

    public void write(String fname, String fcontent){
        try {

            //--System.out.println("Inside file creation function");


            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/LangAid" );
            String fpath =appDirectory+ File.separator+fname;
            File file = new File(fpath);
            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
                //--System.out.println("Creating a new file");
            }
            /*uploadFilePath=appDirectory.toString();
            //--System.out.println("uploadfile path is : "+uploadFilePath);*/
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fpath, true
                ))); //new FileWriter(fpath, true) will append the content
                out.println(fcontent);
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


            //--Log.d("Suceess", fcontent);
            //return true;

        } catch (IOException e) {
            e.printStackTrace();
            //return false;
        }

    }


    interface ServiceToCall {
        @Headers({
                "Content-Type: application/octet-stream",
                "Ocp-Apim-Subscription-Key: 3ff58b00f8cb4fa98006369a07c4b394"
        })
        @POST("describe/")
        Call<ResponseBody> postImage(@Body RequestBody body);
        //Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("description") RequestBody name);
    }

    public void testRetroFit(final Context context, File t) {

        //System.out.println("File to be uploaded is : "+filetoupload.toString());
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Change base URL to your upload server URL.
        ServiceToCall service = new Retrofit.Builder().baseUrl(uriBase_retrofit).client(client).build().create(ServiceToCall.class);

        File file = new File(String.valueOf(t));
        stringname = file.getAbsolutePath();
        System.out.println("Absolute path is : " + file.getAbsolutePath());
        RequestBody te = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(te);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                System.out.println("reaches here 1");
                try {
                    String abc = response.body().string();
                    System.out.println("response is : " + response.body().string());
                    System.out.println("Legth is : "+abc.length()+"and value is : "+abc);
                    JSONObject output = new JSONObject(abc);
                    JSONObject des = output.getJSONObject("description");
                    JSONArray cap = des.getJSONArray("captions");
                    JSONObject cap_description = cap.getJSONObject(0);
                    String cap_textres = cap_description.getString("text");
                    Double cap_conf = cap_description.getDouble("confidence");
                    result_api.setText("Result is : "+cap_textres);

                    write("words.txt",(cap_textres));



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("raw is : "+response.raw());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
