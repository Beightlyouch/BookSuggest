package com.beightlyouch.booksuggest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //ボタンなど
    Button button;
    TextView titleView;
    TextView dateView;
    TextView publisherView;
    TextView descriptionView;
    TextView authorView;
    TextView urlView;
    ImageView imageView;

    //API
    private final String API_URL_PREFIX = "www.googleapis.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        button = findViewById(R.id.button);
        titleView = findViewById(R.id.titleView);
        dateView = findViewById(R.id.dateView);
        publisherView = findViewById(R.id.publisherView);
        descriptionView = findViewById(R.id.descriptionView);
        authorView = findViewById(R.id.authorView);
        urlView = findViewById(R.id.urlView);
        imageView = findViewById(R.id.imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("boolean", true);
                Calendar cl = Calendar.getInstance();
                editor.putLong("push", cl.getTimeInMillis());
                editor.commit();
                canPushButton();

                //匿名クラス
                new AsyncTask<String, Void, String>() { //ボタンを押した時に非同期処理を開始します

                    @Override
                    protected String doInBackground(String... params) {
                        final StringBuilder result = new StringBuilder();
                        Uri.Builder uriBuilder = new Uri.Builder(); //Uri.Builderで要素を入力
                        uriBuilder.scheme("https");
                        uriBuilder.authority(API_URL_PREFIX); //ホスト?
                        uriBuilder.path("/books/v1/volumes");
                        uriBuilder.appendQueryParameter("q", getRandomKeyword()).appendQueryParameter("startIndex", String.valueOf(getRandomPage()));
                        final String uriStr = uriBuilder.build().toString(); //URIを作成して文字列に

                        try {
                            URL url = new URL(uriStr); //文字列からURLに変換
                            HttpURLConnection con = null; //HTTP接続の設定を入力していく
                            con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            con.setDoInput(true); //?
                            con.connect(); //HTTP接続

                            final InputStream in = con.getInputStream(); //情報を受け取り表示するための形式に
                            final InputStreamReader inReader = new InputStreamReader(in);
                            final BufferedReader bufReader = new BufferedReader(inReader);

                            String line = null;
                            while((line = bufReader.readLine()) != null) {
                                result.append(line);
                            }
                            Log.e("but", result.toString());
                            bufReader.close();
                            inReader.close();
                            in.close();
                        }

                        catch(Exception e) { //エラーの時に呼び出される
                            Log.e("button", e.getMessage());
                        }

                        return result.toString(); //onPostExecuteへreturn
                    }

                    @Override
                    protected void onPostExecute(String result) { //doInBackgroundが終わると呼び出される
                        try {
                            JSONObject json = new JSONObject(result);
                            String items = json.getString("items");
                            JSONArray itemsArray = new JSONArray(items);
                            JSONObject bookInfo = itemsArray.getJSONObject(3).getJSONObject("volumeInfo");
                            JSONObject saleInfo = itemsArray.getJSONObject(3).getJSONObject("saleInfo");

                            //題名
                            String title = bookInfo.getString("title");
                            //オプション属性: optStringで取得
                            //出版日
                            String publishedDate = bookInfo.optString("publishedDate");
                            //出版社
                            String publisher = bookInfo.optString("publisher");
                            //説明文
                            String description = bookInfo.optString("description");
                            //価格
                            String price = bookInfo.optString("");
                            //（代表）著者
                            String authorArrayText = bookInfo.optString("authors");
                            String author = "";
                            if(authorArrayText != "") {
                                JSONArray authorArray = new JSONArray(authorArrayText);
                                String.valueOf(authorArray.get(0));
                            }
                            // サーバからURL文字列を取得し、URLに変換
                            if(bookInfo.optJSONObject("imageLinks")!= null) {
                                String image_URL = bookInfo.optJSONObject("imageLinks").optString("thumbnail");
                                setImage(image_URL);
                            }

                            //google Books URL
                            String google_URL = saleInfo.optString("buyLink");
                            urlView.setAutoLinkMask(Linkify.WEB_URLS);

                            //SharedPreference
                            editor.putString("title", title);
                            editor.putString("publishedDate", publishedDate);
                            editor.putString("publisher", publisher);
                            editor.putString("author", author);
                            editor.putString("description", description);
                            editor.putString("google_URL", google_URL);
                            editor.commit();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }.execute();

                Calendar cal = Calendar.getInstance();
                long now = cal.getTimeInMillis();
                long long_time = now - prefs.getLong("push", 0L);

            }
        });
    }

    public String getRandomKeyword() {
        String[] moji = {"あ", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "さ", "し", "す", "せ",
                         "そ", "た", "ち", "つ", "て", "と", "な", "に", "ぬ", "ね", "の", "は", "ひ", "ふ",
                         "へ", "ほ", "ま", "み", "む", "め", "も", "や", "ゆ", "よ", "ら", "り", "る", "れ",
                         "ろ", "わ", "を", "ん" };
        Random rand = new Random();
        int num = rand.nextInt(46);
        return moji[num];
    }

    public int getRandomPage() {
        Random rand = new Random();
        int page = rand.nextInt(250);
        return page;
    }


    @Override
    protected void onStart() {
        super.onStart();
        canPushButton();
        // Glideを設定
    }

    @Override
    protected void onResume() {
        super.onResume();
        canPushButton();
    }

    public void canPushButton() {
        Calendar cl = Calendar.getInstance();
        long now = cl.getTimeInMillis();
        int push = (int)prefs.getLong("push", 0L);
        long long_time = now - prefs.getLong("push", 0L);

        new CountDownTimer(60 * 1000 - long_time, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondInFuture = millisUntilFinished / 1000;
                long hour = (secondInFuture / 3600);
                long minute = (secondInFuture - 3600 * hour) / 60;
                long second = secondInFuture - 3600 * hour - 60 * minute;
                button.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
                button.setEnabled(false);
            }

            public void onFinish() {
                button.setText("done!");
                button.setEnabled(true);
            }
        }.start();
    }

    public void setInfo() {
        String title = prefs.getString("title", "タイトル");
        String publishedDate = prefs.getString("publishedDate", "出版年月日");
        String publisher = prefs.getString("publisher", "出版社");
        String author = prefs.getString("author", "著者");
        String description = prefs.getString("description", "本の紹介文が表示されます。※紹介文がないため表示されない場合もあります。");
        String google_URL = prefs.getString("google_URL", "");

        titleView.setText(title);
        dateView.setText(publishedDate);
        publisherView.setText(publisher);
        authorView.setText(author);
        descriptionView.setText(description);
        urlView.setText(google_URL);
    }


    public void setImage(String image_URL) {
        Picasso.get().
                load(image_URL.replace("http", "https")).
                into(imageView);
    }

}

