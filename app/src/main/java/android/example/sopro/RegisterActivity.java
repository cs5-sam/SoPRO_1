package android.example.sopro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEdit, emailEdit, passwordEdit;
    Button registerBtn;
    TextView loginText;

    final String BASE_URL = "http://node-dev.ap-southeast-1.elasticbeanstalk.com/";
    final String REG_URL = BASE_URL + "registration";
    private String reg_name, reg_password, reg_email;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEdit = findViewById(R.id.register_name);
        emailEdit = findViewById(R.id.email_input_register);
        passwordEdit = findViewById(R.id.password_input_register);
        registerBtn = findViewById(R.id.register_btn);
        loginText = findViewById(R.id.login_text);
        builder = new AlertDialog.Builder(RegisterActivity.this);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_name = nameEdit.getText().toString().trim();
                reg_password = passwordEdit.getText().toString().trim();
                reg_email = emailEdit.getText().toString().trim();
                if(reg_name.equals("") || reg_email.equals("") || reg_password.equals("")){
                    builder.setTitle("Something went wrong");
                    builder.setMessage("Fill all the fields");
                    displayAlert("Input error");
                }
                else{
                    /*if(!(reg_password).equals(confirm_password)){
                        builder.setTitle("Something went wrong");
                        builder.setMessage("Passwords not match");
                        displayAlert("Input error");
                    }*/
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, REG_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                /*JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String message = jsonObject.getString("message");*/
                                String code = response;

                                builder.setTitle("Server Response");
                                builder.setMessage(code);
                                displayAlert(code);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("username",reg_name);
                            params.put("password",reg_password);
                            params.put("email",reg_email);
                            return params;
                        }
                    };
                    MySingleton.getInstance(RegisterActivity.this).addToRequestque(stringRequest);
                }

            }
        });
    }

    public void displayAlert(final String message){
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(message.equals("Input error")){
                    nameEdit.setText("");
                    passwordEdit.setText("");
                    emailEdit.setText("");
                }
                else if(message.equals("Registration SuccessFull " + reg_name)){
                    Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(message.equals("Reg FAILED")){
                    nameEdit.setText("");
                    passwordEdit.setText("");
                    emailEdit.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}