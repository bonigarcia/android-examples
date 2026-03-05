package es.uc3m.android.external.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import es.uc3m.android.external.storage.ExternalStorageHelper
import es.uc3m.android.external.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExternalStorageViewModel(
    application: Application,
    private val externalStorageHelper: ExternalStorageHelper
) : AndroidViewModel(application) {

    private val _fileContent = MutableStateFlow("")
    val fileContent: StateFlow<String> get() = _fileContent

    private val _loadedImage = MutableStateFlow<Bitmap?>(null)
    val loadedImage: StateFlow<Bitmap?> get() = _loadedImage

    private var lastSavedImageUri: Uri? = null

    // Helper method to access strings from resources
    private fun getString(resId: Int, vararg formatArgs: Any): String =
        getApplication<Application>().getString(resId, *formatArgs)

    // App-specific Storage
    fun writeToAppSpecific(fileName: String, content: String) {
        viewModelScope.launch {
            if (externalStorageHelper.writeToAppSpecificStorage(fileName, content)) {
                _fileContent.value = getString(R.string.saved_app_specific)
            } else {
                _fileContent.value = getString(R.string.error_app_specific)
            }
        }
    }

    fun readFromAppSpecific(fileName: String) {
        viewModelScope.launch {
            _fileContent.value = externalStorageHelper.readFromAppSpecificStorage(fileName)
        }
    }

    // MediaStore
    fun saveImageToMediaStore(bitmap: Bitmap, displayName: String) {
        viewModelScope.launch {
            val uri = externalStorageHelper.saveImageToMediaStore(bitmap, displayName)
            lastSavedImageUri = uri
            _fileContent.value = if (uri != null) {
                getString(R.string.saved_media_store, uri.toString())
            } else {
                getString(R.string.error_media_store)
            }
        }
    }

    fun readImageFromMediaStore() {
        viewModelScope.launch {
            lastSavedImageUri?.let { uri ->
                val bitmap = externalStorageHelper.readImageFromUri(uri)
                _loadedImage.value = bitmap
                if (bitmap != null) {
                    _fileContent.value = getString(R.string.image_loaded)
                } else {
                    _fileContent.value = getString(R.string.load_image_error)
                }
            } ?: run {
                _fileContent.value = getString(R.string.save_image_first)
            }
        }
    }

    // SAF (Storage Access Framework)
    fun writeToUri(uri: Uri, content: String) {
        viewModelScope.launch {
            if (externalStorageHelper.writeToUri(uri, content)) {
                _fileContent.value = getString(R.string.saved_saf)
            } else {
                _fileContent.value = getString(R.string.error_saf)
            }
        }
    }

    fun readFromUri(uri: Uri) {
        viewModelScope.launch {
            _fileContent.value = externalStorageHelper.readFromUri(uri)
        }
    }
}