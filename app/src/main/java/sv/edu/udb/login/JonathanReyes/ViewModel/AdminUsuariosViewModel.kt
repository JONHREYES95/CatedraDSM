package sv.edu.udb.login.JonathanReyes.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import sv.edu.udb.login.JonathanReyes.Model.Usuario

class AdminUsuariosViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        cargarUsuarios()
    }
    fun cargarUsuarios() {
        viewModelScope.launch {
            _isLoading.value = true
            val snapshot = db.collection("usuarios").get().await()
            _usuarios.value = snapshot.documents.mapNotNull { doc ->
                val nombre = doc.getString("nombre") ?: ""
                val apellido = doc.getString("apellido") ?: ""
                val correo = doc.getString("correo") ?: ""
                val rol = doc.getString("rol") ?: "usuario"
                Usuario(doc.id, nombre, apellido, correo, rol)
            }
            _isLoading.value = false
        }
    }

    fun actualizarRol(uid: String, nuevoRol: String) {
        viewModelScope.launch {
            db.collection("usuarios").document(uid).update("rol", nuevoRol).await()
            cargarUsuarios()
        }
    }

    fun eliminarUsuario(uid: String) {
        viewModelScope.launch {
            db.collection("usuarios").document(uid).delete().await()
            cargarUsuarios()
        }
    }
}