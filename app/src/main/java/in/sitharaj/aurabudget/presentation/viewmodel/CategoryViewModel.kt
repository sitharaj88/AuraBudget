package `in`.sitharaj.aurabudget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import `in`.sitharaj.aurabudget.domain.model.CategoryEntity
import `in`.sitharaj.aurabudget.domain.repository.CategoryRepository
import javax.inject.Inject

/**
 * ViewModel for Category Management Screen
 */
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                val mostUsed = categories.maxByOrNull { it.usageCount ?: 0 }?.name
                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    mostUsedCategory = mostUsed,
                    isLoading = false
                )
            }
        }
    }

    fun addCategory(name: String, icon: String, color: Long) {
        viewModelScope.launch {
            try {
                val newCategory = CategoryEntity(
                    name = name,
                    icon = icon,
                    color = color,
                    isDefault = false,
                    isActive = true
                )
                categoryRepository.insertCategory(newCategory)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun editCategory(category: CategoryEntity) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            try {
                categoryRepository.deleteCategory(categoryId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun toggleCategoryActive(categoryId: Long) {
        viewModelScope.launch {
            try {
                categoryRepository.toggleCategoryActive(categoryId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}

data class CategoryUiState(
    val categories: List<CategoryEntity> = emptyList(),
    val selectedCategory: CategoryEntity? = null,
    val mostUsedCategory: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
