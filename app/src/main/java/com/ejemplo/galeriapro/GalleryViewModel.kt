package com.ejemplo.galeriapro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {
    private val _images = MutableStateFlow<List<ImageModel>>(emptyList())
    val images: StateFlow<List<ImageModel>> = _images

    private var currentPage = 1

    init { loadImages() }

    fun loadImages() {
        viewModelScope.launch {
            try {
                val result = ApiService.instance.getImages(currentPage)
                _images.value = _images.value + result
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refresh() {
        currentPage = 1
        _images.value = emptyList()
        loadImages()
    }
}
