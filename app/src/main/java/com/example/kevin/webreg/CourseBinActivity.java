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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 2/27/2015.
 */
public class CourseBinActivity extends ActionBarActivity{
    public static final String MyCoursebinFile = "MyCoursebinFile4";

    static String sectionInfo = "http://petri.esd.usc.edu/socAPI/Sections/";
    static Boolean sync = true;
    static ArrayList sectionInfoList;
    static int counter = 0;
    static JSONArray jsonArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursebin);
        counter = 0;
        sectionInfoList = new ArrayList();
        SharedPreferences settings = getSharedPreferences(MyCoursebinFile, 0);
        Map<String, ?> all = settings.getAll();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            sectionInfo = "http://petri.esd.usc.edu/socAPI/Sections/";
            sectionInfo += entry.getKey();
            sectionInfoList.add(counter, sectionInfo);
            counter++;
        }
        counter = 0;
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            new MyAsyncTask().execute();
        }
        Log.v("url", sectionInfo);
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String...arg0) {
            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost((String)sectionInfoList.get(counter));
            counter++;
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
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                Button myButton = new Button(context);
                String sis_course_id = jsonObject.getString("SIS_COURSE_ID");
                String type = jsonObject.getString("TYPE");
                String instructor = jsonObject.getString("INSTRUCTOR");
                String location = jsonObject.getString("LOCATION");
                String day = jsonObject.getString("DAY");
                String begin_time = jsonObject.getString("BEGIN_TIME");
                String end_time = jsonObject.getString("END_TIME");
                String seats = jsonObject.getString("SEATS");
                myButton.setText(sis_course_id + " " + type + "\nInstructor: " + instructor + "\nLocation: " + location + "\nDay: " + day + "\nTime: " + begin_time + "-" + end_time + "\nSeats Available:" + seats);
                myButton.setBackgroundColor(Color.LTGRAY);
                myButton.setTextColor(Color.BLACK);
                ll.addView(myButton, lp);
                sync = true;
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
