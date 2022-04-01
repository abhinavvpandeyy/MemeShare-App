package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currentUrl:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMeme(){

        val url = "https://meme-api.herokuapp.com/gimme"
        val meme_image= findViewById<ImageView>(R.id.meme_imageView)
        val progress_Bar= findViewById<ProgressBar>(R.id.progressBar)
        // Request a string response from the provided URL.
        progress_Bar.visibility=View.VISIBLE
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,

            Response.Listener{ response ->
                currentUrl=response.getString("url")
                Glide.with(this).load(currentUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_Bar.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_Bar.visibility=View.GONE
                        return false
                    }
                }).into(meme_image)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
            }
        )

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {

        val intent= Intent(Intent.ACTION_SEND)
        intent.type="text.plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Yo, I made this App!! Checkout this cool meme from Reddit $currentUrl")

        val chooser=Intent.createChooser(intent,"Meme Share")
        startActivity(chooser)

    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}