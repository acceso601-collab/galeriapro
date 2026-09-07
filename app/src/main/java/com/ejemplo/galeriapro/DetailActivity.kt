package com.ejemplo.galeriapro

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.load

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val url = intent.getStringExtra("image_url")
        val img = findViewById<ImageView>(R.id.imgFull)
        img.load(url)
    }
}
