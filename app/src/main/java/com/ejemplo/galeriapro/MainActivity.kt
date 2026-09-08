package com.ejemplo.galeriapro

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ejemplo.galeriapro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: GalleryViewModel
    private lateinit var adapter: ImageAdapter
    private var isToolbarBlurred = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]

        adapter = ImageAdapter { image, sharedView ->
            openDetail(image, sharedView)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.images.observe(this) { images ->
            adapter.submitList(images)
            binding.swipeRefresh.isRefreshing = false
            adapter.showShimmer(false)
        }

        viewModel.loading.observe(this) { isLoading ->
            adapter.showShimmer(isLoading)
        }

        binding.fabToggle.setOnClickListener {
            toggleGridColumns()
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val offset = recyclerView.computeVerticalScrollOffset()
                val shouldBlur = offset > 200
                if (shouldBlur != isToolbarBlurred) {
                    isToolbarBlurred = shouldBlur
                    binding.toolbar.animate().alpha(if (shouldBlur) 1f else 0.3f).setDuration(200).start()
                }
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                if (layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - 4) {
                    viewModel.loadImages()
                }
            }
        })
    }

    private fun openDetail(imageUrl: String, sharedView: View) {
        val intent = DetailActivity.newIntent(this, imageUrl)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedView, getString(R.string.transition_image))
        startActivity(intent, options.toBundle())
    }

    private fun toggleGridColumns() {
        val layoutManager = binding.recyclerView.layoutManager as GridLayoutManager
        val newSpanCount = if (layoutManager.spanCount == 2) 3 else 2
        layoutManager.spanCount = newSpanCount
        binding.recyclerView.adapter?.notifyDataSetChanged()

        binding.fabToggle.animate().rotationBy(180f).setDuration(300).withEndAction {
            binding.fabToggle.rotation = 0f
            binding.fabToggle.setImageResource(if (newSpanCount == 2) R.drawable.ic_grid_2 else R.drawable.ic_grid_3)
        }.start()
    }
}
