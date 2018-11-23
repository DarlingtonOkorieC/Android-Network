package com.example.nnenna.androidnetwork.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nnenna.androidnetwork.R;
import com.example.nnenna.androidnetwork.utils.JsonParser;
import com.example.nnenna.androidnetwork.utils.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class Registration extends AppCompatActivity {
    final Utils util = new Utils();
    KProgressHUD hud;

    private EditText etFirstName, etLastName, etEmail, etPassword, etPhone;
    private Spinner spGender;
    private Button btnRegster;
    private String email, firstName, lastName, password, phone, gender, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirst);
        etLastName = findViewById(R.id.etLast);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        spGender = findViewById(R.id.spGender);
        btnRegster = findViewById(R.id.btnRegister1);

        btnRegster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

    }

    private void validation() {
        email = etEmail.getText().toString().trim();
        firstName = etFirstName.getText().toString().trim();
        lastName = etLastName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        name = firstName + " " + lastName;
        gender = spGender.getItemAtPosition(spGender.getSelectedItemPosition()).toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter a correct email");
            return;

        }
        if (password.length() < 8) {
            etPassword.setError("Password cannot be less than 8");
            return;

        }

        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("enter firstname");
            return;

        }
        if (TextUtils.isEmpty(lastName)) {
            etLastName.setError("enter lastname");
            return;

        }
        if (gender.equalsIgnoreCase("Select gender")){
            Toast.makeText(this, "pls select a gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!util.isValidPhoneNumber(this, phone) ){
            etPhone.setError("enter phone number");
            return;
        }
        if (phone.length()<11){
            etPhone.setError("invalid phone number");
        }
        if(util.isNetworkAvailable(getApplicationContext())){
            try{
                new sendPostRequest().execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            String message = "check your network connection";
            Toasty.error(Registration.this, message, Toast.LENGTH_SHORT, true ).show();
        }
    }

    private class sendPostRequest extends AsyncTask<String, Void, JSONObject> {
        JsonParser jsonParser = new JsonParser();

        private  static final  String REGISTRATION_URL = "10.10.11.142:8000/api/users";
       String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone_no = etPhone.getText().toString().trim();
        String name = firstName + " " + lastName;
        String gender = spGender.getItemAtPosition(spGender.getSelectedItemPosition()).toString();

        @Override
        protected void onPreExecute() {
            hud = KProgressHUD.create(Registration.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("please wait...")
                    .setDetailsLabel("Registering..." + name)
                    .setCancellable(true)
                    .setBackgroundColor(Color.BLUE)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            super.onPreExecute();

        }
        @Override
        protected JSONObject doInBackground(String... strings) {
            try{
                HashMap<String, String > params = new HashMap<>();
                params.put("name", name);
                params.put("phone_no", phone_no);
                params.put("password", password);
                params.put("gender", gender);
                params.put("email", email);

                JSONObject json = jsonParser.makeHttpRequest(REGISTRATION_URL, "POST", params);

                if (json != null){
                    return json;
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hud.dismiss();
                            String message = "Network Error Pls Try Again Later";
                            util.toastMessage(getApplicationContext(), message);

                        }
                    });
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(JSONObject json) {
            hud.dismiss();
            try{
             if (json != null && json.getString("status").equals(200)){
                 Log.d("jsonerror", ""+json);
             }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch(NullPointerException e){
                e.printStackTrace();
            }
            super.onPostExecute(json);
        }
    }
}
