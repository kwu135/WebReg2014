package com.example.kevin.webreg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Kevin on 2/27/2015.
 */
public class AddSectionActivity extends ActionBarActivity {
    static String courseInfo = "http://petri.esd.usc.edu/socAPI/Courses/";
    static String term_num;
    static String school;
    static String dept;
    static String courseID;
    static String sectionID;
    static JSONArray jsonArray;
    public static final String MyCoursebinFile = "MyCoursebinFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsection);

        Intent i = getIntent();
        term_num = i.getStringExtra("term_num");
        school = i.getStringExtra("school");
        dept = i.getStringExtra("dept");
        courseID = i.getStringExtra("courseID");
        sectionID = i.getStringExtra("sectionID");
        SharedPreferences settings = getSharedPreferences(MyCoursebinFile, 0);
        SharedPreferences.Editor editor  = settings.edit();
        editor.putString(sectionID, "True");
        editor.commit();
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
