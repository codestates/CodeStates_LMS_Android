package com.example.codestates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SigninPage extends AppCompatActivity implements OkSingleton.SignInManager {

    OkSingleton httpClient;
    EditText email;
    EditText password;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_signin);

        httpClient = OkSingleton.getInstance();
        httpClient.setSignInManager(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button = findViewById(R.id.signin);

        ((TextView)findViewById(R.id.sign_up_now)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninPage.this, SignupPage.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                httpClient.signIn(email.getText().toString(),password.getText().toString());
            }
        });
    }

    @Override
    public void loginSuccess() {
        //got to new screen
        Intent intent = new Intent(SigninPage.this, CoursesPage.class);
        startActivity(intent);
    }

    @Override
    public void loginFailure() {
        Toast.makeText(this, "Wrong email or password", Toast.LENGTH_LONG).show();
    }
}
