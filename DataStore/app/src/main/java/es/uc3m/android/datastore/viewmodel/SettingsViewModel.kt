package es.uc3m.android.datastore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uc3m.android.datastore.storage.DataStoreHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStoreHelper: DataStoreHelper) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _isEnabled = MutableStateFlow(false)
    val isEnabled: StateFlow<Boolean> = _isEnabled.asStateFlow()

    init {
        // Observe DataStore changes
        viewModelScope.launch {
            dataStoreHelper.userName.collectLatest { name ->
                _userName.value = name
            }
        }
        viewModelScope.launch {
            dataStoreHelper.enabled.collectLatest { enabled ->
                _isEnabled.value = enabled
            }
        }
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            dataStoreHelper.saveUserName(name)
        }
    }

    fun saveEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreHelper.saveEnabled(enabled)
        }
    }
}