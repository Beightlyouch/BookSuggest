package com.beightlyouch.booksuggest;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //ボタン
    Button button;
    TextView titleView;
    TextView dateView;
    TextView descriptionView;

    //API
    private final String API_URL_PREFIX = "www.googleapis.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        titleView = findViewById(R.id.titleView);
        dateView = findViewById(R.id.dateView);
        descriptionView = findViewById(R.id.descriptionView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //匿名クラス
                new AsyncTask<String, Void, String>() { //ボタンを押した時に非同期処理を開始します

                    @Override
                    protected String doInBackground(String... params) {
                        final StringBuilder result = new StringBuilder();
                        Uri.Builder uriBuilder = new Uri.Builder(); //Uri.Builderで要素を入力
                        uriBuilder.scheme("https");
                        uriBuilder.authority(API_URL_PREFIX);
                        uriBuilder.path("/books/v1/volumes");
                        uriBuilder.appendQueryParameter("q", "pen");
                        final String uriStr = uriBuilder.build().toString(); //URIを作成して文字列に

                        try {
                            URL url = new URL(uriStr); //文字列からURLに変換
                            HttpURLConnection con = null; //HTTP接続の設定を入力していく
                            con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            con.setDoInput(true);
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
                            JSONObject bookInfo = itemsArray.getJSONObject(0).getJSONObject("volumeInfo");

                            String title = bookInfo.getString("title");
                            String publishedDate = bookInfo.getString("publishedDate");
                            String description = bookInfo.getString("description");

                            titleView.setText(title);
                            dateView.setText(publishedDate);
                            descriptionView.setText(description);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }.execute();
            }
        });
    }
}
