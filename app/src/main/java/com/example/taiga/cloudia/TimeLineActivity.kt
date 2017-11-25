package com.example.taiga.cloudia

import android.app.DownloadManager
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Toast

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class TimeLineActivity : AppCompatActivity() {

    var tokenCode: String? = null

    var accessToken: String? = null

    var coordinatorLayout:CoordinatorLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        coordinatorLayout=findViewById(R.id.coordinator)

        val fb=findViewById<FloatingActionButton>(R.id.fb_whisper) as FloatingActionButton
        fb.setOnClickListener(View.OnClickListener {
            Snackbar.make(coordinatorLayout!!, "ささやきたい！", Snackbar.LENGTH_LONG).show()
        })

        val intent = intent
        tokenCode = intent.getStringExtra("CODE")
        getAccessToken()

    }

    // POST
    //AccessToken取得
    private fun getAccessToken() {
        val formBody = FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", getString(R.string.client_id))
                .add("client_secret", getString(R.string.client_secret))
                .add("code", tokenCode)
                .build()

        val request = Request.Builder()
                .url("https://api.croudia.com/oauth/token")       // HTTPアクセス POST送信 テスト確認用ページ
                .post(formBody)
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //Toast.makeText(this@TimeLineActivity, "失敗！", Toast.LENGTH_SHORT).show()
                Snackbar.make(coordinatorLayout!!, "失敗", Snackbar.LENGTH_LONG).show()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val res = response.body()!!.string()
                runOnUiThread {
                    val json: JSONObject
                    try {
                        json = JSONObject(res)
                        accessToken = json.getString("access_token")
                        val manager = supportFragmentManager
                        val viewPager = findViewById<View>(R.id.view_pager) as ViewPager
                        val pagerAdapter=TLPagerAdapter(manager,accessToken!!)
                        viewPager.adapter=pagerAdapter
                        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
                        tabLayout.setupWithViewPager(viewPager)
                        //Toast.makeText(this@TimeLineActivity, "トークンゲット！", Toast.LENGTH_SHORT).show()
                        Snackbar.make(coordinatorLayout!!, "トークンゲット！", Snackbar.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}
