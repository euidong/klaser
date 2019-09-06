package com.khu.klaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.util.Log;

public class myPage extends AppCompatActivity {
    private static final String TAG ="myPage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        TextView usernameText = findViewById(R.id.username);
        Button logOutButton = findViewById(R.id.logOut_button);
        Button menuButton = findViewById(R.id.menu_button);

        final Intent my_intent = getIntent();
        usernameText.setText(my_intent.getStringExtra("username"));

        logOutButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        menuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Switch klasSwitch = findViewById(R.id.klasSwitch);
        EditText klasId = findViewById(R.id.klasId);
        EditText klasPw = findViewById(R.id.klasPw);
        Button submitButton = findViewById(R.id.submit_button);


        Button signOutButton = findViewById(R.id.signOut_button);

        signOutButton.setOnClickListener(new Button.OnClickListener(){
          @Override
          public void onClick(View view) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // All your networking logic
                    // should be here
                    HttpURLConnection notice_connection=null;
                    try {
                        URL signOut_api = new URL("http://ec2-13-124-5-212.ap-northeast-2.compute.amazonaws.com/sign_out_api/");
                        signOut_connection = (HttpURLConnection)signOut_api.openConnection();
                        signOut_connection.setRequestProperty("Content-Type","application/json");
                        signOut_connection.setRequestMethod("POST");
                        signOut_connection.connect();

                        int HttpResult = signOut_connection.getResponseCode();
                        if (HttpResult == HttpURLConnection.HTTP_OK){
                            BufferedReader br = new BufferedReader(new InputStreamReader(signOut_connection.getInputStream(), "utf-8"));
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
                                //log 띄우기
                            }

                        }
                        else {
                            String errorMessage = signOut_connection.getResponseMessage();
                            System.out.println(errorMessage);
                            //log 띄우기
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
                        if(signOut_connection!=null)
                            signOut_connection.disconnect();
                    }
                }
            });
          }
        })
    }
}
