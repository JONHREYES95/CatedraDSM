package sv.edu.udb.login.HenryNajera.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import sv.edu.udb.login.HenryNajera.Model.Comentario
import sv.edu.udb.login.HenryNajera.Model.ComentarioRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComentariosScreen(
    eventoId: String,
    userViewModel: UserViewModel,
    onBack: () -> Unit
) {
    var comentarios by remember { mutableStateOf<List<Comentario>>(emptyList()) }
    var nuevoComentario by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val userId = userViewModel.username.value ?: "Invitado"

    // Cargar comentarios al iniciar
    LaunchedEffect(eventoId) {
        scope.launch {
            cargando = true
            comentarios = ComentarioRepository.obtenerComentarios(eventoId)
            cargando = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comentarios") },
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
            if (cargando) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(comentarios) { comentario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(comentario.autor, style = MaterialTheme.typography.labelMedium)
                                Text(comentario.texto)
                                Text(comentario.fecha, style = MaterialTheme.typography.labelSmall)
                                if (comentario.autor == userId) {
                                    TextButton(
                                        onClick = {
                                            scope.launch {
                                                ComentarioRepository.eliminarComentario(comentario.id)
                                                comentarios = ComentarioRepository.obtenerComentarios(eventoId)
                                            }
                                        }
                                    ) {
                                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nuevoComentario,
                    onValueChange = { nuevoComentario = it },
                    label = { Text("Agregar comentario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        scope.launch {
                            ComentarioRepository.agregarComentario(
                                eventoId = eventoId,
                                autor = userId,
                                texto = nuevoComentario
                            )
                            nuevoComentario = ""
                            comentarios = ComentarioRepository.obtenerComentarios(eventoId)
                        }
                    },
                    enabled = nuevoComentario.isNotBlank(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Enviar")
                }
            }
        }
    }
}
