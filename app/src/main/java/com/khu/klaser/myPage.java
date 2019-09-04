package com.khu.klaser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class myPage extends AppCompatActivity {

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


    }
}
