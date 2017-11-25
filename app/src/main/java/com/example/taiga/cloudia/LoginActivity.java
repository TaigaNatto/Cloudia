package com.example.taiga.cloudia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    WebView webView;

    private String LOGIN_URL;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LOGIN_URL="https://api.croudia.com/oauth/authorize?response_type=code&client_id="+getString(R.string.client_id);

        webView = (WebView) findViewById(R.id.login_web);
        //リンクをタップしたときに標準ブラウザを起動させない
        webView.setWebViewClient(new WebViewClient());
        //最初にログインページを表示する。
        webView.loadUrl(LOGIN_URL);
        //jacascriptを許可する
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String url) {

                int index = url.indexOf("=");
                if (index != -1) {
                    String judgeUrl =url.substring(0,index);
                    if (judgeUrl.equals(getString(R.string.judge_url))) {
                        Intent intent = new Intent(LoginActivity.this, TimeLineActivity.class);
                        intent.putExtra("CODE",getCode(url));
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this,"ログイン成功！", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                Toast.makeText(LoginActivity.this,"ログイン失敗！", Toast.LENGTH_SHORT).show();
                webView.loadUrl(LOGIN_URL);
                return false;
            }
        });

    }

    //todo =の部分は改善すべき
    public String getCode(String origin) {
        int index = origin.indexOf("=");
        if (index != -1) {
            String code = origin.substring(index + 1, origin.length());
            return code;
        }
        return null;
    }
}
