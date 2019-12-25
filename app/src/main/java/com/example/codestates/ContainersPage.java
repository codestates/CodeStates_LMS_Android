package com.example.codestates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.codestates.ContentList.ContainerAdapter;
import com.example.codestates.ContentList.ContainerViewModel;
import com.example.codestates.ContentList.ContentViewModel;
import com.example.codestates.CourseList.CourseViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class ContainersPage extends AppCompatActivity implements OkSingleton.ContainerDownloader{

    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button button;
    TextView name;
    TextView desc;
    ImageView dashboard;

    ContainerAdapter adapter;
    OkSingleton httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_containers);

        httpClient = OkSingleton.getInstance();
        httpClient.setContainerDownloader(this);

        recyclerView = findViewById(R.id.containers_list);
        button = findViewById(R.id.new_container_button);
        progressBar = findViewById(R.id.containers_progress);
        name = findViewById(R.id.course_name);
        desc = findViewById(R.id.course_description);

        name.setText(getIntent().getStringExtra("name"));
        desc.setText(getIntent().getStringExtra("description"));

        dashboard = findViewById(R.id.invite_user);
        dashboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContainersPage.this, DashboardPage.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //todo: Create CustomDialog for making container
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContainerAdapter(this,new LinkedList<ContainerViewModel>());//empty list initially
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(httpClient.isAdmin()){
            dashboard.setVisibility(View.VISIBLE);
            dashboard.setClickable(true);

            button.setVisibility(View.VISIBLE);
            button.setClickable(true);
        }else{
            dashboard.setVisibility(View.INVISIBLE);
            dashboard.setClickable(false);

            button.setVisibility(View.INVISIBLE);
            button.setClickable(false);
        }

        String name = getIntent().getStringExtra("name");
        httpClient.getContainersAndContents(name);
    }

    @Override
    public void containersDownloaded(JSONArray array) {

        //container <-> list of contents
        HashMap<String, LinkedList<ContentViewModel>> map = new HashMap<>();

        JSONObject object;
        for(int i = 0; i < array.length();i++){
            try {
                object = array.getJSONObject(i);

                //Initialize new container
                if(!map.containsKey(object.getString("container_name"))) {
                    LinkedList<ContentViewModel> list = new LinkedList<>();
                    map.put(object.getString("container_name"),list);
                }

                if(!object.getString("name").equals("null")){
                    ContentViewModel model = new ContentViewModel(object.getString("name"),object.getString("text"));
                    map.get(object.getString("container_name")).add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LinkedList<ContainerViewModel> list = new LinkedList<ContainerViewModel>();
        for(String containerName: map.keySet()){
            LinkedList<ContentViewModel> contentList = map.get(containerName);
            ContainerViewModel containerViewModel = new ContainerViewModel(containerName, contentList);
            switch((int) Math.floor(Math.random() * 4)){
                case 0:
                    containerViewModel.setImg(R.drawable.books);
                    break;
                case 1:
                    containerViewModel.setImg(R.drawable.check_mark);
                    break;
                case 2:
                    containerViewModel.setImg(R.drawable.edit);
                    break;
                case 3:
                    containerViewModel.setImg(R.drawable.smiling);
                    break;
            }
            list.add(containerViewModel);
        }

        //Notify all changes
        adapter.setParentList(list, true);
        for(int i =0; i < list.size();i++){
            adapter.notifyParentChanged(i);
            for(int j = 0; j < list.get(i).getChildList().size();j++){
                adapter.notifyChildChanged(i,j);
            }
        }

        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
