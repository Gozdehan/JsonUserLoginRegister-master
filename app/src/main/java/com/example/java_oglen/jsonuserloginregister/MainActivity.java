package com.example.java_oglen.jsonuserloginregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import static android.R.attr.tag;

public class MainActivity extends AppCompatActivity {

    EditText name,surname,phone,mail,password;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText)findViewById(R.id.uName);
        surname=(EditText)findViewById(R.id.uSurname);
        phone=(EditText)findViewById(R.id.uPhone);
        mail=(EditText)findViewById(R.id.uMail);
        password=(EditText)findViewById(R.id.uPass);

    }

    public void tetikle(View v ) {
        String ad = name.getText().toString();
        String soyad = surname.getText().toString();
        String tel = phone.getText().toString();
        String email = mail.getText().toString();
        String sifre = password.getText().toString();

        url = "http://jsonbulut.com/json/userRegister.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                "userName="+ad+"&" +
                "userSurname="+soyad+"&" +
                "userPhone="+ tel + "&" +
                "userMail="+ email + "&" +
                "userPass="+ sifre;

        Log.d("URL ", url);
        new jsonData(url,this).execute();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    class jsonData extends AsyncTask<Void,Void,Void>{

        String url = "";
        String data = "";
        Context cnx;
        ProgressDialog pro;

        public jsonData(String url, Context cnx){
            this.url=url;
            this.cnx=cnx;
            pro = new ProgressDialog(cnx);
            pro.setMessage("İşlem Yapılıyor, Lütfen Bekleyiniz");
            pro.show();
        }

        @Override
        protected void onPreExecute() {   //tetilenecek ilk metod
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                data = Jsoup.connect(url).ignoreContentType(true).get().body().text();
            }catch (Exception ex){
               Log.e("Data Json Hatası","doInBackground",ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {   //doInBackground dogru cevabı aldığında bu metod çalışır
            super.onPostExecute(aVoid);
            //grafiksel özelliği olan işlemler bu bölümde yapılır
            Log.d("Gelen data: ", data);
            try {
                JSONObject obj = new JSONObject(data);
                boolean durum = obj.getJSONArray("user").getJSONObject(0).getBoolean("durum");
                String mesaj = obj.getJSONArray("user").getJSONObject(0).getString("mesaj");
                if(durum){
                    //kullanıcı kayıt başarılı
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    String kid = obj.getJSONArray("user").getJSONObject(0).getString("kullaniciId");
                    Log.d("kid",kid);
                }else{
                    //kullanıcı kayıt başarısız
                    Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //yükleme ekranını tamamla
            pro.hide();
        }
    }
}
