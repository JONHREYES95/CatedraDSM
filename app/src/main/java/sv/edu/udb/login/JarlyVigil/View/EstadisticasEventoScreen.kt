package sv.edu.udb.login.JarlyVigil.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import sv.edu.udb.login.NelsonLuna.Model.Evento
import sv.edu.udb.login.HenryNajera.Model.Calificacion
import sv.edu.udb.login.HenryNajera.Model.CalificacionRepository
import sv.edu.udb.login.HenryNajera.Model.Comentario
import sv.edu.udb.login.HenryNajera.Model.ComentarioRepository
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasEventoScreen(
    eventos: List<Evento>,
    navController: NavController,
    paddingValues: PaddingValues
) {
    var mostrarFuturos by remember { mutableStateOf(false) }
    var mostrarPasados by remember { mutableStateOf(false) }
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val hoy = remember { Date() }
    val eventosFuturos = eventos.filter {
        val fechaEvento: Date? = try {
            sdf.parse(it.fecha)
        } catch (e: Exception) {
            null
        }
        fechaEvento != null && !fechaEvento.before(hoy)
    }
    val eventosPasados = eventos.filter {
        val fechaEvento: Date? = try {
            sdf.parse(it.fecha)
        } catch (e: Exception) {
            null
        }
        fechaEvento != null && fechaEvento.before(hoy)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                mostrarFuturos = !mostrarFuturos
                if (mostrarFuturos) mostrarPasados = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (mostrarFuturos) "Ocultar eventos futuros" else "Mostrar eventos futuros")
        }
        if (mostrarFuturos) {
            Spacer(modifier = Modifier.height(8.dp))
            if (eventosFuturos.isEmpty()) {
                Text(
                    "No hay eventos futuros para mostrar.",
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(eventosFuturos) { evento ->
                        var mostrarDialogo by remember { mutableStateOf(false) }
                        val asistentes = evento.asistentes.values.filterNotNull().count { it }
                        val noAsistentes = evento.asistentes.values.filterNotNull().count { !it }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    evento.titulo,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Fecha: ${evento.fecha}  Hora: ${evento.hora}")
                                Text("Ubicación: ${evento.ubicacion}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Asistirán: $asistentes")
                                Text("No asistirán: $noAsistentes")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { mostrarDialogo = true },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Ver calificaciones")
                                }
                            }
                        }
                        if (mostrarDialogo) {
                            CalificacionesDialog(eventoId = evento.id, onDismiss = { mostrarDialogo = false })
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                mostrarPasados = !mostrarPasados
                if (mostrarPasados) mostrarFuturos = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (mostrarPasados) "Ocultar eventos pasados" else "Mostrar eventos pasados")
        }
        if (mostrarPasados) {
            Spacer(modifier = Modifier.height(8.dp))
            if (eventosPasados.isEmpty()) {
                Text(
                    "No hay eventos pasados para mostrar.",
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(eventosPasados) { evento ->
                        var mostrarDialogo by remember { mutableStateOf(false) }
                        val asistentes = evento.asistentes.values.filterNotNull().count { it }
                        val noAsistentes = evento.asistentes.values.filterNotNull().count { !it }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(evento.titulo, style = MaterialTheme.typography.titleMedium)
                                Text("Fecha: ${evento.fecha}  Hora: ${evento.hora}")
                                Text("Ubicación: ${evento.ubicacion}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Asistieron: $asistentes")
                                Text("No asistieron: $noAsistentes")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { mostrarDialogo = true },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Ver calificaciones")
                                }
                            }
                        }
                        if (mostrarDialogo) {
                            CalificacionesDialog(eventoId = evento.id, onDismiss = { mostrarDialogo = false })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalificacionesDialog(
    eventoId: String,
    onDismiss: () -> Unit
) {
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
        title = { Text("Calificaciones") },
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