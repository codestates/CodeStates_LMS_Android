package com.example.codestates;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterInvitePage extends AppCompatActivity {

    EditText editText;
    Button button;
    OkSingleton httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_enterinvite);

        httpClient = OkSingleton.getInstance();

        editText=findViewById(R.id.enterinvite);
        button = findViewById(R.id.submitinvite);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty()){
                    //todo - accept invite
                    httpClient.acceptInvite(editText.getText().toString());
                }
            }
        });
    }
}
