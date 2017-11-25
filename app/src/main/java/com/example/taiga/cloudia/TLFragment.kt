package com.example.taiga.cloudia

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.widget.ArrayAdapter
import android.widget.ListView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import org.json.JSONArray


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TLFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TLFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TLFragment : Fragment() {

    var tlList: ListView? = null
    var TLAdapter: ArrayAdapter<String>? = null

    var accessToken: String? = null

    fun newInstance(tlType: String, accesToken: String): TLFragment {
        val frag = TLFragment()
        val b = Bundle()
        b.putString("TOKEN", accesToken)
        b.putString("TYPE", tlType)
        frag.arguments = b
        return frag
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_tl, null)
        tlList = view.findViewById<ListView>(R.id.tl_list) as ListView
        TLAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1)

        accessToken = this.arguments.getString("TOKEN")

        getPublicTL()

        return view
    }

    // GET
    private fun getPublicTL() {
        val request = Request.Builder()
                .url("https://api.croudia.com/2/statuses/public_timeline.json")
                .header("Authorization", "Bearer")
                .header("Authorization", accessToken)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val res = response.body()!!.string()


                val resJson = JSONArray(res)
                for (i in 0..19) {
                    val status = resJson.getJSONObject(i)
                    val text = status.getString("text")
                    TLAdapter!!.add(text)
                }

                activity.runOnUiThread(Runnable {
                    tlList!!.adapter = TLAdapter
                })
            }
        })
    }
}
