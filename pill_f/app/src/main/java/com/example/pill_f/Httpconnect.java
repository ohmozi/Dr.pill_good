package com.example.pill_f;

import android.os.AsyncTask;
import android.util.Base64;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Httpconnect extends AsyncTask{

    JSONObject jsonObject;
    HttpClient httpClient;
    StringEntity se;
    String url;
    String name;
    byte[] image;

    public Httpconnect(byte[] data,String nn){
        image = data;
        name=nn;
    }

    @Override protected void onPreExecute() { super.onPreExecute(); }

    @Override
    protected Object doInBackground(Object[] objects) {

        jsonObject = new JSONObject();
        httpClient = new DefaultHttpClient();

        String encoded=Base64.encodeToString(image,0);
        url="http://13.125.23.142:3000/test";

        try {
            jsonObject.put("image",encoded);
            jsonObject.put("name",name);
            se = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpPost request = new HttpPost(url);

        request.setEntity(se);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json;charset=UTF-8");

        ResponseHandler responseHandler = new BasicResponseHandler();
        try {
            HttpResponse httpResponse =  httpClient.execute(request);
            String json_string = EntityUtils.toString(httpResponse.getEntity());
            JSONObject temp1 = new JSONObject(json_string);
            return temp1;
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }
}