package sv.edu.udb.login.JonathanReyes.View

import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    val username = userViewModel.username.value ?: "Usuario"
    val rol = userViewModel.rol.value ?: "usuario"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Nombre de usuario:", style = MaterialTheme.typography.titleMedium)
            Text(username, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Rol:", style = MaterialTheme.typography.titleMedium)
            Text(rol, style = MaterialTheme.typography.bodyLarge)
        }
    }
}