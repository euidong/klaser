package com.khu.klaser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class menu extends AppCompatActivity {
    private static final String TAG ="menu";
    private void get_notice(final String url_address,final TextView noticeText) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // All your networking logic
                // should be here
                HttpURLConnection notice_connection=null;
                try {
                    URL notice_api = new URL(url_address);
                    notice_connection = (HttpURLConnection)notice_api.openConnection();
                    notice_connection.setRequestProperty("Content-Type","application/json");
                    notice_connection.setRequestMethod("GET");
                    notice_connection.connect();

                    int HttpResult = notice_connection.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(notice_connection.getInputStream(), "utf-8"));
                        String line = br.readLine();
                        br.close();
                        try {
                            StringBuffer total_notice = new StringBuffer();
                            int id =0;
                            String name = "";
                            String date = "";
                            String url ="";
                            JSONArray resultAry = new JSONArray(line);
                            for (int i = 0; i < resultAry.length() ; i++) {
                                JSONObject result = resultAry.getJSONObject(i);
                                id = result.getInt("id");
                                name = result.getString("name");
                                date = result.getString("date");
                                url = result.getString("url");
                                System.out.println(result.toString());
                                total_notice.append(id + "\n" + name + "\n" + date + "\n" + url);
                            }
                            noticeText.setText(total_notice.toString());
                        }catch (JSONException e) {

                        }

                    }
                    else {
                        String errorMessage = notice_connection.getResponseMessage();
                        System.out.println(errorMessage);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
                catch (MalformedURLException e) {
                    System.err.println("Malformed URL");
                    e.printStackTrace();
                }
                catch (IOException e) {
                    System.out.println (e.toString());
                }
                finally{
                    if(notice_connection!=null)
                        notice_connection.disconnect();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final TextView ce_noticeText = findViewById(R.id.ce_notice);
        final TextView sw_noticeText = findViewById(R.id.sw_notice);

        String ce_address = "http://ec2-52-79-239-236.ap-northeast-2.compute.amazonaws.com/notice/ce_api/";
        String sw_address = "http://ec2-52-79-239-236.ap-northeast-2.compute.amazonaws.com/notice/sw_api/";
        get_notice(ce_address, ce_noticeText);
        get_notice(sw_address, sw_noticeText);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();


                    }
                });



        TextView usernameText = findViewById(R.id.username);
        Button logOutButton = findViewById(R.id.logOut);
        Button myPageButton = findViewById(R.id.myPage);

        final Intent my_intent =getIntent();
        usernameText.setText(my_intent.getStringExtra("username") + "님 안녕하세요");

        logOutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        myPageButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), myPage.class);
                intent.putExtra("username", my_intent.getStringExtra("username"));
                startActivity(intent);
            }
        });

    }
}
