package com.turtillion.estoredelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.turtillion.estoredelivery.base.BaseActivity;
import com.turtillion.estoredelivery.databinding.ActivityLoginBinding;
import com.turtillion.estoredelivery.utils.CommonUtils;
import com.turtillion.estoredelivery.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
ActivityLoginBinding binding;
    String sUsername, sPassword;
    boolean showPassword = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        mActivity = this;

        listeners();
    }

    private void listeners() {
        binding.btnLogin.setOnClickListener(this);
        binding.btnForgot.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.showPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (CommonUtils.checkConnectivity(this)) {
                    checkValues();
                } else {
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnForgot:
       //         startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                break;
            case R.id.btnRegister:
        //        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.showPassword:
                showPassword = !showPassword;
                if (showPassword) {
                    binding.showPassword.setImageResource(R.drawable.viewpassword);
                    binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    binding.showPassword.setImageResource(R.drawable.hidepassword);
                    binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                binding.edtPassword.setSelection(binding.edtPassword.length());
                break;
        }
    }

    private void checkValues() {

        sUsername = binding.edtUsrname.getText().toString().trim();
        sPassword = binding.edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(sUsername)) {
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sPassword)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (sUsername.length() < 10) {
            Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
        } else if (sPassword.length() < 8) {
            Toast.makeText(this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        } else {
            CommonUtils.setProgressBar(this);
            loginMethod();
        }
    }

    private void loginMethod() {
       // CommonUtils.setProgressBar(this);
        final JSONObject params = new JSONObject();
        try {
            params.put("phone", sUsername);
            params.put("password", getSha1Hex(sPassword));

            Log.d("request-->", params.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://day2night.in/delivery/login.php",
                response -> {
                    Log.d("response-->", response);
                    CommonUtils.cancelProgressBar();
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("response-->", obj.toString());
                        if (obj.getString("code").equals("1")) {
                            editor.putString(Constants.DBOY_ID, obj.getJSONObject("dboy_detail").getString("dboy_id"));
                            editor.putString(Constants.DBOY_NAME, obj.getJSONObject("dboy_detail").getString("dboy_name"));
                            editor.putString(Constants.PHONE, obj.getJSONObject("dboy_detail").getString("phone"));
                            editor.putString(Constants.PINCODE, obj.getJSONObject("dboy_detail").getString("pincode"));

                           // editor.putString(Constants.LIVE_LATITUDE,"28.5821195");
                           // editor.putString(Constants.LIVE_LONGITUDE,"77.3266991");
                            
                            editor.apply();
                            gotoDashBoard(obj);
                        } else {
                            Toast.makeText(LoginActivity.this, obj.getString("status"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    CommonUtils.cancelProgressBar();
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return params.toString().getBytes();
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void gotoDashBoard(JSONObject obj) {


        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    public static String getSha1Hex(String clearString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}