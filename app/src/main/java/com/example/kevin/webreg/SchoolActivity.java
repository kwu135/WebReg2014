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
import android.widget.RelativeLayout;

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
 * Created by Kevin on 2/23/2015.
 */
public class SchoolActivity extends ActionBarActivity {

    static String schoolInfo = "http://petri.esd.usc.edu/socAPI/Schools/";
    static String term_num;
    static String school_des;
    static JSONArray jsonArray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school);

        Intent i = getIntent();
        term_num = i.getStringExtra("term_num");

        new MyAsyncTask().execute();
        /*LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button myButton = new Button(this);
        myButton.setText("HELLO");
        myButton.setBackgroundColor(Color.LTGRAY);
        ll.addView(myButton, lp);
        Button myButton2 = new Button(this);
        myButton2.setText("WORLD");
        myButton2.setBackgroundColor(Color.LTGRAY);
        ll.addView(myButton2, lp);*/
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String...arg0) {
            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpPost httpPost = new HttpPost(schoolInfo);
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
            /*try {
                JSONArray jsonArray= new JSONArray(result);
                /*JSONObject jsonObject = jsonArray.getJSONObject(0);
                String school_des = jsonObject.getString("SOC_SCHOOL_DESCRIPTION");
                Log.v("TEST", school_des);
                test += school_des;
                Log.v("TEST2", test);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            return result;
        }

        protected void onPostExecute(String result) {
            LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,6,0,6);
            Context context=getApplicationContext();
            /*Button myButton = new Button(context);
            myButton.setText("TEXT");
            myButton.setBackgroundColor(Color.LTGRAY);
            myButton.setTextColor(Color.GRAY);
            myButton.setPadding(0,5,0,5);
            ll.addView(myButton, lp);
            Log.v("INC_TEST", "TEST");*/
            try {
                jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                for (int i=0;i<length;i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Button myButton = new Button(context);
                    school_des = jsonObject.getString("SOC_SCHOOL_DESCRIPTION");
                    //Log.v("school_des", school_des);
                    myButton.setText(school_des);
                    myButton.setBackgroundColor(Color.LTGRAY);
                    myButton.setTextColor(Color.BLACK);
                    myButton.setId(i);
                    ll.addView(myButton, lp);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View arg0) {
                            //Starting a new Intent
                            Intent nextScreen = new Intent(getApplicationContext(), DeptActivity.class);
                            nextScreen.putExtra("term_num", term_num);
                            try {
                                JSONObject tempJSONObject = jsonArray.getJSONObject(arg0.getId());
                                String tempSchoolCode = tempJSONObject.getString("SOC_SCHOOL_CODE");
                                nextScreen.putExtra("school", tempSchoolCode);
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
                Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(nextScreen);
                return true;
            case R.id.coursebin:
                Intent nextScreen2 = new Intent(getApplicationContext(), CourseBinActivity.class);
                startActivity(nextScreen2);
                return true;
            case R.id.register:
                Intent nextScreen3 = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(nextScreen3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
