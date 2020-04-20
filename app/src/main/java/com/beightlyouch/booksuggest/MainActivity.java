package com.beightlyouch.booksuggest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    //ボタンなど
    Button button;
    TextView titleView;
    TextView dateView;
    TextView publisherView;
    TextView descriptionView;
    TextView authorView;
    TextView urlView;
    ImageView imageView;
    ScrollView scrollView;
    LinearLayout infoView;
    LinearLayout totalLayout;
    ProgressBar progressBar;

    //これいいんかな？
    Activity activity = this;

    ProgressDialog progressDialog;

    private static final String API_URL_PREFIX = "app.rakuten.co.jp";
    //private final String API_URL_PREFIX = "www.googleapis.com";

    @Override
    //クラス分けろ
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("新しい本と出会いましょう");

        prefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();

        button = findViewById(R.id.button2);
        titleView = findViewById(R.id.titleView);
        dateView = findViewById(R.id.dateView);
        publisherView = findViewById(R.id.publisherView);
        descriptionView = findViewById(R.id.descriptionView);
        authorView = findViewById(R.id.authorView);
        imageView = findViewById(R.id.imageView);
        scrollView = findViewById(R.id.scrollView);
        infoView = findViewById(R.id.infoView);
        urlView = findViewById(R.id.urlView);
        totalLayout = findViewById(R.id.totalLayout);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        //画面サイズ取得
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int height = p.y;
        int width = p.x;

        ViewGroup.LayoutParams scrollParams;
        ViewGroup.LayoutParams urlParams;
        ViewGroup.LayoutParams imageParams;
        ViewGroup.LayoutParams infoParams;
        ViewGroup.LayoutParams totalParams;
        ViewGroup.LayoutParams buttonParams;

        scrollParams = scrollView.getLayoutParams();
        imageParams = imageView.getLayoutParams();
        infoParams =  infoView.getLayoutParams();
        urlParams = urlView.getLayoutParams();
        totalParams = totalLayout.getLayoutParams();
        buttonParams = button.getLayoutParams();

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)scrollParams;
        ViewGroup.MarginLayoutParams imagelp = (ViewGroup.MarginLayoutParams)imageParams;
        ViewGroup.MarginLayoutParams ulp = (ViewGroup.MarginLayoutParams)urlParams;
        ViewGroup.MarginLayoutParams infolp = (ViewGroup.MarginLayoutParams)infoParams;
        ViewGroup.MarginLayoutParams totallp = (ViewGroup.MarginLayoutParams)totalParams;
        ViewGroup.MarginLayoutParams buttonlp = (ViewGroup.MarginLayoutParams)buttonParams;

        //高さを決める
        totalParams.height = (int)(height * 1 / 5);
        imageParams.height = (int)(height * 1 / 5);
        infoParams.height = (int)(height * 1 / 5);
        scrollParams.height = (height -
                        ((30 + mlp.bottomMargin) +
                        (urlParams.height + ulp.topMargin + ulp.bottomMargin) +
                        (totalParams.height + totallp.topMargin + totallp.bottomMargin) +
                        (buttonParams.height + buttonlp.topMargin + buttonlp.bottomMargin))) * 2 / 3;

        //説明文のマージン
        infolp.setMargins(100, infolp.topMargin, mlp.rightMargin,mlp.bottomMargin);
        mlp.setMargins(mlp.leftMargin, 30, mlp.rightMargin,0);

        infoView.setLayoutParams(infolp);
        scrollView.setLayoutParams(mlp);

        //幅を決める
        urlParams.width = (int)(width * 8 / 10);
        totalParams.width = (int)(width * 8 / 10);
        scrollParams.width = (int)(width * 8 / 10);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("boolean", true);
                Calendar cl = Calendar.getInstance();
                editor.putLong("push", cl.getTimeInMillis());
                editor.commit();
                canPushButton();

                progressBar.setVisibility(View.VISIBLE);
                MyAsynk asynk = new MyAsynk(titleView, dateView, publisherView, authorView, urlView, descriptionView, imageView, activity, progressBar);
                asynk.execute();
            }
        });
    }


    public static String getRandomGenre() {
        Random rand = new Random();
        int num = rand.nextInt(BookCenter.genre.length);
        return BookCenter.genre[num];
    }

    public static int getRandomPage() {
        Random rand = new Random();
        int page = rand.nextInt(100) + 1;
        return page;
    }

    @Override
    protected void onStart() {
        super.onStart();
        canPushButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        canPushButton();
        setInfo();
    }

    public void canPushButton() {
        Calendar cl = Calendar.getInstance();
        long now = cl.getTimeInMillis();
        int push = (int)prefs.getLong("push", 0L);
        long long_time = now - prefs.getLong("push", 0L);

        new CountDownTimer(6 * 1000 - long_time, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondInFuture = millisUntilFinished / 1000;
                long hour = (secondInFuture / 3600);
                long minute = (secondInFuture - 3600 * hour) / 60;
                long second = secondInFuture - 3600 * hour - 60 * minute;
                button.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
                button.setEnabled(false);
            }

            public void onFinish() {
                button.setText("新しい本を探す");
                button.setEnabled(true);
            }
        }.start();
    }

    //staticにしてから使えてない
    public void setInfo() {
        String description = prefs.getString("itemCaption", "紹介文は登録されていません。");
        String title = prefs.getString("title", "・タイトル");
        String publishedDate = prefs.getString("salesDate", "・出版年月日");
        String publisher = prefs.getString("publisherName", "・出版社");
        String author = prefs.getString("author", "・著者");
        String URL = prefs.getString("URL", "");

        titleView.setText(title);
        dateView.setText(publishedDate);
        publisherView.setText(publisher);
        authorView.setText(author);
        urlView.setText(URL);
        descriptionView.setText(description);
        Linkify.addLinks(urlView, Linkify.ALL);
        setImage();
    }

    //staticにしてから使えてない?
    public void setImage() {
        String image_str = prefs.getString("image_URL", "");
        Uri image_URL = Uri.parse(image_str);
        Picasso.get().
                load(image_URL).
                error(ResourcesCompat.getDrawable(getResources(), R.drawable.default_book, null)).
                into(imageView);
    }

    static class MyAsynk extends AsyncTask<String, Void, String> {
        int count = 0;

        //なんだこれ？　Acitivityとともに消えるとでもいうのか？ GC
        private final WeakReference<TextView> titleViewReference;
        private final WeakReference<TextView> dateViewReference;
        private final WeakReference<TextView> publisherViewReference;
        private final WeakReference<TextView> authorViewReference;
        private final WeakReference<TextView> urlViewReference;
        private final WeakReference<TextView> descriptionViewReference;
        private final WeakReference<ImageView> imageViewWeakReference;
        private final WeakReference<Activity> activityWeakReference;
        private final WeakReference<ProgressBar> progressBarWeakReference;

        public MyAsynk(TextView titleView, TextView dateView, TextView publisherView,
                       TextView authorView, TextView urlView, TextView descriptionView,
                       ImageView imageView, Activity activity, ProgressBar progressBar) {
            titleViewReference = new WeakReference<TextView>(titleView);
            dateViewReference = new WeakReference<TextView>(dateView);
            publisherViewReference = new WeakReference<TextView>(publisherView);
            authorViewReference = new WeakReference<TextView>(authorView);
            urlViewReference = new WeakReference<TextView>(urlView);
            descriptionViewReference = new WeakReference<TextView>(descriptionView);
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            activityWeakReference = new WeakReference<Activity>(activity);
            progressBarWeakReference = new WeakReference<ProgressBar>(progressBar);
        }

        //ボタンを押した時に非同期処理を開始します
        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            while (count == 0) {
                //while文
                //result.clear的な
                result.setLength(0);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Uri.Builder uriBuilder = new Uri.Builder(); //Uri.Builderで要素を入力
                uriBuilder.scheme("https");
                uriBuilder.authority(API_URL_PREFIX); //ホスト?
                uriBuilder.path("/services/api/BooksBook/Search/20170404");
                uriBuilder.appendQueryParameter("format", "json").
                        appendQueryParameter("booksGenreId", getRandomGenre()).
                        appendQueryParameter("page", String.valueOf(getRandomPage())).
                        //環境変数?
                        appendQueryParameter("applicationId", "1073644741330296219");

                final String uriStr = uriBuilder.build().toString(); //URIを作成して文字列に
                Log.d("url", uriStr);
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
                    while ((line = bufReader.readLine()) != null) {
                        result.append(line);
                        Log.d("ここここここは？", line);
                    }
                    bufReader.close();
                    inReader.close();
                    in.close();

                    JSONObject json = new JSONObject(result.toString());
                    count = Integer.parseInt(json.optString("count"));

                    Log.d("errorは?", json.optString("error") + "aaaaa");
                    Log.d("resultは？", result.toString());
                    Log.d("countは？", String.valueOf(count));
                } catch (Exception e) { //エラーの時に呼び出される
                    Log.e("ERROR", e.toString());
                    //配列から内容を出力する
                    for(StackTraceElement a : e.getStackTrace()){
                        System.out.println(a);
                    }
                }
            }
            return result.toString(); //onPostExecuteへreturn
        }

        @Override
        protected void onPostExecute(String result) { //doInBackgroundが終わると呼び出される
            try {
                ProgressBar progressBar = progressBarWeakReference.get();

                JSONObject json = new JSONObject(result);
                String items = json.getString("Items");
                JSONArray itemsArray = new JSONArray(items);

                String hits_str = json.getString("hits");
                Random rand = new Random();
                int hits = rand.nextInt(Integer.parseInt(hits_str));
                Log.d("hits", String.valueOf(hits));
                JSONObject bookInfo = itemsArray.getJSONObject(hits).getJSONObject("Item");

                //なげえ
                //題名
                String title_result = bookInfo.getString("title");
                //オプション属性: optStringで取得
                //出版日
                String salesDate_result = bookInfo.optString("salesDate");
                //出版社
                String publisherName_result = bookInfo.optString("publisherName");
                //説明文
                String itemCaption_result = bookInfo.optString("itemCaption");
                //価格
                String price_result = bookInfo.optString("itemPrice");
                //（代表）著者
                String author_result = bookInfo.optString("author");
                //画像
                String image_URL_result = bookInfo.optString("largeImageUrl");
                //google Books URL
                String URL_result = bookInfo.optString("itemUrl");

                //SharedPreference
                editor.putString("title", title_result);
                editor.putString("salesDate", salesDate_result);
                editor.putString("publisherName", publisherName_result);
                editor.putString("author", author_result);
                editor.putString("itemCaption", itemCaption_result);
                editor.putString("URL", URL_result);
                editor.putString("image_URL", image_URL_result);
                editor.commit();

                String description = prefs.getString("itemCaption", "紹介文は登録されていません。");
                Log.d("count", String.valueOf(count));

                String title = prefs.getString("title", "・タイトル");
                String publishedDate = prefs.getString("salesDate", "・出版年月日");
                String publisher = prefs.getString("publisherName", "・出版社");
                String author = prefs.getString("author", "・著者");
                String URL = prefs.getString("URL", "");

                //if () {
                    TextView titleView = titleViewReference.get();
                    TextView dateView = dateViewReference.get();
                    TextView publisherView = publisherViewReference.get();
                    TextView descriptionView = descriptionViewReference.get();
                    TextView authorView = authorViewReference.get();
                    TextView urlView = urlViewReference.get();
                    ImageView imageView = imageViewWeakReference.get();
                    Activity activity = activityWeakReference.get();
                //}

                titleView.setText(title);
                dateView.setText(publishedDate);
                publisherView.setText(publisher);
                authorView.setText(author);
                urlView.setText(URL);
                descriptionView.setText(description);
                Linkify.addLinks(urlView, Linkify.ALL);

                progressBar.setVisibility(View.GONE);


                String image_str = prefs.getString("image_URL", "");
                Uri image_URL = Uri.parse(image_str);
                Picasso.get().
                        load(image_URL).
                        error(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.default_book, null)).
                        into(imageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


