package com.ejemplo.galeriapro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    private val _images = MutableLiveData<List<String>>()
    val images: LiveData<List<String>> = _images

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPage = 1

    init {
        loadImages()
    }

    fun loadImages() {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = ApiService.instance.getImages(currentPage)
                // Convertimos los modelos a URLs para simplificar
                val urls = result.map { "https://picsum.photos/id/${it.id}/400/400" }
                _images.value = (_images.value ?: emptyList()) + urls
                currentPage++
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _loading.value = false
            }
        }
    }

    fun refresh() {
        currentPage = 1
        _images.value = emptyList()
        loadImages()
    }
}
