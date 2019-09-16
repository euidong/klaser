package com.khu.klaser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class signUp extends AppCompatActivity {
    private static final String TAG ="signUp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText userName = findViewById(R.id.username_input);
        final EditText email = findViewById(R.id.email_input);
        final EditText password = findViewById(R.id.password_input);
        final EditText firstName = findViewById(R.id.firstName_input);
        final EditText lastName = findViewById(R.id.lastName_input);

        Button back = findViewById(R.id.backToSignIn);
        Button signUp = findViewById(R.id.signUp_button);

        back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        signUp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // All your networking logic
                        // should be here
                        StringBuilder sb = new StringBuilder();
                        HttpURLConnection signUp_connection=null;
                        try {
                            URL sign_up_api = new URL("http://54.180.102.107/sign_up_api/");
                            signUp_connection = (HttpURLConnection)sign_up_api.openConnection();
                            signUp_connection.setRequestProperty("Content-Type","application/json");
                            signUp_connection.setRequestMethod("POST");
                            signUp_connection.connect();

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("username",userName.getText().toString());
                            jsonObject.put("email",email.getText().toString());
                            jsonObject.put("password",password.getText().toString());
                            jsonObject.put("first_name",firstName.getText().toString());
                            jsonObject.put("last_name",lastName.getText().toString());
                            OutputStreamWriter out = new OutputStreamWriter(signUp_connection.getOutputStream());
                            out.write(jsonObject.toString());
                            out.close();

                            int HttpResult = signUp_connection.getResponseCode();
                            if (HttpResult == HttpURLConnection.HTTP_CREATED){
                                finish();
                            }
                            else {
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
                        catch (JSONException e) {
                            System.out.println (e.toString());
                        }
                        finally{
                            if(signUp_connection!=null)
                                signUp_connection.disconnect();
                        }
                    }
                });
            }
        });
    }


}
