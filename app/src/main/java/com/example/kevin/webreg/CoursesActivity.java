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
public class CoursesActivity extends ActionBarActivity {
    static String courseInfo = "http://petri.esd.usc.edu/socAPI/Courses/";
    static String term_num;
    static String school;
    static String dept;
    static JSONArray jsonArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses);

        Intent i = getIntent();
        term_num = i.getStringExtra("term_num");
        school = i.getStringExtra("school");
        dept = i.getStringExtra("dept");
        courseInfo = "http://petri.esd.usc.edu/socAPI/Courses/";
        courseInfo += term_num + "/";
        courseInfo += dept;
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
            return result;
        }

        protected void onPostExecute(String result) {
            LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,6,0,6);
            Context context=getApplicationContext();
            try {
                jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                Log.v("length", "" + length);
                for (int i=0;i<length;i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Button myButton = new Button(context);
                    String course_id = jsonObject.getString("SIS_COURSE_ID");
                    String course_title = jsonObject.getString("TITLE");
                    //Log.v("school_des", school_des);
                    myButton.setText(course_id + ": " + course_title);
                    myButton.setBackgroundColor(Color.LTGRAY);
                    myButton.setTextColor(Color.BLACK);
                    myButton.setId(i);
                    ll.addView(myButton, lp);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View arg0) {
                            //Starting a new Intent
                            Intent nextScreen = new Intent(getApplicationContext(), CourseDetailActivity.class);
                            nextScreen.putExtra("term_num", term_num);
                            nextScreen.putExtra("school", school);
                            nextScreen.putExtra("dept", dept);
                            try {
                                JSONObject tempJSONObject = jsonArray.getJSONObject(arg0.getId());
                                String tempCourseIDCode = tempJSONObject.getString("COURSE_ID");
                                nextScreen.putExtra("courseID", tempCourseIDCode);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Log.v("ID", "" + arg0.getId());
                            startActivity(nextScreen);
                        }
                    });
                }
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
