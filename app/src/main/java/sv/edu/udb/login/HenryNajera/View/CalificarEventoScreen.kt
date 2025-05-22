package sv.edu.udb.login.HenryNajera.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.navigation.NavController
import sv.edu.udb.login.HenryNajera.Model.CalificacionRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalificarEventoScreen(
    eventoId: String,
    userViewModel: UserViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    var calificacion by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val usuarioId = userViewModel.username.value ?: "Invitado"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calificar Evento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
            Text("Selecciona una calificación:")
            Row {
                (1..5).forEach { estrella ->
                    IconToggleButton(
                        checked = calificacion >= estrella,
                        onCheckedChange = { calificacion = estrella }
                    ) {
                        Icon(
                            imageVector = if (calificacion >= estrella) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentario (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    scope.launch {
                        CalificacionRepository.agregarCalificacion(
                            eventoId = eventoId,
                            usuarioId = usuarioId,
                            calificacion = calificacion,
                            comentario = comentario
                        )
                        mensaje = "¡Gracias por tu calificación!"
                    }
                },
                enabled = calificacion > 0
            ) {
                Text("Enviar")
            }
            mensaje?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}