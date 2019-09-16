package com.khu.klaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText email = findViewById(R.id.email_input);
        final EditText password = findViewById(R.id.password_input);
        Button signIn_button = findViewById(R.id.signIn_button);
        final Button signUp_button = findViewById(R.id.signUp_button);

        signUp_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signUp.class);
                startActivity(intent);
            }
        });

        signIn_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // All your networking logic
                        // should be here
                        StringBuilder sb = new StringBuilder();
                        HttpURLConnection signIn_connection=null;
                        try {
                            URL sign_in_api = new URL("http://54.180.102.107/sign_in_api/");
                            signIn_connection = (HttpURLConnection)sign_in_api.openConnection();
                            signIn_connection.setRequestProperty("Content-Type","application/json");
                            signIn_connection.setRequestMethod("POST");
                            signIn_connection.connect();

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("email",email.getText().toString());
                            jsonObject.put("password",password.getText().toString());
                            OutputStreamWriter out = new OutputStreamWriter(signIn_connection.getOutputStream());
                            out.write(jsonObject.toString());
                            out.close();

                            int HttpResult = signIn_connection.getResponseCode();
                            if (HttpResult == HttpURLConnection.HTTP_OK){
                                BufferedReader br = new BufferedReader(new InputStreamReader(signIn_connection.getInputStream(), "utf-8"));
                                String line = null;
                                while((line =br.readLine()) != null) {
                                    sb.append(line + "\n");
                                }
                                br.close();
                                String result = sb.toString();
                                System.out.println(result);
                                if (result.contains("error")) {
                                    //log 띄우기
                                }
                                else {
                                    JSONObject js = new JSONObject(result);

                                    Intent intent = new Intent(getApplicationContext(), menu.class);
                                    intent.putExtra("username",js.getString("username"));
                                    startActivity(intent);
                                }
                            }
                            else {
                                String errorMessage = signIn_connection.getResponseMessage();
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
                        catch (JSONException e) {
                            System.out.println (e.toString());
                        }
                        finally{
                            if(signIn_connection!=null)
                                signIn_connection.disconnect();
                        }
                    }
                });
            }
        });

    }
}
