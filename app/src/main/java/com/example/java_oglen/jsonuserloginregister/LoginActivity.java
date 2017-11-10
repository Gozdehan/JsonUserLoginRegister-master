package com.example.java_oglen.jsonuserloginregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class LoginActivity extends AppCompatActivity {

    Button btnReg;
    EditText mail, pass;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = (EditText)findViewById(R.id.txtMail);
        pass=(EditText)findViewById(R.id.txtPass);
        btnReg = (Button)findViewById(R.id.btnRegister);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    public void loginYap(View view) {
        String eposta = mail.getText().toString();
        String sifre = pass.getText().toString();

        url = "http://jsonbulut.com/json/userLogin.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                "userEmail=" + eposta + "&" +
                "userPass=" + sifre + "&" +
                "face=no";

        new jsonData(url,this).execute();
        startActivity(new Intent(LoginActivity.this,ProfileActivity.class));

    }

    class jsonData extends AsyncTask<Void,Void,Void> {

        String url = "";
        String dataLogin = "";
        Context cnx;
        ProgressDialog pro;

        public jsonData(String url, Context cnx){
            this.url=url;
            this.cnx=cnx;
            pro = new ProgressDialog(cnx);
            pro.setMessage("Giriş Yapılıyor, Lütfen Bekleyiniz");
            pro.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                dataLogin = Jsoup.connect(url).ignoreContentType(true).get().body().text();
            }catch (Exception ex){
                Log.e("Data JSON Hatası","doInBackground",ex);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.d("Gelen Data: ", dataLogin);

            try {
                JSONObject object = new JSONObject(dataLogin);
                boolean durum = object.getJSONArray("user").getJSONObject(0).getBoolean("durum");
                String mesaj = object.getJSONArray("user").getJSONObject(0).getString("mesaj");
                if(durum){
                    //kullanıcı giriş başarılı
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    String userInfo = object.getJSONArray("user").getJSONObject(0).getString("bilgiler");
                    Log.d("USER INFO: ", userInfo);
                }else{
                    //kullanıcı giriş başarısız
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            pro.dismiss();
        }
    }
}
