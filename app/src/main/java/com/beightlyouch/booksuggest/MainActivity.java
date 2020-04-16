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
import android.preference.PreferenceManager;
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

    //ボタン
    Button button;
    Button button2;

    TextView titleView;
    TextView dateView;
    TextView descriptionView;
    TextView authorView;
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
        button2 = findViewById(R.id.button2);
        titleView = findViewById(R.id.titleView);
        dateView = findViewById(R.id.dateView);
        descriptionView = findViewById(R.id.descriptionView);
        authorView = findViewById(R.id.authorView);
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
                            Log.d("number", String.valueOf(itemsArray.length()));
                            Log.d("numb", String.valueOf(bookInfo));

                            String title = bookInfo.getString("title");
                            String publishedDate = bookInfo.optString("publishedDate");
                            String description = bookInfo.optString("description");
                            String authorArrayText = bookInfo.optString("authors");
                            String author = "";
                            if(authorArrayText != "") {
                                JSONArray authorArray = new JSONArray(authorArrayText);
                                String.valueOf(authorArray.get(0));
                            }
                            // サーバからURL文字列を取得し、URLに変換
                            String image_URL = bookInfo.getJSONObject("imageLinks").optString("smallThumbnail");
                            setImage(image_URL);

                            titleView.setText(title);
                            dateView.setText(publishedDate);
                            authorView.setText(author);
                            descriptionView.setText(description);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }.execute();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cl = Calendar.getInstance();
                long now = cl.getTimeInMillis();
                Log.d("NOW", String.valueOf(now));
                Log.d("PUSH", String.valueOf(prefs.getLong("push", 0)));
                Log.d("MINUS", String.valueOf(now - prefs.getLong("push", 0L)));
                canPushButton();
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
        int time = (int)long_time;
        Log.d("MINUS", String.valueOf(time));
        if(push == 0 || time >=  60 * 1000) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    public void setImage(String image_URL) {
        Picasso.get().
                load(image_URL.replace("http", "https")).
                into(imageView);
    }

}

