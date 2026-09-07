package com.ejemplo.galeriapro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var adapter: ImageAdapter
    private lateinit var swipeRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefresh = findViewById(R.id.swipeRefresh)
        recyclerView = findViewById(R.id.recyclerView)

        // Configurar Grid de 2 columnas
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = ImageAdapter { image ->
            // Abrir pantalla de detalle
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("image_url", "https://picsum.photos/id/${image.id}/1080/1920")
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Detectar scroll infinito
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItem >= totalItemCount - 4) {
                    viewModel.loadImages()
                }
            }
        })

        // Observar datos y actualizar UI
        lifecycleScope.launch {
            viewModel.images.collectLatest { list ->
                adapter.submitList(list)
            }
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            swipeRefresh.isRefreshing = false
        }
    }
}
