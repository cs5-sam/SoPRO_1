package android.example.sopro.api;

import android.app.Application;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WebAPI implements API {
    public static final String BASE_URL = "http://node-dev.ap-southeast-1.elasticbeanstalk.com/";
    private final Application mApplication;
    private RequestQueue mRequestQueue;

    public WebAPI(Application application){
        mApplication = application;
        mRequestQueue = Volley.newRequestQueue(application);
    }
    public void login(String name,String password){

        String url = BASE_URL + "login";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username",new JSONObject(name));
            jsonObject.put("password",new JSONObject(password));


            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(mApplication, "Successful response", Toast.LENGTH_SHORT).show();
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error response" +error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,jsonObject,successListener,errorListener);
            mRequestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(mApplication, "JSON exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
