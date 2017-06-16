package com.example.user.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<PinpointLocation> arrayMyData = null;
    private HttpGet hg = new HttpGet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // WEBアクセス( HttpGet )
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Void, String>() {

                    // 非同期処理
                    @Override
                    protected String doInBackground(String... params) {
                        String result = null;
                        HttpGet hg = new HttpGet();
                        result =
                                hg.execute(
                                        params[0],
                                        params[1],
                                        null
                                );

                        return result;
                    }

                    // UI スレッド処理
                    @Override
                    protected void onPostExecute(String json) {
                        super.onPostExecute(json);

                        // ListView のインスタンスを取得
                        ListView listview = (ListView) MainActivity.this.findViewById(R.id.listView);
                        // 専用クラス用
                        arrayMyData = new ArrayAdapter<PinpointLocation>(
                                MainActivity.this,
                                android.R.layout.simple_list_item_1,
                                android.R.id.text1
                        );

                        Gson gson = new Gson();
                        Weather weatherData = gson.fromJson(json, Weather.class);

                        // Weather クラス内の配列部分をセット
                        arrayMyData.addAll(weatherData.pinpointLocations);
                        listview.setAdapter(arrayMyData);

                        String reJson = gson.toJson(weatherData);
                        SharedPreferences sp = getSharedPreferences("lightbox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("json", reJson);
                        editor.commit();

                    }
                }.execute("http://weather.livedoor.com/forecast/webservice/json/v1?city=270000", "utf-8");
            }
        });
        // クリックした時のイベント作成
        ListView listview = (ListView) MainActivity.this.findViewById(R.id.listView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックされたビューの内部データ
                Object data = (Object) parent.getItemAtPosition(position);

                PinpointLocation myData = (PinpointLocation) data;
                Log.i("lightbox", "url:" + myData.link);

                // ブラウザの呼び出し
                callBrowser(myData.link);

            }
        });
    }

    private void callBrowser( String url ) {
        // ブラウザの呼び出し
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            return;
        }

        // 対応するアプリが無い
        Toast.makeText(
                MainActivity.this,
                "ブラウザを呼び出せません",
                Toast.LENGTH_LONG
        ).show();
        return;

    }
    public class PinpointLocation {
        String link;
        String name;

        @Override
        public String toString() {
            return name;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.lv_action1) {
            if ( arrayMyData == null || arrayMyData.getCount() == 0 ) {
                Toast.makeText(MainActivity.this,"データがありません",Toast.LENGTH_LONG).show();
                return true;
            }

            PinpointLocation data = new PinpointLocation();
            // 久留米市
            data.name = "\u4e45\u7559\u7c73\u5e02";
            data.link = "http://weather.livedoor.com/area/forecast/4020300";
            arrayMyData.insert(data,3);

            return true;
        }

        if (id == R.id.lv_action2) {
            if ( arrayMyData == null || arrayMyData.getCount() == 0 ) {
                Toast.makeText(MainActivity.this,"データがありません",Toast.LENGTH_LONG).show();
                return true;
            }

            PinpointLocation data = null;
            int count = arrayMyData.getCount();
            for ( int i = 0; i < count; i++ ) {
                data = arrayMyData.getItem(i);
                Log.i("lightbox",data.name + ":" + data.link);
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}

