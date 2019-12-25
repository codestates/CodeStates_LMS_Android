package com.example.codestates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.codestates.CourseList.CourseViewModel;
import com.example.codestates.Dashboard.DashAdapter;
import com.example.codestates.Dashboard.DashViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class DashboardPage extends AppCompatActivity implements OkSingleton.UserDownloader,DashAdapter.OnDashListener {

    RecyclerView recyclerView;
    OkSingleton httpClient;
    DashAdapter adapter;
    ProgressBar progressBar;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_dashboard);

        httpClient = OkSingleton.getInstance();
        httpClient.setUserDownloader(this);

        recyclerView= findViewById(R.id.dashboard_list);
        progressBar = findViewById(R.id.dash_progress);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DashAdapter(this, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        name = getIntent().getStringExtra("name");
        httpClient.getUsers(name);
    }

    @Override
    public void usersDownloaded(JSONArray array) {
        LinkedList<DashViewModel> list = new LinkedList<>();
        JSONObject object;
        DashViewModel model;
        for(int i = 0; i < array.length();i++){
            try {
                object = array.getJSONObject(i);
                model = new DashViewModel();
                model.setEmail(object.getString("email"));
                model.setFirstname(object.getString("firstname"));
                model.setLastname(object.getString("lastname"));
                model.setInvitecode("Invite Code None");
                list.add(model);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter.setList(list);
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void inviteCodeReceived(JSONObject object, int position) {
        try {
            adapter.getList().get(position).setInvitecode(object.getString("invite_code"));
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserClick(int position) {
        //todo- send invitecode request
        String email = adapter.getList().get(position).getEmail();
        httpClient.getInviteCode(name, email,position);
    }
}
