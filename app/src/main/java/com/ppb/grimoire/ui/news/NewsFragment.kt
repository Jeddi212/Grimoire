package com.ppb.grimoire.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.ppb.grimoire.*
import com.ppb.grimoire.databinding.FragmentNewsBinding
import com.ppb.grimoire.databinding.ItemNewsBinding
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class NewsFragment : Fragment(), OnItemClickListener{
    private lateinit var binding: FragmentNewsBinding
    var list = ArrayList<News>()

    fun getListNews(){
        binding.progressBar.visibility = View.VISIBLE
        val url = "https://newsapi.org/v2/top-headlines?country=id&apiKey=4c47daa5a8294b4f911942117bfdc09e"
        val client = AsyncHttpClient()
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers:
                Array<Header>,
                responseBody: ByteArray
            ) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE

                // Parsing JSON
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val json = JSONObject(result)
                    val result2 = json.getString("articles")
                    val jsonArray = JSONArray(result2)

                    for (i in 0 until jsonArray.length()) {
                        val news = News("","","","")
                        val jsonObject = jsonArray.getJSONObject(i)
                        news.title = jsonObject.getString("title")
                        news.description = jsonObject.getString("description")
                        news.photo = jsonObject.getString("urlToImage")
                        news.url = jsonObject.getString("url")
                        list.add(news)
                    }
                    showRecyclerList()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable,
            ) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("Error",errorMessage)
            }
        })
    }
    private fun showRecyclerList() {
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        val listNewsAdapter = ListNewsAdapter(list,this)
        binding.rvNews.adapter = listNewsAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentNewsBinding.inflate(layoutInflater)
        binding.rvNews.setHasFixedSize(true)
        getListNews()
        val root: View = binding.root
        return root
    }

    companion object {
        private val TAG = NewsFragment::class.java.simpleName
    }

    override fun onItemClicked(news: News) {
        startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(news.url)))
    }

}