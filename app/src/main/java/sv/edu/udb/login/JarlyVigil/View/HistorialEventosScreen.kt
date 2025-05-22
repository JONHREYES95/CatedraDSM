package sv.edu.udb.login.JarlyVigil.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sv.edu.udb.login.JonathanReyes.ViewModel.UserViewModel
import sv.edu.udb.login.NelsonLuna.Model.Evento
import sv.edu.udb.login.NelsonLuna.Model.EventoRepository
import sv.edu.udb.login.HenryNajera.Model.Calificacion
import sv.edu.udb.login.HenryNajera.Model.CalificacionRepository
import sv.edu.udb.login.HenryNajera.Model.Comentario
import sv.edu.udb.login.HenryNajera.Model.ComentarioRepository

@Composable
fun HistorialEventosScreen(
    userViewModel: UserViewModel,
    userId: String,
    paddingValues: PaddingValues
) {
    var historial by remember { mutableStateOf<List<Evento>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var filtro by remember { mutableStateOf("todos") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                cargando = true
                historial = EventoRepository.obtenerHistorialEventos(userId)
                cargando = false
            } catch (e: Exception) {
                error = "Error al cargar historial"
                cargando = false
            }
        }
    }
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text("Historial de Eventos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                FilterChip("Todos", filtro == "todos") { filtro = "todos" }
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip("Asistí", filtro == "asistire") { filtro = "asistire" }
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip("No asistí", filtro == "no_asistire") { filtro = "no_asistire" }
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip("Tal vez", filtro == "tal_vez") { filtro = "tal_vez" }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (cargando) {
                CircularProgressIndicator()
            } else if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            } else {
                val eventosFiltrados = historial.filter { evento ->
                    val rsvp = evento.asistentes[userId]
                    when (filtro) {
                        "asistire" -> rsvp == true
                        "no_asistire" -> rsvp == false
                        "tal_vez" -> rsvp == null
                        else -> true
                    }
                }
                if (eventosFiltrados.isEmpty()) {
                    Text("No hay eventos en el historial para este filtro.")
                } else {
                    LazyColumn {
                        items(eventosFiltrados) { evento ->
                            EventoHistorialCard(evento)
                        }
                    }
                }
            }
        }
    }

@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = if (selected)
            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        else
            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(text)
    }
}

@Composable
fun EventoHistorialCard(evento: Evento) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Título: ${evento.titulo}", style = MaterialTheme.typography.titleMedium)
            Text("Descripción: ${evento.descripcion}")
            Text("Fecha: ${evento.fecha}  Hora: ${evento.hora}")
            Text("Ubicación: ${evento.ubicacion}")
            Button(
                onClick = { mostrarDialogo = true },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Visualizar calificaciones")
            }
        }
    }
    if (mostrarDialogo) {
        DetalleEventoDialog(eventoId = evento.id, onDismiss = { mostrarDialogo = false })
    }
}
@Composable
fun DetalleEventoDialog(eventoId: String, onDismiss: () -> Unit) {
    val scope = rememberCoroutineScope()
    var calificaciones by remember { mutableStateOf<List<Calificacion>>(emptyList()) }
    var comentarios by remember { mutableStateOf<List<Comentario>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(eventoId) {
        scope.launch {
            cargando = true
            calificaciones = CalificacionRepository.obtenerCalificaciones(eventoId)
            comentarios = ComentarioRepository.obtenerComentarios(eventoId)
            cargando = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ver calificaciones") },
        text = {
            if (cargando) {
                CircularProgressIndicator()
            } else {
                Column {
                    Text("Calificaciones:", style = MaterialTheme.typography.titleMedium)
                    if (calificaciones.isEmpty()) {
                        Text("No hay calificaciones.")
                    } else {
                        calificaciones.forEach {
                            Text("Usuario: ${it.usuarioId} - ${it.calificacion} estrellas")
                            Text("Comentario: ${it.comentario}")
                            Text("Fecha: ${it.fecha}")
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
