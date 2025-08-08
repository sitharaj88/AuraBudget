package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.sitharaj.aurabudget.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for theme management
 * Following MVVM pattern and clean architecture
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val isDarkTheme = preferencesRepository.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun toggleTheme() {
        viewModelScope.launch {
            preferencesRepository.setDarkTheme(!isDarkTheme.value)
        }
    }
}
