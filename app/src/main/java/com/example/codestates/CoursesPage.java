package com.example.codestates;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.codestates.CourseList.CourseAdapter;
import com.example.codestates.CourseList.CourseViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class CoursesPage extends AppCompatActivity implements OkSingleton.CourseDownloader, CourseAdapter.OnCourseListener{

    OkSingleton httpClient;
    CourseAdapter adapter;

    RecyclerView mRecylcerView;
    ProgressBar mProgressBar;
    Button mButton;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_courses);

        httpClient = OkSingleton.getInstance();
        httpClient.setCourseDownloader(this);

        mButton = findViewById(R.id.new_course_button);
        mProgressBar = findViewById(R.id.courses_progress);
        mRecylcerView = findViewById(R.id.courses_list);
        imageView = findViewById(R.id.user_dashboard);



        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoursesPage.this, EnterInvitePage.class);
                startActivity(intent);
            }
        });


        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CoursesPage.this);
                View layout = getLayoutInflater().inflate(R.layout.dialog_create_course,null);
                final EditText name = layout.findViewById(R.id.make_course_name);
                final EditText description = layout.findViewById(R.id.make_course_description);
                final Dialog dialog = builder.setView(layout).create();

                Button button = layout.findViewById(R.id.make_course_button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!name.getText().toString().isEmpty() && !description.getText().toString().isEmpty()){
                            httpClient.createCourse(name.getText().toString(),description.getText().toString());
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        mRecylcerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CourseAdapter(this,this);
        mRecylcerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!httpClient.isAdmin()){
            mButton.setVisibility(View.INVISIBLE);
            mButton.setClickable(false);

            imageView.setVisibility(View.VISIBLE);
            imageView.setClickable(true);
        }else{
            mButton.setVisibility(View.VISIBLE);
            mButton.setClickable(true);

            imageView.setVisibility(View.INVISIBLE);
            imageView.setClickable(false);
        }

        httpClient.getCourses();
    }


    @Override
    public void coursesDownloaded(JSONArray array) {

        LinkedList<CourseViewModel> list = new LinkedList<>();

        //change adapter items
        JSONObject object;
        CourseViewModel model;
        for(int i = 0; i < array.length();i++){
            try {
                object = array.getJSONObject(i);
                model = new CourseViewModel();
                model.setTitle(object.getString("name"));
                model.setDescription(object.getString("description"));
                switch(i%3){
                    case 0:
                        model.setImg(R.drawable.atom);
                        break;
                    case 1:
                        model.setImg(R.drawable.robot);
                        break;
                    case 2:
                        model.setImg(R.drawable.tube);
                        break;
                }
                list.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter.setList(list);
        adapter.notifyDataSetChanged();

        mRecylcerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void courseCreated() {
        httpClient.getCourses();
    }

    @Override
    public void onCourseClick(int position) {
        CourseViewModel model = adapter.getList().get(position);
        Intent intent = new Intent(CoursesPage.this, ContainersPage.class);
        intent.putExtra("name",model.getTitle());
        intent.putExtra("description",model.getDescription());
        startActivity(intent);
    }
}