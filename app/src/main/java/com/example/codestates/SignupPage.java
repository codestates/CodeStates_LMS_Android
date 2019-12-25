package com.example.codestates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.codestates.Constants.JSON;
import static com.example.codestates.Constants.SIGNUP_URL;

public class SignupPage extends AppCompatActivity {

    OkSingleton httpClient;
    EditText phone;
    EditText email;
    EditText password;
    EditText firstname;
    EditText lastname;
    EditText admincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_signup);

        httpClient = OkSingleton.getInstance();

        phone = findViewById(R.id.phone_number);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        admincode = findViewById(R.id.admincode);

        Button signUp = findViewById(R.id.button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final JSONObject json = new JSONObject();
                try {
                    json.put("email",email.getText().toString());
                    json.put("phone",phone.getText().toString());
                    json.put("password",password.getText().toString());
                    json.put("firstname",firstname.getText().toString());
                    json.put("lastname",lastname.getText().toString());
                    if(admincode.getText().toString().isEmpty()){
                        json.put("admincode","");
                    }else{
                        json.put("admincode",admincode.getText().toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(json.toString(),JSON);
                Request request = new Request.Builder()
                        .url(SIGNUP_URL)
                        .post(requestBody)
                        .build();
                httpClient.setEmail(email.getText().toString());

                httpClient.newCall(request).enqueue(new Callback(){
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try(ResponseBody body = response.body()){
                            if(!response.isSuccessful()){
                                throw new IOException("Unexpected code " + response);
                            }
                            String responseString = body.string();
                            Log.d("TAG",responseString);

                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseString);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                OkSingleton.getInstance().setAdmin(jsonObject.getBoolean("admin"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Handler mainHandler = new Handler((SignupPage.this).getMainLooper());
                            mainHandler.post(new Runnable(){
                                @Override
                                public void run() {
                                    //Go to new screen
                                    Intent intent = new Intent(SignupPage.this, CoursesPage.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });


    }

}
