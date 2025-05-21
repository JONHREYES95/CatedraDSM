package sv.edu.udb.login.JonathanReyes.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import sv.edu.udb.login.JonathanReyes.ViewModel.AdminUsuariosViewModel

@Composable
fun AdminUsuariosScreen(
    adminUsuariosViewModel: AdminUsuariosViewModel = viewModel(),
    paddingValues: PaddingValues
) {
    val usuarios by adminUsuariosViewModel.usuarios.collectAsState()
    val isLoading by adminUsuariosViewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(usuarios) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nombre: ${usuario.nombre} ${usuario.apellido}")
                            Text("Correo: ${usuario.correo}")
                            Text("Rol: ${usuario.rol}")
                            Row {
                                Button(
                                    onClick = {
                                        val nuevoRol = if (usuario.rol == "admin") "usuario" else "admin"
                                        adminUsuariosViewModel.actualizarRol(usuario.uid, nuevoRol)
                                    }
                                ) {
                                    Text(if (usuario.rol == "admin") "Hacer Usuario" else "Hacer Admin")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { adminUsuariosViewModel.eliminarUsuario(usuario.uid) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
