package sv.edu.udb.login.gui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel para mantener el nombre de usuario
class UserViewModel : ViewModel() {
    // Estado mutable para el nombre de usuario
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username
// Función para actualizar el nombre de usuario
    fun setUsername(name: Any?) {
        viewModelScope.launch {
            _username.emit(name.toString())
        }
    }
}