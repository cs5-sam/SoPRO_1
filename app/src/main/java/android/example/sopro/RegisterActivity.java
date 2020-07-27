package android.example.sopro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEdit, emailEdit, passwordEdit;
    Button registerBtn;
    TextView loginText;

    final String url_Register="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEdit = findViewById(R.id.register_name);
        emailEdit = findViewById(R.id.email_input_register);
        passwordEdit = findViewById(R.id.password_input_register);
        registerBtn = findViewById(R.id.register_btn);
        loginText = findViewById(R.id.login_text);

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
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                new RegisterUser().execute(name,email,password);
            }
        });
    }

    public class  RegisterUser extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String email = strings[1];
            String password = strings[2];
            String finalUrl = url_Register + "?user_name=" + name +
                    "&user_id=" + email +
                    "&user_password=" + password;

                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(finalUrl)
                            .get()
                            .build();
                    Response response = null;

                    try {
                        response = okHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            String result = response.body().string();

                            if (result.equalsIgnoreCase("User registered successfully")) {
                                showMessage("Register successful");
                                Intent i = new Intent(RegisterActivity.this,
                                        MainActivity.class);
                                startActivity(i);
                                finish();
                            } else if (result.equalsIgnoreCase("User already exists")) {
                                showMessage("User already exists");
                            } else {
                                showMessage("oop! please try again");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            return null;
        }
    }

    public void showMessage(final String message){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}