package android.example.sopro;

import android.app.Application;
import android.example.sopro.api.API;
import android.example.sopro.api.WebAPI;
import android.view.Display;

public class Model {

    private static Model sInstance = null;
    private final Application mApplication;
    private final API mApi;

    public Model(Application application) {
        mApplication = application;
        mApi = new WebAPI(mApplication);
    }

    public static Model getInstance(MainActivity mainActivity, Application application){
        if (sInstance == null) {
            sInstance = new Model(application);
        }
        return sInstance;
    }
    public Application getApplication(){
        return mApplication;
    }
    public void login(String name,String password){
        mApi.login(name, password);
    }
}
