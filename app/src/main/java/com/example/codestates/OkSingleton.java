package com.example.codestates;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.codestates.Constants.ACCEPT_INVITE;
import static com.example.codestates.Constants.CREATE_COURSE;
import static com.example.codestates.Constants.GET_INVITECODE;
import static com.example.codestates.Constants.GET_USERS;
import static com.example.codestates.Constants.JSON;
import static com.example.codestates.Constants.SIGNIN_URL;

public class OkSingleton extends OkHttpClient {

    //Application data
    private String email;
    private boolean admin;


    //Interfaces for callbacks
    private CourseDownloader mCourseDownloader;
    private SignInManager mSignInManager;
    private ContainerDownloader mContainerDownloader;
    private UserDownloader mUserDownloader;


    /*** Singleton class ***/
    private static class LazyHolder{
        private static final OkSingleton instance = new OkSingleton();
    }

    public static OkSingleton getInstance(){
        return LazyHolder.instance;
    }


    /*** Server Requests ***/
    public void acceptInvite(String inviteCode){
        JSONObject json = new JSONObject();
        try {
            json.put("invite_code",inviteCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(json.toString(),JSON);
        Request request = new Request.Builder()
                .url(ACCEPT_INVITE)
                .post(requestBody)
                .build();
        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody body = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseString = body.string();
                    Log.d("TAG", responseString);

                }
            }
        });
    }


    public void signIn(final String email, String password){
        JSONObject json = new JSONObject();
        try {
            json.put("email",email);
            json.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(json.toString(),JSON);
        Request request = new Request.Builder()
                .url(SIGNIN_URL)
                .post(requestBody)
                .build();

        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody body = response.body()){
                    if(!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseString = body.string();
                    Log.d("TAG",responseString);

                    //TODO: check login success
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        if(jsonObject.getBoolean("user_found")){

                            //update app data
                            OkSingleton.this.email = email;
                            int temp =  jsonObject.getInt("admin");
                            if (temp == 0) {
                                OkSingleton.this.admin = false;
                            }else{
                                OkSingleton.this.admin=true;
                            }

                            Handler mainHandler = new Handler(((Context)mSignInManager).getMainLooper());
                            mainHandler.post(new Runnable(){
                                @Override
                                public void run() {
                                    mSignInManager.loginSuccess();
                                }
                            });
                        }else{
                            Handler mainHandler = new Handler(((Context)mSignInManager).getMainLooper());
                            mainHandler.post(new Runnable(){
                                @Override
                                public void run() {
                                    mSignInManager.loginFailure();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void createCourse(String name, String description){
        JSONObject json = new JSONObject();
        try {
            json.put("course_name",name);
            json.put("course_description",description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(json.toString(),JSON);
        Request request = new Request.Builder()
                .url(CREATE_COURSE)
                .post(requestBody)
                .build();

        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody body = response.body()){
                    if(!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseString = body.string();
                    Log.d("TAG",responseString);

                    mCourseDownloader.courseCreated();
                }
            }
        });
    }

    public void getInviteCode(String name, String email,final int position){
        HttpUrl.Builder httpBuilder = HttpUrl.parse(GET_INVITECODE).newBuilder();
        httpBuilder.addQueryParameter("email",email);
        httpBuilder.addQueryParameter("course_name",name);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .build();

        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody body = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseString = body.string();
                    Log.d("TAG", responseString);

                    try {
                        final JSONObject object = new JSONObject(responseString);

                        Handler mainHandler = new Handler(((Context)mUserDownloader).getMainLooper());
                        mainHandler.post(new Runnable(){
                            @Override
                            public void run() {
                                mUserDownloader.inviteCodeReceived(object,position);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void getUsers(String name){

        HttpUrl.Builder httpBuilder = HttpUrl.parse(GET_USERS).newBuilder();
        httpBuilder.addQueryParameter("name",name);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .build();

        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody body = response.body()){
                    if(!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseString = body.string();
                    Log.d("TAG",responseString);
                    try {
                        final JSONArray array = new JSONArray(responseString);

                        Handler mainHandler = new Handler(((Context)mUserDownloader).getMainLooper());
                        mainHandler.post(new Runnable(){
                            @Override
                            public void run() {
                                mUserDownloader.usersDownloaded(array);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getContainersAndContents(String name){
        HttpUrl.Builder httpBuilder = HttpUrl.parse(Constants.GET_CONTAINERS_AND_CONTENTS).newBuilder();
        httpBuilder.addQueryParameter("name",name);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .build();

        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody body = response.body()){
                    if(!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseString = body.string();
                    Log.d("TAG",responseString);
                    try {
                        final JSONArray array = new JSONArray(responseString);

                        Handler mainHandler = new Handler(((Context)mContainerDownloader).getMainLooper());
                        mainHandler.post(new Runnable(){
                            @Override
                            public void run() {
                                mContainerDownloader.containersDownloaded(array);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getCourses(){
        HttpUrl.Builder httpBuilder = HttpUrl.parse(Constants.GET_COURSES).newBuilder();
        httpBuilder.addQueryParameter("email", email);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .build();

        newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try(ResponseBody body = response.body()){
                    if(!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseString = body.string();
                    Log.d("TAG",responseString);
                    try {
                        final JSONArray array = new JSONArray(responseString);

                        Handler mainHandler = new Handler(((Context)mCourseDownloader).getMainLooper());
                        mainHandler.post(new Runnable(){
                            @Override
                            public void run() {
                                mCourseDownloader.coursesDownloaded(array);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /*** Interfaces ***/
    public interface UserDownloader{
        void usersDownloaded(JSONArray array);
        void inviteCodeReceived(JSONObject object, int position);
    }
    public void setUserDownloader(UserDownloader userDownloader){
        mUserDownloader = userDownloader;
    }

    public interface ContainerDownloader{
        void containersDownloaded(JSONArray array);
    }
    public void setContainerDownloader(ContainerDownloader containerDownloader){
        mContainerDownloader = containerDownloader;
    }

    public interface CourseDownloader{
        void coursesDownloaded(JSONArray array);
        void courseCreated();
    }
    public void setCourseDownloader(CourseDownloader courseDownloader){
        mCourseDownloader = courseDownloader;
    }

    public interface SignInManager{
        void loginSuccess();
        void loginFailure();
    }
    public void setSignInManager(SignInManager signInManager){
        mSignInManager = signInManager;
    }

    /*** Application data(user info) ***/
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
