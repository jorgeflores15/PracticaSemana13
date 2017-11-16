package com.linder.logginsemana14.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.linder.logginsemana14.R;
import com.linder.logginsemana14.Service.ApiService;
import com.linder.logginsemana14.Service.ApiServiceGenerator;
import com.linder.logginsemana14.Service.ResponseMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText txtnombre, txtemail, txtpassword, txtpasswordAgain;
    Button finaRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtnombre = (EditText) findViewById(R.id.nameRegister);
        txtemail = (EditText) findViewById(R.id.emailRegister);
        txtpassword = (EditText) findViewById(R.id.passwordRegister);
        txtpasswordAgain = (EditText) findViewById(R.id.passwordRegisteragain);

        finaRegister = (Button) findViewById(R.id.finalRegister);

        finaRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = txtnombre.getText().toString();
                final String correo = txtemail.getText().toString();
                final String password = txtpassword.getText().toString();
                final String passwordAgain = txtpasswordAgain.getText().toString();


                if (username.isEmpty() || correo.isEmpty() || password.isEmpty()  || passwordAgain.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Rellene los datos", Toast.LENGTH_SHORT).show();

                }

                if (password.equals(passwordAgain)){
                    ApiService service = ApiServiceGenerator.createService(ApiService.class);

                    Call<ResponseMessage> call = null;
                    call = service.registrarUsuario(username, correo, password);

                    call.enqueue(new Callback<ResponseMessage>() {
                        @Override
                        public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                            try {
                                int statusCode = response.code();
                                Log.d("Satatus", "HTTP status code: " + statusCode);
                                if (response.isSuccessful()) {
                                    ResponseMessage responseMessage = response.body();
                                    Log.d("Response Messgae", "responseMessage: " + responseMessage);
                                    Toast.makeText(RegisterActivity.this, responseMessage.getMessage(), Toast.LENGTH_LONG).show();
                                    // Go to Login
                                    goLogin();
                                } else {
                                    Log.e("OnError", "onError: " + response.errorBody().string());
                                    //throw new Exception();
                                }

                            } catch (Throwable t) {
                                try {
                                    Log.e("onThrowable", "onThrowable: " + t.toString(), t);
                                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (Throwable x) {
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMessage> call, Throwable t) {
                            Log.e("OnFail", "onFailure: " + t.toString());
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });

                }else{
                    Toast.makeText(RegisterActivity.this, "Las contraseña deben coincidir", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("correo",txtemail.getText().toString());
        startActivity(intent);
        finish();
    }
}
