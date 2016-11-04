package com.example.viknox.giusharemate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by x230 on 11/3/2016.
 */

public class ActivityOverview extends AppCompatActivity implements FragmentComms, View.OnClickListener{
    LinearLayout nav_pane;
    ImageButton btn_search;
    ImageButton btn_groups;
    ImageButton btn_chats;
    ImageButton btn_settings;
    FragmentManager manager = getFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    SettingsFrg frg = new SettingsFrg();
    ChatFrg chfrg = new ChatFrg();
    GrouFrg grpfrg = new GrouFrg();
    SearchFrg srchfrg = new SearchFrg();
    HomeFrg hmfrg = new HomeFrg();
    String userId;
    public void switchFragment(final Fragment fragment, final String tag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fragment != null) {
                    manager.beginTransaction().replace(R.id.frg_holder,
                            fragment, tag).addToBackStack(null).commitAllowingStateLoss();

                }
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Intent i = getIntent();
        String pr_name = i.getStringExtra("name");
        Integer pr_age = i.getIntExtra("age",0);
        String pr_email = i.getStringExtra("email");
        String UserId = i.getStringExtra("userID");


        initUI();
        transaction.add(R.id.frg_holder,hmfrg,"STNGFRG");
        transaction.commit();

    }



    private void initUI() {
        nav_pane = (LinearLayout) findViewById(R.id.nav_bar);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_chats = (ImageButton) findViewById(R.id.btn_chat);
        btn_settings = (ImageButton) findViewById(R.id.btn_settings);
        btn_groups = (ImageButton) findViewById(R.id.btn_groups);
        Log.v("OverviewActivity", "UI Initiated");


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void respond(String data1) {

        switch(data1){
            case "Chat":
                switchFragment(chfrg,"CHTFRG");
                break;
            case "Group":
                switchFragment(grpfrg,"GRPFRG");
                break;
            case "Search":
                switchFragment(srchfrg,"SRCHFRG");
                break;
            case "Settings":
                switchFragment(frg, "STNGFRG");
                break;
        }


    }
}
