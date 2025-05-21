package sv.edu.udb.login.JonathanReyes.View

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import sv.edu.udb.login.JonathanReyes.ViewModel.AuthViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    authViewModel: AuthViewModel,
    onRegistroExitoso: () -> Unit,
    navController: NavController
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoExito = false; onRegistroExitoso() },
            title = { Text("¡Registro exitoso!") },
            text = { Text("Tu cuenta ha sido creada correctamente.") },
            confirmButton = {
                Button(onClick = { mostrarDialogoExito = false; onRegistroExitoso() }) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (mensajeError != null) {
        AlertDialog(
            onDismissRequest = { mensajeError = null },
            title = { Text("Error") },
            text = { Text(mensajeError ?: "") },
            confirmButton = {
                Button(onClick = { mensajeError = null }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // TopAppBar con flecha de regreso
        TopAppBar(
            title = { Text("Registro de Usuario") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                cargando = true
                // --- Verificar si el correo ya existe antes de registrar ---
                val db = FirebaseFirestore.getInstance()
                db.collection("usuarios")
                    .whereEqualTo("correo", correo)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            cargando = false
                            mensajeError = "Ya existe un usuario con este correo electrónico."
                        } else {
                            // Si no existe, proceder con el registro
                            authViewModel.registrarUsuario(
                                nombre, apellido, correo, contrasena,
                                onSuccess = {
                                    cargando = false
                                    mostrarDialogoExito = true
                                },
                                onError = {
                                    cargando = false
                                    mensajeError = when {
                                        it.contains("email address is already in use", ignoreCase = true) ->
                                            "El correo electrónico ya está en uso."
                                        it.contains("badly formatted", ignoreCase = true) ->
                                            "El correo electrónico no tiene un formato válido."
                                        it.contains("Password should be at least", ignoreCase = true) ->
                                            "La contraseña debe tener al menos 6 caracteres."
                                        else -> "Error: $it"
                                    }
                                }
                            )
                        }
                    }
                    .addOnFailureListener { e ->
                        cargando = false
                        mensajeError = "Error al verificar el correo: ${e.message}"
                    }
            },
            enabled = !cargando && nombre.isNotBlank() && apellido.isNotBlank() && correo.isNotBlank() && contrasena.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
        if (cargando) {
            CircularProgressIndicator()
        }
    }
}