package com.khu.klaser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        usernameText.setText(my_intent.getStringExtra("username")+ "님");

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

        Button pushOn_button = findViewById(R.id.pushOn_button);
        Button pushOff_button = findViewById(R.id.pushOff_button);

        pushOn_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // All your networking logic
                        // should be here
                        HttpURLConnection pushOn_connection=null;
                        try {
                            URL push_on_api = new URL("http://54.180.102.107/klaser/on/");
                            pushOn_connection = (HttpURLConnection)push_on_api.openConnection();
                            pushOn_connection.setRequestProperty("Content-Type","application/json");
                            pushOn_connection.setRequestMethod("POST");
                            pushOn_connection.connect();

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("token",FirebaseInstanceId.getInstance().getToken());
                            jsonObject.put("username",my_intent.getStringExtra("username"));
                            jsonObject.put("enable_push",true);
                            OutputStreamWriter out = new OutputStreamWriter(pushOn_connection.getOutputStream());
                            out.write(jsonObject.toString());
                            out.close();

                            int HttpResult = pushOn_connection.getResponseCode();
                            if (HttpResult == HttpURLConnection.HTTP_CREATED){
                                //log 띄우기
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
                            if(pushOn_connection!=null)
                                pushOn_connection.disconnect();
                        }
                    }
                });
            }
        });

        pushOff_button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // All your networking logic
                        // should be here
                        HttpURLConnection pushOff_connection=null;
                        try {
                            URL push_off_api = new URL("http://54.180.102.107/klaser/off/");
                            pushOff_connection = (HttpURLConnection)push_off_api.openConnection();
                            pushOff_connection.setRequestProperty("Content-Type","application/json");
                            pushOff_connection.setRequestMethod("POST");
                            pushOff_connection.connect();

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("username",my_intent.getStringExtra("username"));
                            OutputStreamWriter out = new OutputStreamWriter(pushOff_connection.getOutputStream());
                            out.write(jsonObject.toString());
                            out.close();

                            int HttpResult = pushOff_connection.getResponseCode();
                            if (HttpResult == HttpURLConnection.HTTP_NO_CONTENT){
                                //log 띄우기
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
                            if(pushOff_connection!=null)
                                pushOff_connection.disconnect();
                        }
                    }
                });
            }
        });

        Switch klasSwitch = findViewById(R.id.klasSwitch);
        final EditText klasId = findViewById(R.id.klasId);
        final EditText klasPw = findViewById(R.id.klasPw);
        final Button submitButton = findViewById(R.id.submit_button);

        submitButton.setEnabled(false);

        klasSwitch.setOnClickListener(new Button.OnClickListener(){
           @Override
            public void onClick(View view) {
               submitButton.setEnabled(!submitButton.isEnabled());
           }
        });

        submitButton.setOnClickListener(new Button.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                AsyncTask.execute(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // All your networking logic
                                                        // should be here
                                                        HttpURLConnection klas_connection = null;
                                                        try {
                                                            URL klas_api = new URL("http://54.180.102.107/get_klas_data/");
                                                            klas_connection = (HttpURLConnection) klas_api.openConnection();
                                                            klas_connection.setRequestProperty("Content-Type", "application/json");
                                                            klas_connection.setRequestMethod("POST");
                                                            klas_connection.connect();

                                                            JSONObject jsonObject = new JSONObject();
                                                            jsonObject.put("username", my_intent.getStringExtra("username"));
                                                            jsonObject.put("klas_id", klasId.getText().toString());
                                                            jsonObject.put("klas_pw", klasPw.getText().toString());
                                                            OutputStreamWriter out = new OutputStreamWriter(klas_connection.getOutputStream());
                                                            out.write(jsonObject.toString());
                                                            out.close();

                                                            int HttpResult = klas_connection.getResponseCode();
                                                            if (HttpResult == HttpURLConnection.HTTP_OK) {
                                                                //log 띄우기
                                                            } else {
                                                                //log 띄우기
                                                            }
                                                        } catch (MalformedURLException e) {
                                                            System.err.println("Malformed URL");
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            System.out.println(e.toString());
                                                        } catch (JSONException e) {
                                                            System.out.println(e.toString());
                                                        } finally {
                                                            if (klas_connection != null) {
                                                                klas_connection.disconnect();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        });

        Button signOutButton = findViewById(R.id.signOut_button);

        signOutButton.setOnClickListener(new Button.OnClickListener(){
          @Override
          public void onClick(View view) {
              AsyncTask.execute(new Runnable() {
                  @Override
                  public void run() {
                      // All your networking logic
                      // should be here
                      HttpURLConnection signOut_connection=null;
                      try {
                          URL sign_out_api = new URL("http://54.180.102.107/sign_out_api/");
                          signOut_connection = (HttpURLConnection)sign_out_api.openConnection();
                          signOut_connection.setRequestProperty("Content-Type","application/json");
                          signOut_connection.setRequestMethod("POST");
                          signOut_connection.connect();

                          JSONObject jsonObject = new JSONObject();
                          jsonObject.put("username",my_intent.getStringExtra("username"));
                          OutputStreamWriter out = new OutputStreamWriter(signOut_connection.getOutputStream());
                          out.write(jsonObject.toString());
                          out.close();

                          int HttpResult = signOut_connection.getResponseCode();
                          if (HttpResult == HttpURLConnection.HTTP_NO_CONTENT){
                              Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                              startActivity(intent);
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
                          if(signOut_connection!=null)
                              signOut_connection.disconnect();
                      }
                  }
              });
          }
        });
    }
}
