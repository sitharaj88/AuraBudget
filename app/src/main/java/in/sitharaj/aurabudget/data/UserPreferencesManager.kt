package `in`.sitharaj.aurabudget.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[DARK_THEME_KEY] ?: false
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_THEME_KEY] = enabled
        }
    }
}
