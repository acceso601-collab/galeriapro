package com.ejemplo.galeriapro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.ejemplo.galeriapro.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_IMAGE_URL = "extra_image_url"
        fun newIntent(context: Context, imageUrl: String) =
            Intent(context, DetailActivity::class.java).apply { putExtra(EXTRA_IMAGE_URL, imageUrl) }
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        binding.detailImage.load(imageUrl) {
            crossfade(true)
            listener(onSuccess = { _, _ -> supportStartPostponedEnterTransition() })
        }

        binding.detailToolbar.setNavigationOnClickListener { supportFinishAfterTransition() }
    }
}
