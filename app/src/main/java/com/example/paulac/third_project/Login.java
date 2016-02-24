package com.example.paulac.third_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText Username, Password;
    private Button login;
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this);


    }

    public void signup(View v){
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signup:

                startActivity(new Intent(Login.this, SignUp.class));

                break;
        }
    }
}