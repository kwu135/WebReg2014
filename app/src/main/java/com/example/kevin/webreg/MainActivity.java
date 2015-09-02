package com.example.kevin.webreg;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn_term_20143 = (Button) findViewById(R.id.btn_term_20143);
        Button btn_term_20151 = (Button) findViewById(R.id.btn_term_20151);
        Button btn_term_20152 = (Button) findViewById(R.id.btn_term_20152);

        //Listening to button event
        btn_term_20143.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), SchoolActivity.class);
                nextScreen.putExtra("term_num", "20143");
                startActivity(nextScreen);

            }
        });

        btn_term_20151.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), SchoolActivity.class);
                nextScreen.putExtra("term_num", "20151");
                startActivity(nextScreen);

            }
        });

        btn_term_20152.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), SchoolActivity.class);
                nextScreen.putExtra("term_num", "20152");
                startActivity(nextScreen);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.courses:
                Log.v("TEST", "COURSES");
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);
                return true;
            case R.id.coursebin:
                Log.v("TEST", "COURSEBIN");
                Intent nextScreen2 = new Intent(getApplicationContext(), CourseBinActivity.class);
                startActivity(nextScreen2);
                return true;
            case R.id.register:
                Log.v("TEST", "REGISTER");
                Intent nextScreen3 = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(nextScreen3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
