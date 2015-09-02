package com.example.kevin.webreg;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

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
 * Created by Kevin on 2/25/2015.
 */
public class CourseDetailActivity extends ActionBarActivity {

    static String courseInfo = "http://petri.esd.usc.edu/socAPI/Courses/";
    static String term_num;
    static String school;
    static String dept;
    static String courseID;
    static JSONArray jsonArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursedetail);

        Button buttonCoursebin = (Button) findViewById(R.id.buttonCoursebin);
        buttonCoursebin.setVisibility(View.GONE);
        Intent i = getIntent();
        term_num = i.getStringExtra("term_num");
        school = i.getStringExtra("school");
        dept = i.getStringExtra("dept");
        courseID = i.getStringExtra("courseID");
        courseInfo = "http://petri.esd.usc.edu/socAPI/Courses/";
        courseInfo += term_num + "/";
        courseInfo += courseID;
        Log.v("url", courseInfo);
        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String...arg0) {
            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost(courseInfo);
            httpPost.setHeader("Content-type", "application/json");
            InputStream inputStream = null;
            String result = null;
            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null) {
                    stringBuilder.append(line+"\n");
                }
                result = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                }
            }
            Log.v("TEST", "TEST33");
            return result;
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                TextView sis_course_id = (TextView)findViewById(R.id.SIS_COURSE_ID);
                TextView title = (TextView)findViewById(R.id.TITLE);
                TextView description = (TextView)findViewById(R.id.DESCRIPTION);
                TextView min_units = (TextView)findViewById(R.id.MIN_UNITS);
                TextView max_units = (TextView)findViewById(R.id.MAX_UNITS);
                TextView diversity_flag = (TextView)findViewById(R.id.DIVERSITY_FLAG);
                sis_course_id.setText(jsonObject.getString("SIS_COURSE_ID"));
                title.setText(jsonObject.getString("TITLE"));
                description.setText(jsonObject.getString("DESCRIPTION"));
                min_units.setText("Min Units: " + jsonObject.getString("MIN_UNITS"));
                max_units.setText("Max Units: " + jsonObject.getString("MAX_UNITS"));
                diversity_flag.setText("Diversity Flag: " + jsonObject.getString("DIVERSITY_FLAG"));
                Button buttonCoursebin = (Button) findViewById(R.id.buttonCoursebin);
                buttonCoursebin.setVisibility(View.VISIBLE);
                buttonCoursebin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        //Starting a new Intent
                        Intent nextScreen = new Intent(getApplicationContext(), SectionActivity.class);
                        nextScreen.putExtra("term_num", term_num);
                        nextScreen.putExtra("school", school);
                        nextScreen.putExtra("dept", dept);
                        nextScreen.putExtra("courseID", courseID);

                        //Log.v("ID", "" + arg0.getId());
                        startActivity(nextScreen);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
