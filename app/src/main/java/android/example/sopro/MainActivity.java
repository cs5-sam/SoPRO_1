package android.example.sopro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginBtn;
    TextView registerText;

    // INPUT API URL FOR LOGIN
    final String BASE_URL = "http://node-dev.ap-southeast-1.elasticbeanstalk.com/";
    private String LOGIN_URL = BASE_URL + "login";
    private String name = "";
    private String password = "";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEmail = findViewById(R.id.email_input);
        loginPassword = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);
        registerText = findViewById(R.id.register_text);
        builder = new AlertDialog.Builder(MainActivity.this);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String loginStringpassword = loginPassword.getText().toString().trim();
                name = email.trim();
                password = loginStringpassword.trim();

                if(name.equals("") || password.equals("")){
                    builder.setTitle("Something went wrong");
                    displayAlert("Enter a valid Username/Password");
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                /*JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);*/
                                String code = response;
                                if(code.equals("Wrong Credential")){
                                    builder.setTitle("Login Error...");
                                    displayAlert("message");
                                }
                                else{
                                    Intent i =  new Intent(MainActivity.this,HomeActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name","name");
                                    i.putExtras(bundle);
                                    startActivity(i);
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("username",name);
                            params.put("password",password);
                            return params;
                        }
                    };
                    MySingleton.getInstance(MainActivity.this).addToRequestque(stringRequest);
                }

            }
        });
    }
    public void displayAlert(String message){

        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginEmail.setText("");
                loginPassword.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
