package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Settings Screen - Manages app preferences and user settings
 */
@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        // Load saved settings from preferences
        _uiState.value = _uiState.value.copy(
            userName = "John Doe", // Load from preferences
            userEmail = "john@example.com",
            isDarkMode = false,
            notificationsEnabled = true,
            biometricEnabled = false,
            currency = "USD"
        )
    }

    fun toggleDarkMode() {
        val newValue = !_uiState.value.isDarkMode
        _uiState.value = _uiState.value.copy(isDarkMode = newValue)
        // Save to preferences
    }

    fun toggleNotifications() {
        val newValue = !_uiState.value.notificationsEnabled
        _uiState.value = _uiState.value.copy(notificationsEnabled = newValue)
        // Save to preferences
    }

    fun toggleBiometric() {
        val newValue = !_uiState.value.biometricEnabled
        _uiState.value = _uiState.value.copy(biometricEnabled = newValue)
        // Save to preferences
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                // Implement data export logic
                // Generate CSV/JSON file with user's financial data
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun showClearDataDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = true)
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                // Clear all user data
                _uiState.value = _uiState.value.copy(showClearDataDialog = false)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun dismissDialog() {
        _uiState.value = _uiState.value.copy(showClearDataDialog = false)
    }
}

data class SettingsUiState(
    val userName: String = "",
    val userEmail: String = "",
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val currency: String = "USD",
    val showClearDataDialog: Boolean = false
)
