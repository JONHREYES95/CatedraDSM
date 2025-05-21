package sv.edu.udb.login.JonathanReyes.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
    private val _rol = MutableStateFlow("usuario")
    val rol: StateFlow<String> = _rol

    fun setRol(rol: String) {
        viewModelScope.launch {
            _rol.emit(rol)
        }
    }
    fun logout() {
        try {
            FirebaseAuth.getInstance().signOut()
        } catch (e: Exception) {
        }
        _username.value = ""
        _rol.value = "usuario"
    }
}