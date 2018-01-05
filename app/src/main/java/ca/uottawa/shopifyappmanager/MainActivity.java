package ca.uottawa.shopifyappmanager;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;


import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private  ListView productListView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> titles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productListView = (ListView) findViewById(R.id.listViewProducts);
        titles=new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this,R.layout.simplerow,titles);
        productListView.setAdapter(listAdapter);

        new MyTaskGet().execute();

    }
    class MyTaskGet extends AsyncTask<Void,String,Void>
    {
        ArrayAdapter<String> adapter;

        @Override
        protected void onPreExecute() {
           adapter = (ArrayAdapter<String>) productListView.getAdapter();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection myConnection = null;
            try {
                URL shopifyEndpoint = new URL("https://e4296886c1c43ea7e3135072efc2451d:140b64e5268784ac8e5478b7fcd833fa@testitos-latitos.myshopify.com/admin/products.json");
                myConnection = (HttpURLConnection) shopifyEndpoint.openConnection();
                Authenticator.setDefault(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("e4296886c1c43ea7e3135072efc2451d", "140b64e5268784ac8e5478b7fcd833fa".toCharArray());
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //myConnection.setRequestProperty("GET","" );
            try {
                if (myConnection.getResponseCode()==200){
                    publishProgress("rip starting late T_T");
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.setLenient(false);

                    jsonReader.beginObject();
                    publishProgress(jsonReader.nextName());
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()){
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()){
                            String key0 = jsonReader.nextName();
                            if (key0.equals("title")){
                                publishProgress(jsonReader.nextString());
                            }
                            else if(key0.equals("body_html")){
                                jsonReader.skipValue();
                                //publishProgress(jsonReader.nextString());
                            }
                            else if (key0.equals("tags")){
                                jsonReader.skipValue();
                                //publishProgress(jsonReader.nextString());
                            }
                            else if (key0.equals("variants")){
                                jsonReader.beginArray();
                                while (jsonReader.hasNext()){
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()){
                                        String key1 = jsonReader.nextName();
                                        if (key1.equals("title")){
                                            publishProgress(jsonReader.nextString());
                                        }
                                        else{
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.endObject();
                                }
                                jsonReader.endArray();
                            }
                            else if (key0.equals("options")){
                                jsonReader.beginArray();
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()){
                                    String key2 = jsonReader.nextName();
                                    if (key2.equals("values")){
                                        jsonReader.beginArray();
                                        while (jsonReader.hasNext()){
                                            jsonReader.skipValue();
                                        }
                                        jsonReader.endArray();
                                    }else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.endObject();
                                jsonReader.endArray();
                            }
                            else if(key0.equals("images")){
                                jsonReader.skipValue();
                                /*
                                jsonReader.beginArray();
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()){
                                    String key3 = jsonReader.nextName();
                                    if (key3.equals("variant_ids")){
                                        jsonReader.beginArray();
                                        while (jsonReader.hasNext()){
                                            jsonReader.skipValue();
                                        }
                                        jsonReader.endArray();
                                    }
                                    else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.endObject();
                                jsonReader.endArray();
                                */
                            }
                            else if(key0.equals("image")){
                                jsonReader.skipValue();
                                /*
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()){
                                    String key3 = jsonReader.nextName();
                                    if (key3.equals("variant_ids")){
                                        jsonReader.beginArray();
                                        while (jsonReader.hasNext()){
                                            jsonReader.skipValue();
                                        }
                                        jsonReader.endArray();
                                    }
                                    else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.endObject();
                            */
                            }
                            else{
                                jsonReader.skipValue();
                            }
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    jsonReader.endObject();

                    myConnection.disconnect();
                }
                else{
                    //publishProgress("I didnt make it");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }


}
