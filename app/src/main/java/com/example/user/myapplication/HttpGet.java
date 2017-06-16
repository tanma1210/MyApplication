package com.example.user.myapplication;

/**
 * Created by user on 2017/06/12.
 */


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class HttpGet {

    // **********************************************
    // コンストラクタ
    // **********************************************
    public HttpGet() {
    }

    // **********************************************
    // 指定した URL へ 任意の charset で処理
    // **********************************************
    public String execute(String targetUrl,String targetCharset,Map<String,String> params) {

        StringBuffer web_data = new StringBuffer();

        try {
            // **********************************************
            // Query String の作成( 必要無ければ引数を null とする )
            // **********************************************
            String data = "";
            if ( params != null ) {
                Iterator<String> it =  params.keySet().iterator();
                String key = null;
                String value = null;
                while(it.hasNext()) {
                    key = it.next().toString();
                    value = params.get(key);
                    if ( !data.equals("") ) {
                        data += "&";
                    }
                    data += key + "=" + URLEncoder.encode(value, targetCharset) ;
                }
                if ( !data.equals("") ) {
                    targetUrl = targetUrl + "?" + data;
                }
            }

            // **********************************************
            // インターネットへの接続
            // **********************************************
            // 読み込む WEB上のターゲット
            URL url = new URL(targetUrl);
            // 接続オブジェクト
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            // GET メソッド
            http.setRequestMethod("GET");
            // 接続
            http.connect();

            // **********************************************
            // ストリームとして読み込む準備
            // **********************************************
            // 以下読み込み３点セット InputStream / InputStreamReader / BufferedReader
            InputStream input_stream = http.getInputStream();
            // UTF-8 でリーダーを作成
            InputStreamReader input_stream_reader = new InputStreamReader(input_stream, targetCharset);
            // 行単位で読み込む為の準備
            BufferedReader buffered_reader = new BufferedReader(input_stream_reader);

            // **********************************************
            // 行の一括読み込み
            // **********************************************
            String line_buffer = null;
            // BufferedReader は、readLine が null を返すと読み込み終了
            while ( null != (line_buffer = buffered_reader.readLine() ) ) {
                web_data.append( line_buffer );
                web_data.append( "\n" );
            }

            // **********************************************
            // 接続解除
            // **********************************************
            http.disconnect();
        }
        catch(Exception e) {
            // 失敗
            System.out.println( e.getMessage());
        }
        return web_data.toString();
    }
}
